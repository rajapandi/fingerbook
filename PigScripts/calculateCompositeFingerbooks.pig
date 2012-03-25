REGISTER FBUDFS.jar;

loaded1 = LOAD 'hbase://tfinger' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('group_str:*', '-loadKey true') AS (id:chararray, group:map[]);
amounts1 = FOREACH loaded1 GENERATE fbUDF.ConvertToHashAmounts(*);
data1 = FOREACH amounts1 GENERATE FLATTEN($0) as (fid:chararray, hash:chararray);

loaded2 = LOAD 'hbase://tfinger' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('group_str:*', '-loadKey true') AS (id:chararray, group:map[]);
amounts2 = FOREACH loaded2 GENERATE fbUDF.ConvertToHashAmounts(*);
data2 = FOREACH amounts2 GENERATE FLATTEN($0) as (fid:chararray, hash:chararray);

grouped_edges = GROUP data1 BY fid;
aug_edges     = FOREACH grouped_edges GENERATE FLATTEN(data1) AS (v1, v2), COUNT(data1) AS v1_out;

grouped_dups  = GROUP data2 BY fid;
aug_dups      = FOREACH grouped_dups GENERATE FLATTEN(data2) AS (v1, v2), COUNT(data2) AS v1_out;

edges_joined  = JOIN aug_edges BY v2, aug_dups BY v2;

intersection  = FOREACH edges_joined {
                  added_size = aug_edges::v1_out + aug_dups::v1_out;
                  GENERATE
                    aug_edges::v1 AS v1,
                    aug_dups::v1  AS v2,
                    added_size    AS added_size, 
		                aug_edges::v1_out AS v1_amount
                  ;
                };


intersect_grp   = GROUP intersection BY (v1, v2);

intersect_sizes = FOREACH intersect_grp {
                    intersection_size = (double)COUNT(intersection);
                    GENERATE
                      FLATTEN(group)               AS (v1, v2),
                      intersection_size            AS intersection_size,
                      MAX(intersection.added_size) AS added_size, -- hack, we only need this one 
			                MAX(intersection.v1_amount) AS v1_amount
                    ;
                  };

similarities = FOREACH intersect_sizes {
                 --
                 -- results in:
                 -- (X, Y, |X /\ Y|/|X U Y|)
                 --
                 --similarity = (double)intersection_size/((double)added_size-(double)intersection_size);
		            similarity = (double)intersection_size/((double) v1_amount);
                 GENERATE
                   v1         AS v1,
                   v2         AS v2,
                   similarity AS similarity
                 ;
               };
similarities_filtered = FILTER similarities BY similarity >= 0.75 and v1 != v2;

sim_joined  = JOIN similarities_filtered BY (v1,v2), edges_joined BY (aug_edges::v1, aug_dups::v1);

sim_hash = FOREACH sim_joined GENERATE similarities_filtered::v1 as fid1, similarities_filtered::v2 as fid2, edges_joined::aug_edges::v2 as hash; 
sim_hash_grp   = GROUP sim_hash BY (fid1, fid2, hash);
sim_hash_amounts = FOREACH sim_hash_grp {
                    hash_amount = (int)COUNT(sim_hash);
                    GENERATE
                      CONCAT(CONCAT(group.fid1,'-'), group.fid2)  AS fid_comp,
                      group.hash        AS hash,
                      hash_amount       AS hash_amount,
                      group.fid1        AS fid1,
                      group.fid2        AS fid2
                     ;
                  };

--TAGS
load_tags1 = LOAD 'hbase://tgroup' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('tag:*', '-loadKey true') AS (id:bytearray, group:map[]);
tag_amounts1 = FOREACH load_tags1 GENERATE fbUDF.TagsMapToBag(*);
tags1 = FOREACH tag_amounts1 GENERATE FLATTEN($0) as (fid:chararray, tag:chararray);

load_tags2 = LOAD 'hbase://tgroup' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('tag:*', '-loadKey true') AS (id:bytearray, group:map[]);
tag_amounts2 = FOREACH load_tags2 GENERATE fbUDF.TagsMapToBag(*);
tags2 = FOREACH tag_amounts2 GENERATE FLATTEN($0) as (fid:chararray, tag:chararray);

grouped_tags1 = GROUP tags1 BY fid;
aug_tags1     = FOREACH grouped_tags1 GENERATE FLATTEN(tags1) AS (fid, tag);

grouped_tags2  = GROUP tags2 BY fid;
aug_tags2      = FOREACH grouped_tags2 GENERATE FLATTEN(tags2) AS (fid, tag);

tags_joined  = JOIN aug_tags1 BY tag, aug_tags2 BY tag;

tags_intersection  = FOREACH tags_joined {
                  GENERATE
                    aug_tags1::fid  AS fid1,
                    aug_tags2::fid  AS fid2,
                    aug_tags1::tag  AS tag
                  ;
                };


tags_intersection_grp = GROUP tags_intersection BY (fid1, fid2);


tags_by_fids = FOREACH tags_intersection_grp {
                    GENERATE
                      FLATTEN(group)               AS (fid1, fid2),
                      tags_intersection.tag AS tags
                    ;
                  };

--TAGS

joined_with_tags = JOIN sim_hash_amounts BY (fid1,fid2) left outer, tags_by_fids BY (fid1, fid2);

sim_hash_amounts_grouped = GROUP joined_with_tags BY sim_hash_amounts::fid_comp;

hbase_format = FOREACH sim_hash_amounts_grouped GENERATE group, fbUDF.BagToMap.BagToMap(*), TOMAP((chararray)MAX(joined_with_tags.sim_hash_amounts::fid1), MAX(joined_with_tags.sim_hash_amounts::fid2), (chararray)MAX(joined_with_tags.sim_hash_amounts::fid2), MAX(joined_with_tags.sim_hash_amounts::fid1)), COUNT(joined_with_tags), fbUDF.BagToMap.TupleBagToMap( TOP(1,0,joined_with_tags.tags_by_fids::tags));
--dump hbase_format;

STORE hbase_format INTO 'tcomposite' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('finger:* group_fid:* info:total tag:*');

