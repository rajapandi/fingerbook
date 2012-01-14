REGISTER FBUDFS.jar;

loaded1 = LOAD 'hbase://tfinger' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('group_str:*', '-loadKey true') AS (id:chararray, group:map[]);
amounts1 = FOREACH loaded1 GENERATE fbUDF.ConvertToHashAmounts(*);
data1 = FOREACH amounts1 GENERATE FLATTEN($0) as (fid:chararray, hash:chararray);
data1_filtered = FILTER data1 BY (int)fid >= 6;

loaded2 = LOAD 'hbase://tfinger' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('group_str:*', '-loadKey true') AS (id:chararray, group:map[]);
amounts2 = FOREACH loaded2 GENERATE fbUDF.ConvertToHashAmounts(*);
data2 = FOREACH amounts2 GENERATE FLATTEN($0) as (fid:chararray, hash:chararray);
data2_filtered = FILTER data1 BY (int)fid < 6;


grouped_edges = GROUP data1_filtered BY fid;
aug_edges     = FOREACH grouped_edges GENERATE FLATTEN(data1_filtered) AS (fid, hash), COUNT(data1_filtered) AS fid_out;

grouped_dups  = GROUP data2_filtered BY fid;
aug_dups      = FOREACH grouped_dups GENERATE FLATTEN(data2_filtered) AS (fid, hash), COUNT(data2_filtered) AS fid_out;

edges_joined  = JOIN aug_edges BY hash, aug_dups BY hash;

intersection1  = FOREACH edges_joined {
                  --
                  -- results in:
                  -- (X, Y, |X| + |Y|)
                  -- 
                  added_size = aug_edges::fid_out + aug_dups::fid_out;
                  GENERATE
                    aug_edges::fid AS fid1,
                    aug_dups::fid  AS fid2,
                    added_size    AS added_size, 
		    aug_edges::fid_out AS hash_amount
                  ;
                };

intersection2  = FOREACH edges_joined {
                  --
                  -- results in:
                  -- (X, Y, |X| + |Y|)
                  -- 
                  added_size = aug_edges::fid_out + aug_dups::fid_out;
                  GENERATE
                    aug_dups::fid AS fid1,
                    aug_edges::fid  AS fid2,
                    added_size    AS added_size, 
		    aug_dups::fid_out AS hash_amount
                  ;
                };

united = UNION intersection1, intersection2;

intersect_grp   = GROUP united BY (fid1, fid2);

intersect_sizes = FOREACH intersect_grp {
                    --
                    -- results in:
                    -- (X, Y, |X /\ Y|, |X| + |Y|)
                    --
                    intersection_size = (double)COUNT(united);
                    GENERATE
                      FLATTEN(group)               AS (fid1, fid2),
                      intersection_size            AS intersection_size,
                      MAX(united.added_size) AS added_size, -- hack, we only need this one 
			MAX(united.hash_amount) AS hash_amount
                    ;
                  };

similarities = FOREACH intersect_sizes {
                 --
                 -- results in:
                 -- (X, Y, |X /\ Y|/|X U Y|)
                 --
                 --similarity = (double)intersection_size/((double)added_size-(double)intersection_size);
		similarity = (double)intersection_size/((double) hash_amount);
                 GENERATE
                   fid1         AS fid1,
                   fid2         AS fid2,
                   similarity AS similarity
                 ;
               };
 
similarities_grouped = GROUP similarities by fid1;

hbase_format = FOREACH similarities_grouped GENERATE group, fbUDF.BagToMap.BagToMap(*);
--DUMP hbase_format;

STORE hbase_format INTO 'tsimilarities' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('group_fid:*');

