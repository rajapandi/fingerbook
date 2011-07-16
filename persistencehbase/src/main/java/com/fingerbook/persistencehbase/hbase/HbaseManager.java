package com.fingerbook.persistencehbase.hbase;

import java.io.IOException;
import java.util.NavigableMap;
import java.util.Vector;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.ColumnPaginationFilter;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseManager {
	
	public static Configuration config;
	
	public static HTable getTable(String tableName) throws HbaseManagerException {
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH
		
//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}

	    // This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table;
		try {
			table = new HTable(config, tableName);
		} catch (IOException e) {
			e.printStackTrace();
			throw new HbaseManagerException(e.getMessage());
		}
		
		return table;
	}

	public static void putValue(String tableName, byte[] rowId, String columnFamily, String columnName, byte[] value) throws IOException {
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH

//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}

	    // This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);

	    // To add to a row, use Put.  A Put constructor takes the name of the row
	    // you want to insert into as a byte array.  In HBase, the Bytes class has
	    // utility for converting all kinds of java types to byte arrays.  In the
	    // below, we are converting the String "myLittleRow" into a byte array to
	    // use as a row key for our update. Once you have a Put instance, you can
	    // adorn it by setting the names of columns you want to update on the row,
	    // the timestamp to use in your update, etc.If no timestamp, the server
	    // applies current time to the edits.
	    Put p = new Put(rowId);

	    // To set the value you'd like to update in the row 'myLittleRow', specify
	    // the column family, column qualifier, and value of the table cell you'd
	    // like to update.  The column family must already exist in your table
	    // schema.  The qualifier can be anything.  All must be specified as byte
	    // arrays as hbase is all about byte arrays.  Lets pretend the table
	    // 'myLittleHBaseTable' was created with a family 'myLittleFamily'.
	    p.add(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName), value);

	    // Once you've adorned your Put instance with all the updates you want to
	    // make, to commit it do the following (The HTable#put method takes the
	    // Put instance you've been building and pushes the changes you made into
	    // hbase)
	    table.put(p);
	    
	    table.close();
	}
	
	public static void putValue(String tableName, byte[] rowId, String columnFamily, byte[] columnName, byte[] value) throws IOException {
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH

//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}

	    // This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);

	    // To add to a row, use Put.  A Put constructor takes the name of the row
	    // you want to insert into as a byte array.  In HBase, the Bytes class has
	    // utility for converting all kinds of java types to byte arrays.  In the
	    // below, we are converting the String "myLittleRow" into a byte array to
	    // use as a row key for our update. Once you have a Put instance, you can
	    // adorn it by setting the names of columns you want to update on the row,
	    // the timestamp to use in your update, etc.If no timestamp, the server
	    // applies current time to the edits.
	    Put p = new Put(rowId);

	    // To set the value you'd like to update in the row 'myLittleRow', specify
	    // the column family, column qualifier, and value of the table cell you'd
	    // like to update.  The column family must already exist in your table
	    // schema.  The qualifier can be anything.  All must be specified as byte
	    // arrays as hbase is all about byte arrays.  Lets pretend the table
	    // 'myLittleHBaseTable' was created with a family 'myLittleFamily'.
	    p.add(Bytes.toBytes(columnFamily), columnName, value);

	    // Once you've adorned your Put instance with all the updates you want to
	    // make, to commit it do the following (The HTable#put method takes the
	    // Put instance you've been building and pushes the changes you made into
	    // hbase)
	    table.put(p);
	    
	    table.close();
	}

	public static void putValue(String tableName, String rowId, String columnFamily, String columnName, String value) throws IOException {
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH

//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}

	    // This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);

	    // To add to a row, use Put.  A Put constructor takes the name of the row
	    // you want to insert into as a byte array.  In HBase, the Bytes class has
	    // utility for converting all kinds of java types to byte arrays.  In the
	    // below, we are converting the String "myLittleRow" into a byte array to
	    // use as a row key for our update. Once you have a Put instance, you can
	    // adorn it by setting the names of columns you want to update on the row,
	    // the timestamp to use in your update, etc.If no timestamp, the server
	    // applies current time to the edits.
	    Put p = new Put(Bytes.toBytes(rowId));

	    // To set the value you'd like to update in the row 'myLittleRow', specify
	    // the column family, column qualifier, and value of the table cell you'd
	    // like to update.  The column family must already exist in your table
	    // schema.  The qualifier can be anything.  All must be specified as byte
	    // arrays as hbase is all about byte arrays.  Lets pretend the table
	    // 'myLittleHBaseTable' was created with a family 'myLittleFamily'.
	    p.add(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName),
	      Bytes.toBytes(value));

	    // Once you've adorned your Put instance with all the updates you want to
	    // make, to commit it do the following (The HTable#put method takes the
	    // Put instance you've been building and pushes the changes you made into
	    // hbase)
	    table.put(p);
	    
	    table.close();
	}
	
	public static byte[] getValue(String tableName, byte[] rowId, String columnFamily, byte[] columnName) throws IOException {
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH

//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
		
		byte [] value = null;
	    
		// This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);
	    
		// Now, to retrieve the data we just wrote. The values that come back are
	    // Result instances. Generally, a Result is an object that will package up
	    // the hbase return into the form you find most palatable.
//	    Get g = new Get(Bytes.toBytes(rowId));
	    Get g = new Get(rowId);
	    g.addColumn(Bytes.toBytes(columnFamily), columnName);
	    Result r = table.get(g);
	    if(r != null && !r.isEmpty()) {
	    	value = r.getValue(Bytes.toBytes(columnFamily), columnName);
	    }
	    // If we convert the value bytes, we should get back 'Some Value', the
	    // value we inserted at this location.
//	    String valueStr = Bytes.toString(value);
//	    System.out.println("GET: " + valueStr);
		
		return value;
	}
	
	public static NavigableMap<byte[],NavigableMap<byte[],byte[]>> getFullRowMap(String tableName, byte[] rowId) throws IOException {
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH

//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
	    
		// This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);
	    
		// Now, to retrieve the data we just wrote. The values that come back are
	    // Result instances. Generally, a Result is an object that will package up
	    // the hbase return into the form you find most palatable.
	    Get g = new Get(rowId);
	    Result r = table.get(g);
	    
//	    NavigableMap<byte[],byte[]> ret = r.getFamilyMap(Bytes.toBytes(columnFamily));
	    NavigableMap<byte[],NavigableMap<byte[],byte[]>> ret = r.getNoVersionMap();
	    
		
		return ret;
	}
	
	public static NavigableMap<byte[],byte[]> getMembersMap(String tableName, byte[] rowId, String columnFamily) throws IOException {
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH

//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
	    
		// This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);
	    
		// Now, to retrieve the data we just wrote. The values that come back are
	    // Result instances. Generally, a Result is an object that will package up
	    // the hbase return into the form you find most palatable.
	    Get g = new Get(rowId);
	    
	    g.addFamily(Bytes.toBytes(columnFamily));
	    
	    Result r = table.get(g);
	    NavigableMap<byte[],byte[]> ret = r.getFamilyMap(Bytes.toBytes(columnFamily));
	    
		
		return ret;
	}
	
	public static int getMembersMapCount(String tableName, byte[] rowId, String columnFamily) throws IOException {
		
		int total = 0;
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH

//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
	    
		// This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);
	    
		// Now, to retrieve the data we just wrote. The values that come back are
	    // Result instances. Generally, a Result is an object that will package up
	    // the hbase return into the form you find most palatable.
	    Get g = new Get(rowId);
	    g.addFamily(Bytes.toBytes(columnFamily));
	    Result r = table.get(g);
	    NavigableMap<byte[],byte[]> ret = r.getFamilyMap(Bytes.toBytes(columnFamily));
	    
	    if(ret != null) {
	    	total = ret.size();
	    }
		
		return total;
	}
	
	public static NavigableMap<byte[],byte[]> getMembersMapPag(String tableName, byte[] rowId, String columnFamily, int limit, int offset) throws IOException {
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH

//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
	    
		// This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);
	    
		// Now, to retrieve the data we just wrote. The values that come back are
	    // Result instances. Generally, a Result is an object that will package up
	    // the hbase return into the form you find most palatable.
	    Get g = new Get(rowId);
	    
	    g.setFilter(new ColumnPaginationFilter(limit,offset));
	    
	    g.addFamily(Bytes.toBytes(columnFamily));
	    
	    Result r = table.get(g);
	    NavigableMap<byte[],byte[]> ret = r.getFamilyMap(Bytes.toBytes(columnFamily));
	    
		
		return ret;
	}
	
	public static Vector<String> scanTable(String tableName, String columnFamily, String columnName) throws IOException {
		
		Vector<String> ret = new Vector<String>();
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH

//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
	    
		// This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);
	    
		// Sometimes, you won't know the row you're looking for. In this case, you
	    // use a Scanner. This will give you cursor-like interface to the contents
	    // of the table.  To set up a Scanner, do like you did above making a Put
	    // and a Get, create a Scan.  Adorn it with column names, etc.
	    Scan s = new Scan();
	    
	    if(columnFamily != null) {
	    	if(columnName != null) {
	    		s.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));
	    	}
	    	else {
	    		s.addFamily(Bytes.toBytes(columnFamily));
	    	}
	    }
	    
	    ResultScanner scanner = table.getScanner(s);
	    try {
	      // Scanners return Result instances.
	      // Now, for the actual iteration. One way is to use a while loop like so:
	      for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
	        // print out the row we found and the columns we were looking for
	        System.out.println("Found row: " + rr);
	        ret.add(rr.toString());
	      }

	      // The other approach is to use a foreach loop. Scanners are iterable!
	      // for (Result rr : scanner) {
	      //   System.out.println("Found row: " + rr);
	      // }
	    } finally {
	      // Make sure you close your scanners when you are done!
	      // Thats why we have it inside a try/finally clause
	      scanner.close();
	    }
		
		return ret;
	}
	
	public static Vector<byte[]> scanRows(String tableName, String columnFamily, String columnName) throws IOException {
		
		Vector<byte[]> ret = new Vector<byte[]>();
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH

//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
	    
		// This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);
	    
		// Sometimes, you won't know the row you're looking for. In this case, you
	    // use a Scanner. This will give you cursor-like interface to the contents
	    // of the table.  To set up a Scanner, do like you did above making a Put
	    // and a Get, create a Scan.  Adorn it with column names, etc.
	    Scan s = new Scan();
	    
	    if(columnFamily != null) {
	    	if(columnName != null) {
	    		s.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));
	    	}
	    	else {
	    		s.addFamily(Bytes.toBytes(columnFamily));
	    	}
	    }
	    
	    ResultScanner scanner = table.getScanner(s);
	    try {
	      // Scanners return Result instances.
	      // Now, for the actual iteration. One way is to use a while loop like so:
	      for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
	        // print out the row we found and the columns we were looking for
	        System.out.println("Found row: " + rr);
	        ret.add(rr.getRow());
	      }

	      // The other approach is to use a foreach loop. Scanners are iterable!
	      // for (Result rr : scanner) {
	      //   System.out.println("Found row: " + rr);
	      // }
	    } finally {
	      // Make sure you close your scanners when you are done!
	      // Thats why we have it inside a try/finally clause
	      scanner.close();
	    }
		
		return ret;
	}

	public static Vector<byte[]> filterRows(String tableName, byte[] columnFamily, byte[] columnName, byte[] value, CompareOp compareOp) throws IOException {
		
		Vector<byte[]> ret = new Vector<byte[]>();
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH

//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
	    
		// This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);
	    
		// Sometimes, you won't know the row you're looking for. In this case, you
	    // use a Scanner. This will give you cursor-like interface to the contents
	    // of the table.  To set up a Scanner, do like you did above making a Put
	    // and a Get, create a Scan.  Adorn it with column names, etc.
	    Scan s = new Scan();
	    SingleColumnValueFilter filter = new SingleColumnValueFilter(columnFamily, columnName, compareOp, value);
	    s.setFilter(filter);
	    
	    
	    ResultScanner scanner = table.getScanner(s);
	    try {
	      // Scanners return Result instances.
	      // Now, for the actual iteration. One way is to use a while loop like so:
	      for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
	        // print out the row we found and the columns we were looking for
//	        System.out.println("Found row: " + rr);
	        ret.add(rr.getRow());
	      }
	
	      // The other approach is to use a foreach loop. Scanners are iterable!
	      // for (Result rr : scanner) {
	      //   System.out.println("Found row: " + rr);
	      // }
	    } finally {
	      // Make sure you close your scanners when you are done!
	      // Thats why we have it inside a try/finally clause
	      scanner.close();
	    }
		
		return ret;
	}
	
	public static Vector<byte[]> getColumnValuesFromFilteredRows(String tableName, byte[] columnFamilyFilter, byte[] columnNameFilter, byte[] value, CompareOp compareOp, byte[] columnFamilyValue, byte[] columnNameValue) throws IOException {
		
		Vector<byte[]> ret = new Vector<byte[]>();
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH

//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
	    
		// This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);
	    
		// Sometimes, you won't know the row you're looking for. In this case, you
	    // use a Scanner. This will give you cursor-like interface to the contents
	    // of the table.  To set up a Scanner, do like you did above making a Put
	    // and a Get, create a Scan.  Adorn it with column names, etc.
	    Scan s = new Scan();
	    SingleColumnValueFilter filter = new SingleColumnValueFilter(columnFamilyFilter, columnNameFilter, compareOp, value);
	    s.setFilter(filter);
	    
	    
	    ResultScanner scanner = table.getScanner(s);
	    try {
	      // Scanners return Result instances.
	      // Now, for the actual iteration. One way is to use a while loop like so:
	      for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
	        // print out the row we found and the columns we were looking for
//	        System.out.println("Found row: " + rr);
	        ret.add(rr.getValue(columnFamilyValue, columnNameValue));
	      }
	
	      // The other approach is to use a foreach loop. Scanners are iterable!
	      // for (Result rr : scanner) {
	      //   System.out.println("Found row: " + rr);
	      // }
	    } finally {
	      // Make sure you close your scanners when you are done!
	      // Thats why we have it inside a try/finally clause
	      scanner.close();
	    }
		
		return ret;
	}
	
	public static Vector<String> filterRows(String tableName, String columnFamily, String columnName, String value) throws IOException {
		
		Vector<String> ret = new Vector<String>();
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH

//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
	    
		// This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);
	    
		// Sometimes, you won't know the row you're looking for. In this case, you
	    // use a Scanner. This will give you cursor-like interface to the contents
	    // of the table.  To set up a Scanner, do like you did above making a Put
	    // and a Get, create a Scan.  Adorn it with column names, etc.
	    Scan s = new Scan();
	    SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(value));
	    s.setFilter(filter);
	    
	    
	    ResultScanner scanner = table.getScanner(s);
	    try {
	      // Scanners return Result instances.
	      // Now, for the actual iteration. One way is to use a while loop like so:
	      for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
	        // print out the row we found and the columns we were looking for
	        System.out.println("Found row: " + rr);
	        ret.add(rr.toString());
	      }

	      // The other approach is to use a foreach loop. Scanners are iterable!
	      // for (Result rr : scanner) {
	      //   System.out.println("Found row: " + rr);
	      // }
	    } finally {
	      // Make sure you close your scanners when you are done!
	      // Thats why we have it inside a try/finally clause
	      scanner.close();
	    }
		
		return ret;
	}
	
	
	
	public static void createTable(String tableName, Vector<String> columnFamilies) throws IOException {
		
//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
		
		HTableDescriptor desc = new HTableDescriptor(Bytes.toBytes(tableName));
		HBaseAdmin admin = new HBaseAdmin(config);
		
		for(String family: columnFamilies) {
			desc.addFamily(new HColumnDescriptor(Bytes.toBytes(family)));
		}
		
		admin.createTable(desc);
	}
	
	public static void deleteTable(String tableName) throws IOException {
		
//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
		
		HBaseAdmin admin = new HBaseAdmin(config);
		
		admin.disableTable(tableName);
		admin.deleteTable(tableName);
	}
	
	public static boolean tableExists(String tableName) throws IOException {
		
//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
		
		HBaseAdmin admin = new HBaseAdmin(config);
		
		boolean ret = admin.tableExists(tableName);
		return ret;
	}
	
	public static byte[][] getEndingKeys(String tableName) throws IOException {
		
//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
		
	    HTable table = new HTable(config, tableName);
	    
	    return table.getEndKeys();
	}
	
	public static void deleteRow(String tableName, byte[] rowId) throws IOException {
		
//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
		
	    HTable table = new HTable(config, tableName);
		Delete delete = new Delete(rowId);
		
		table.delete(delete);
	}
	
	public static void deleteFamily(String tableName, byte[] rowId, String columnFamily) throws IOException {
		
//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
		
	    HTable table = new HTable(config, tableName);
		Delete delete = new Delete(rowId);
		delete = delete.deleteFamily(Bytes.toBytes(columnFamily));
		table.delete(delete);
	}
	
	public static boolean rowExists(String tableName, byte[] rowId) throws IOException {

//	    HBaseConfiguration config = new HBaseConfiguration();
//		Configuration config = HBaseConfiguration.create();
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
		
	    HTable table = new HTable(config, tableName);
	    Get g = new Get(rowId);
	    
	    return table.exists(g);
	}
	
	public static Configuration getConfiguration() {
		
		if(config == null) {
			config = HBaseConfiguration.create();
		}
		
		return config;
	}
}