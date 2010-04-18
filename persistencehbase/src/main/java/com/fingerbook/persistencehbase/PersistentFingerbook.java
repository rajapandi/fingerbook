package com.fingerbook.persistencehbase;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Vector;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.fingerbook.models.FileInfo;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.persistencehbase.hbase.HbaseManager;

public class PersistentFingerbook extends Fingerbook{
	
	public static String FINGER_TABLE_NAME = "tfinger";
	public static String GROUP_TABLE_NAME = "tgroup";
	
	public static String TFINGER_GROUP_FAMILY = "group";
	public static String TGROUP_FINGER_FAMILY = "finger";
	public static String TGROUP_INFO_FAMILY = "info";

	public PersistentFingerbook(Fingerbook fingerBook) {
		this.fingerbookId = fingerBook.getFingerbookId();
		this.fingerPrints = fingerBook.getFingerPrints();
		this.userInfo = fingerBook.getUserInfo();
		this.stamp = fingerBook.getStamp();
	}

	public long saveMe() {
		
		byte[] groupIdB = Bytes.toBytes(fingerbookId);
		byte[] stampB = Bytes.toBytes(stamp);
		
		List<FileInfo> files = fingerPrints.getFiles();
		for(FileInfo fileInfo: files) {
			
			String shaHash = fileInfo.getShaHash();
			byte[] shaHashB = Bytes.toBytes(shaHash);
			
			try {
				HbaseManager.putValue(FINGER_TABLE_NAME, shaHashB, TFINGER_GROUP_FAMILY, groupIdB, stampB);
				HbaseManager.putValue(GROUP_TABLE_NAME, groupIdB, TGROUP_FINGER_FAMILY, shaHashB, Bytes.toBytes(""));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fingerbookId;
	}
	
	public Fingerbook loadMe() {
		
		Fingerbook fingerBook = new Fingerbook();
		fingerBook.setFingerbookId(fingerbookId);
		
		Fingerprints auxFingerPrints = new Fingerprints();
		List<FileInfo> auxFiles = new ArrayList<FileInfo>();
		
		
		NavigableMap<byte[],byte[]> familyMap = null;
		try {
			familyMap = HbaseManager.getMembersMap(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_FINGER_FAMILY);
			for(byte[] shaHashB: familyMap.keySet()) {
				
				String shaHash = Bytes.toString(shaHashB); 
				
				FileInfo auxFileInfo = new FileInfo();
				auxFileInfo.setShaHash(shaHash);
				auxFiles.add(auxFileInfo);
			}
			
			auxFingerPrints.setFiles(auxFiles);
			fingerBook.setFingerPrints(auxFingerPrints);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fingerBook;
	}
	
	public static Vector<Fingerbook> getFingerbookByHash(String hash) {
		
		Vector<Fingerbook> fingerbooks = new Vector<Fingerbook>();
		NavigableMap<byte[],byte[]> familyMap = null;
		try {
			familyMap = HbaseManager.getMembersMap(FINGER_TABLE_NAME, Bytes.toBytes(hash), TFINGER_GROUP_FAMILY);
			for(byte[] fingerbookIdB: familyMap.keySet()) {
				
				long fingerbookId = Bytes.toInt(fingerbookIdB);
				long stamp = Bytes.toLong(familyMap.get(fingerbookIdB));
				
				Fingerbook auxFingerbook = new Fingerbook();
				auxFingerbook.setFingerbookId(fingerbookId);
				auxFingerbook.setStamp(stamp);
				
				fingerbooks.add(auxFingerbook);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fingerbooks;
	}
	
	public static void createFingerTables() throws IOException {
		
		Vector<String> families = new Vector<String>();
		families.add(TFINGER_GROUP_FAMILY);
		
		HbaseManager.createTable(FINGER_TABLE_NAME, families);
		
		families = new Vector<String>();
		families.add(TGROUP_FINGER_FAMILY);
		families.add(TGROUP_INFO_FAMILY);
		
		HbaseManager.createTable(GROUP_TABLE_NAME, families);
	}
	
	
	public static long getNextGroupId() throws IOException {
		
		long maxGroupId = 0;
		HBaseConfiguration config = new HBaseConfiguration();
	    HTable table = new HTable(config, GROUP_TABLE_NAME);
	    
	    byte[] rowB = Bytes.toBytes(Long.MAX_VALUE);
	    byte[] familyB = Bytes.toBytes(TGROUP_FINGER_FAMILY);
	    
		Result result = table.getRowOrBefore(rowB, familyB);
		
		maxGroupId = Bytes.toLong(result.getRow());
		
		return maxGroupId + 1;
	}
}