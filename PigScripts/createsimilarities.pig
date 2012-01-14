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

--dump intersection;
intersect_grp   = GROUP intersection BY (v1, v2);
--dump intersect_grp;
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
 
similarities_grouped = GROUP similarities by v1;
--DESCRIBE similarities_grouped;
--DUMP similarities_grouped;

hbase_format = FOREACH similarities_grouped GENERATE group, fbUDF.BagToMap.BagToMap(*);
--DESCRIBE hbase_format;
DUMP hbase_format;

--STORE hbase_format INTO 'tsimilarities' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('group_fid:*');

