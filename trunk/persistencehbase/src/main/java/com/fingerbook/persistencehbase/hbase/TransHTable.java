package com.fingerbook.persistencehbase.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RowLock;

public class TransHTable {

	private HTable table;
	
	public TransHTable(String tableName) throws IOException {

		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH
	    HBaseConfiguration config = new HBaseConfiguration();

	    // This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    this.table = new HTable(config, tableName);
	    this.table.setAutoFlush(false);
	    		
	}
	
	public void put(byte[] rowId, byte[] family, byte[] column, byte[] value) throws IOException {
		
//		Put put = new Put(rowLock.getRow(), rowLock);
		Put put = new Put(rowId);
		
		put.add(family, column, value);
		
		this.table.put(put);
		
	}
	
	public void put(RowLock rowLock, byte[] family, byte[] column, byte[] value) throws IOException {
		
		Put put = new Put(rowLock.getRow(), rowLock);
		
		put.add(family, column, value);
		
		this.table.put(put);
		
	}
	
	public void commit() throws IOException {
		this.table.flushCommits();
	}
	
	public void commitAndDestroy() throws IOException {
		this.commit();
		this.table.close();
	}
	
	public RowLock lockRow(byte[] rowId) throws IOException {
		
		return table.lockRow(rowId);
	}
}
