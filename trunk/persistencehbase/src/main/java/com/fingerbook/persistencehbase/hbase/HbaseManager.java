package com.fingerbook.persistencehbase.hbase;

import java.io.IOException;
import java.util.NavigableMap;
import java.util.Vector;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseManager {

	public static void putValue(String tableName, byte[] rowId, String columnFamily, String columnName, byte[] value) throws IOException {
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH
	    HBaseConfiguration config = new HBaseConfiguration();

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
	    HBaseConfiguration config = new HBaseConfiguration();

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
	    HBaseConfiguration config = new HBaseConfiguration();

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
	
	public static String getValue(String tableName, String rowId, String columnFamily, String columnName) throws IOException {
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH
	    HBaseConfiguration config = new HBaseConfiguration();
	    
		// This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);
	    
		// Now, to retrieve the data we just wrote. The values that come back are
	    // Result instances. Generally, a Result is an object that will package up
	    // the hbase return into the form you find most palatable.
	    Get g = new Get(Bytes.toBytes(rowId));
	    Result r = table.get(g);
	    byte [] value = r.getValue(Bytes.toBytes(columnFamily),
	      Bytes.toBytes(columnName));
	    // If we convert the value bytes, we should get back 'Some Value', the
	    // value we inserted at this location.
	    String valueStr = Bytes.toString(value);
	    System.out.println("GET: " + valueStr);
		
		return valueStr;
	}
	
	public static NavigableMap<byte[],byte[]> getMembersMap(String tableName, byte[] rowId, String columnFamily) throws IOException {
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH
	    HBaseConfiguration config = new HBaseConfiguration();
	    
		// This instantiates an HTable object that connects you to
	    // the "myLittleHBaseTable" table.
	    HTable table = new HTable(config, tableName);
	    
		// Now, to retrieve the data we just wrote. The values that come back are
	    // Result instances. Generally, a Result is an object that will package up
	    // the hbase return into the form you find most palatable.
	    Get g = new Get(rowId);
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
	    HBaseConfiguration config = new HBaseConfiguration();
	    
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
	    HBaseConfiguration config = new HBaseConfiguration();
	    
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
	
	public static Vector<String> filterRows(String tableName, String columnFamily, String columnName, String value) throws IOException {
		
		Vector<String> ret = new Vector<String>();
		
		// You need a configuration object to tell the client where to connect.
	    // When you create a HBaseConfiguration, it reads in whatever you've set
	    // into your hbase-site.xml and in hbase-default.xml, as long as these can
	    // be found on the CLASSPATH
	    HBaseConfiguration config = new HBaseConfiguration();
	    
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
		
		HBaseConfiguration config = new HBaseConfiguration();
		HTableDescriptor desc = new HTableDescriptor(Bytes.toBytes(tableName));
		HBaseAdmin admin = new HBaseAdmin(config);
		
		for(String family: columnFamilies) {
			desc.addFamily(new HColumnDescriptor(Bytes.toBytes(family)));
		}
		
		admin.createTable(desc);
	}
	
	public static byte[][] getEndingKeys(String tableName) throws IOException {
		
		HBaseConfiguration config = new HBaseConfiguration();
	    HTable table = new HTable(config, tableName);
	    
	    return table.getEndKeys();
	}
}