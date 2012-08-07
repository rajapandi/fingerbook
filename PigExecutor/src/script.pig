loaded1 = LOAD 'hbase://tfinger' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('group_str:*', '-loadKey true') AS (id:chararray, group:map[]); 
--dump loaded1;
--STORE loaded1 INTO 'out_fb_pig';
STORE loaded1 INTO '$outpath';