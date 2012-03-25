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
                  --
                  -- results in:
                  -- (X, Y, |X| + |Y|)
                  -- 
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
                    --
                    -- results in:
                    -- (X, Y, |X /\ Y|, |X| + |Y|)
                    --
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
sim_hash = FOREACH sim_joined GENERATE similarities_filtered::v1 as fid1, similarities_filtered::v2 as fid2, edges_joined::aug_edges::v2 as hash; --AS fid1, fid2, hash;
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

sim_hash_amounts_grouped = GROUP sim_hash_amounts BY fid_comp;

hbase_format = FOREACH sim_hash_amounts_grouped GENERATE group, fbUDF.BagToMap.BagToMap(*), TOMAP((chararray)MAX(sim_hash_amounts.fid1), MAX(sim_hash_amounts.fid2), (chararray)MAX(sim_hash_amounts.fid2), MAX(sim_hash_amounts.fid1)), COUNT(sim_hash_amounts);
--dump hbase_format;

STORE hbase_format INTO 'tcomposite' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('finger:* group_fid:* info:total');

