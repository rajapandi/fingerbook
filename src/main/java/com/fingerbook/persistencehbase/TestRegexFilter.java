package com.fingerbook.persistencehbase;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Vector;

import org.apache.hadoop.hbase.util.Bytes;

import com.fingerbook.persistencehbase.hbase.HbaseManager;

public class TestRegexFilter {
	
//	public static String BASE_REGEX = "(1\\-.)|[^\\-]+(\\-1)";
//	public static String BASE_REGEX = "(^1\\-.)";
	public static String BASE_REGEX = "(^id\\-.)";
	
	public static Long FB_ID = 1L;
	
//	public static String TSIMILHASH_TABLE_NAME = "tsimilhash";
	public static String TSIMILHASH_TABLE_NAME = "tcomposite";
	public static String TSIMILHASH_GROUP_FAMILY = "group_fid";
	public static String TSIMILHASH_FINGER_FAMILY = "finger";
	public static String TSIMILHASH_TAG_FAMILY = "tag";
	public static String TSIMILHASH_INFO_FAMILY = "info";
	public static String TSIMILHASH_COLUMN_TOTAL = "total";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		getOtherFbIds();
		getRowKeys();
//		getFullRowsData();

	}
	
	public static String buildRegex(Long compId) {
		
		String compIdStr = compId.toString();
		
		String regex = BASE_REGEX.replaceFirst("id", compIdStr);
		
		return regex;
	}
	
	public static void getRowKeys() {
		
		Vector<byte[]> rowKeys = null;
		
//		String regex = BASE_REGEX;
		String regex = buildRegex(FB_ID);
		
		try {
						
			rowKeys = HbaseManager.getRowKeysFilteredByKeyRegex(TSIMILHASH_TABLE_NAME, regex);
			
			if(rowKeys != null) {
				
				System.out.println("START ROW_KEYS");
				
				for(byte[] auxRowKey: rowKeys) {
					
					String rowKeyStr = Bytes.toString(auxRowKey);
					
					System.out.println("ROW_KEY: " + rowKeyStr);
				}
				
				System.out.println("END ROW_KEYS");
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("END");

	}
	
	
	public static void getOtherFbIds() {
		
		Vector<byte[]> fbIds = null;
		
//		String regex = BASE_REGEX;
		String regex = buildRegex(FB_ID);
		
		String fbIdStr = FB_ID.toString();
		
		try {
//			filteredMapsVec = HbaseManager.getFullRowMapFilteredByKeyRegex(TSIMILHASH_TABLE_NAME, regex);
			
			fbIds = HbaseManager.getValuesFilteredByKeyRegex(TSIMILHASH_TABLE_NAME, regex, TSIMILHASH_GROUP_FAMILY, Bytes.toBytes(fbIdStr));
			
			if(fbIds != null) {
				
				System.out.println("START FB_ID2");
				
				for(byte[] auxFbId: fbIds) {
					
					String fbId2Str = Bytes.toString(auxFbId);
					Long fbId2 = Long.valueOf(fbId2Str);
					
					System.out.println("FBID2: " + fbId2);
				}
				
				System.out.println("END FB_ID2");
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("END");

	}
	
	
	public static void getFullRowsData() {
		
		Vector<NavigableMap<byte[],NavigableMap<byte[],byte[]>>> filteredMapsVec = null;
		NavigableMap<byte[],byte[]> familyMapGroup = null;
		NavigableMap<byte[],byte[]> familyMapFinger = null;
		NavigableMap<byte[],byte[]> familyMapTag = null;
		NavigableMap<byte[],byte[]> familyMapInfo = null;
		
//		String regex = BASE_REGEX;
		String regex = buildRegex(FB_ID);
		
		Vector<String> columnFamilies = new Vector<String>();
		columnFamilies.add(TSIMILHASH_GROUP_FAMILY);
		columnFamilies.add(TSIMILHASH_INFO_FAMILY);
		columnFamilies.add(TSIMILHASH_FINGER_FAMILY);
		
		try {
//			filteredMapsVec = HbaseManager.getFullRowMapFilteredByKeyRegex(TSIMILHASH_TABLE_NAME, regex);
			filteredMapsVec = HbaseManager.getFullRowMapFilteredByKeyRegex(TSIMILHASH_TABLE_NAME, regex, columnFamilies);
			
			if(filteredMapsVec != null) {
				
				
				for(NavigableMap<byte[],NavigableMap<byte[],byte[]>> filteredMap: filteredMapsVec) {
					
					if(filteredMap != null) {
						
						familyMapGroup = filteredMap.get(Bytes.toBytes(TSIMILHASH_GROUP_FAMILY));
						familyMapFinger = filteredMap.get(Bytes.toBytes(TSIMILHASH_FINGER_FAMILY));
						familyMapTag = filteredMap.get(Bytes.toBytes(TSIMILHASH_TAG_FAMILY));
						familyMapInfo = filteredMap.get(Bytes.toBytes(TSIMILHASH_INFO_FAMILY));
						
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
						
						if(familyMapTag != null) {
							
							System.out.println("START FAMILY_TAG");
							
							for(Entry<byte[], byte[]> entryTag: familyMapTag.entrySet()) {
								
								String tag = Bytes.toString(entryTag.getKey());
								
								System.out.println("TAG: " + tag);
								
							}
							
							System.out.println("END FAMILY_TAG");
						}
						
						if(familyMapInfo != null) {
							
							System.out.println("START FAMILY_INFO");
							
							for(Entry<byte[], byte[]> entryInfo: familyMapInfo.entrySet()) {
								
								String totalKey = Bytes.toString(entryInfo.getKey());
								String totalValue = Bytes.toString(entryInfo.getValue());
								
								System.out.println("TOTAL_KEY: " + totalKey);
								System.out.println("TOTAL_VALUE: " + totalValue);
								
							}
							
							System.out.println("END FAMILY_INFO");
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
