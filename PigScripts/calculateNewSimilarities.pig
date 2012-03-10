--SCRIPT para calcular las nuevas similitudes. Hay que reemplazar en los dos filter el numero por un parámetro que sea el último fingerbookID para el cual se calcularon similitudes
REGISTER FBUDFS.jar;

loaded1 = LOAD 'hbase://tfinger' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('group_str:*', '-loadKey true') AS (id:chararray, group:map[]);
amounts1 = FOREACH loaded1 GENERATE fbUDF.ConvertToHashAmounts(*);
data1 = FOREACH amounts1 GENERATE FLATTEN($0) as (fid:chararray, hash:chararray);
data1_filtered = FILTER data1 BY (int)fid > 4;

loaded2 = LOAD 'hbase://tfinger' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('group_str:*', '-loadKey true') AS (id:chararray, group:map[]);
amounts2 = FOREACH loaded2 GENERATE fbUDF.ConvertToHashAmounts(*);
data2 = FOREACH amounts2 GENERATE FLATTEN($0) as (fid:chararray, hash:chararray);


grouped_edges = GROUP data1_filtered BY fid;
aug_edges     = FOREACH grouped_edges GENERATE FLATTEN(data1_filtered) AS (fid, hash), COUNT(data1_filtered) AS fid_out;

grouped_dups  = GROUP data2 BY fid;
aug_dups      = FOREACH grouped_dups GENERATE FLATTEN(data2) AS (fid, hash), COUNT(data2) AS fid_out;

edges_joined1  = JOIN aug_edges BY hash, aug_dups BY hash;
edges_joined2  = JOIN aug_dups BY hash, aug_edges BY hash;

edges_joined2_filtered = FILTER edges_joined2 BY (int)aug_dups::fid <= 4;

edges_united = UNION edges_joined1, edges_joined2_filtered;

intersection1  = FOREACH edges_united {
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

intersect_grp   = GROUP intersection1 BY (fid1, fid2);

intersect_sizes = FOREACH intersect_grp {
                    --
                    -- results in:
                    -- (X, Y, |X /\ Y|, |X| + |Y|)
                    --
                    intersection_size = (double)COUNT(intersection1);
                    GENERATE
                      FLATTEN(group)               AS (fid1, fid2),
                      intersection_size            AS intersection_size,
                      MAX(intersection1.added_size) AS added_size, -- hack, we only need this one 
			MAX(intersection1.hash_amount) AS hash_amount
                    ;
                  };

similarities = FOREACH intersect_sizes {
		similarity = (double)intersection_size/((double) hash_amount);
                 GENERATE
                   fid1         AS fid1,
                   fid2         AS fid2,
                   similarity 	AS similarity
                 ;
               };
 
similarities_grouped = GROUP similarities by fid1;

hbase_format = FOREACH similarities_grouped GENERATE group, fbUDF.BagToMap.BagToMap(*);
--DUMP hbase_format;

STORE hbase_format INTO 'tsimilarities' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('group_fid:*');

