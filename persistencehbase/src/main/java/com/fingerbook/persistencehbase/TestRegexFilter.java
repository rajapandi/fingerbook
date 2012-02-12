package com.fingerbook.persistencehbase;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Vector;

import org.apache.hadoop.hbase.util.Bytes;

import com.fingerbook.persistencehbase.hbase.HbaseManager;

public class TestRegexFilter {
	
	public static String BASE_REGEX = "(1\\-.)|[^\\-]+(\\-1)";
	public static String TSIMILHASH_TABLE_NAME = "tsimilhash";
	
	public static String TSIMILHASH_GROUP_FAMILY = "group_fid";
	public static String TSIMILHASH_FINGER_FAMILY = "finger";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Vector<NavigableMap<byte[],NavigableMap<byte[],byte[]>>> filteredMapsVec = null;
		NavigableMap<byte[],byte[]> familyMapGroup = null;
		NavigableMap<byte[],byte[]> familyMapFinger = null;
		
		String regex = BASE_REGEX;
		
		
		try {
			filteredMapsVec = HbaseManager.getFullRowMapFilteredByKeyRegex(TSIMILHASH_TABLE_NAME, regex);
			
			if(filteredMapsVec != null) {
				
				
				for(NavigableMap<byte[],NavigableMap<byte[],byte[]>> filteredMap: filteredMapsVec) {
					
					if(filteredMap != null) {
						
						familyMapGroup = filteredMap.get(Bytes.toBytes(TSIMILHASH_GROUP_FAMILY));
						familyMapFinger = filteredMap.get(Bytes.toBytes(TSIMILHASH_FINGER_FAMILY));
						
						if(familyMapGroup != null) {
							
							System.out.println("START FAMILY_GROUP");
							
							for(Entry<byte[], byte[]> entryGroup: familyMapGroup.entrySet()) {
								
								String colName = Bytes.toString(entryGroup.getKey());
								
//								Long valueL = Bytes.toLong(entryGroup.getValue());
//								String value = valueL.toString();
								String value = Bytes.toString(entryGroup.getValue());
								
								System.out.println("COLNAME: " + colName);
								System.out.println("VALUE: " + value);
							}
							
							System.out.println("END FAMILY_GROUP");
						}
						
						if(familyMapFinger != null) {
							
							System.out.println("START FAMILY_FINGER");
							
							for(Entry<byte[], byte[]> entryFinger: familyMapFinger.entrySet()) {
								
								String finger = Bytes.toString(entryFinger.getKey());
								
								System.out.println("FINGER: " + finger);
								
							}
							
							System.out.println("END FAMILY_FINGER");
						}
					}
					
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("END");

	}

}
