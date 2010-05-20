package com.fingerbook.persistencehbase;

import java.io.IOException;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.ColumnCountGetFilter;
import org.apache.hadoop.hbase.filter.ColumnValueFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.util.Bytes;

import com.fingerbook.models.FileInfo;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.persistencehbase.hbase.ColumnCountFilterAux;
import com.fingerbook.persistencehbase.hbase.ColumnPaginationFilter;
import com.fingerbook.persistencehbase.hbase.HbaseManager;
import com.fingerbook.persistencehbase.hbase.HbaseManagerException;

public class TestTransaction {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Fingerbook fb = new Fingerbook();
		
		Fingerprints fp1 = new Fingerprints();
		
		FileInfo fi1 = new FileInfo();
		
		
		ColumnPaginationFilter colFilter = new ColumnPaginationFilter(1,0);
//		FilterList filter = new FilterList(Operator.MUST_PASS_ALL);
//		filter.filterRowKey(Bytes.toBytes(1L), 0, 8);
//		filter.addFilter(colFilter);
		
		ColumnCountGetFilter cvf = new ColumnCountGetFilter(1);
		ColumnCountFilterAux ccf = new ColumnCountFilterAux(1);
		
		Get get = new Get(Bytes.toBytes(1L));
//		get.setFilter(colFilter);
		get.setFilter(cvf);
//		get.setFilter(ccf);
		
		Scan s = new Scan();
		s.addFamily(Bytes.toBytes(PersistentFingerbook.TGROUP_FINGER_FAMILY));
//		s.setFilter(colFilter);
		
		try {
			HTable table = HbaseManager.getTable(PersistentFingerbook.GROUP_TABLE_NAME);
			Result r = table.get(get);
		    NavigableMap<byte[],byte[]> ret = r.getFamilyMap(Bytes.toBytes(PersistentFingerbook.TGROUP_FINGER_FAMILY));
			
		    if(ret != null) {
			    for(byte[] hashFNCol: ret.keySet()) {
					
	//				String shaHash = Bytes.toString(shaHashB); 
					String shaHash = PersistentFingerbook.getHashFromHashFNCol(hashFNCol);
					String fileName = PersistentFingerbook.getFileNameFromHashFNCol(hashFNCol);
					long sizeInBytes = Bytes.toLong(ret.get(hashFNCol));
					
					System.out.println("Hash: " + shaHash + " - Name: " + fileName);
			    }
		    }
		    else {
		    	System.out.println("NUULLLL");
		    }
		    
//			ResultScanner scanner = table.getScanner(s);
//			for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
//		        // print out the row we found and the columns we were looking for
//		        System.out.println("Found row: " + rr);
//			}
		    
		    System.out.println("END");
			
		} catch (HbaseManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
