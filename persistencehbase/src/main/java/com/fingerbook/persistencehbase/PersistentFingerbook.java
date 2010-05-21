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
import com.fingerbook.models.UserInfo;
import com.fingerbook.persistencehbase.hbase.HbaseManager;
import com.fingerbook.persistencehbase.hbase.TransHTable;

public class PersistentFingerbook extends Fingerbook{
	private static final long serialVersionUID = -2963312837954174296L;

	public static int separator = 30;
	
	public static String FINGER_TABLE_NAME = "tfinger";
	public static String GROUP_TABLE_NAME = "tgroup";
	
	public static String TFINGER_GROUP_FAMILY = "group_info";
	public static String TFINGER_FILE_FAMILY = "file_info";
	public static String TGROUP_FINGER_FAMILY = "finger";
	public static String TGROUP_INFO_FAMILY = "info";
	
	public static String COLUMN_STAMP = "stamp";
	public static String COLUMN_USER = "user";
	
	private TransHTable groupTable;
	private TransHTable fingerTable;
//	private RowLock groupRowLock;

	public PersistentFingerbook(Fingerbook fingerBook) {
		this.fingerbookId = fingerBook.getFingerbookId();
		this.fingerPrints = fingerBook.getFingerPrints();
		this.userInfo = fingerBook.getUserInfo();
		this.stamp = fingerBook.getStamp();
	}

	public long saveMeO() {
		
		try {
			this.fingerbookId = getNextGroupId();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
		byte[] groupIdB = Bytes.toBytes(fingerbookId);
		byte[] stampB = Bytes.toBytes(stamp);
		byte[] userB = null;
		
		try {
			/* Insert stamp in group table */
			HbaseManager.putValue(GROUP_TABLE_NAME, groupIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_STAMP), stampB);
			if(this.userInfo != null && this.userInfo.getUser() != null) {
				userB = Bytes.toBytes(this.userInfo.getUser());
				HbaseManager.putValue(GROUP_TABLE_NAME, groupIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_USER), userB);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
		
		saveFingerprintsO(fingerbookId, fingerPrints);
		return fingerbookId;
	}
	
	public static void saveFingerprintsO(long fingerbookId, Fingerprints fingerprints) {
		
		byte[] groupIdB = Bytes.toBytes(fingerbookId);
		List<FileInfo> files = fingerprints.getFiles();
		for(FileInfo fileInfo: files) {
			
			String shaHash = fileInfo.getShaHash();
			String fileName = fileInfo.getName();
			byte[] shaHashB = Bytes.toBytes(shaHash);
			
			byte[] groupFileNameCol = createGroupFileNameCol(fingerbookId, fileName);
			byte[] hashFileNameCol = createHashFileNameCol(shaHash, fileName);
			
			byte[] sizeInBytesB = Bytes.toBytes(fileInfo.getSizeInBytes());
			
			try {
				/* Insert groupId and stamp in finger table */
				HbaseManager.putValue(FINGER_TABLE_NAME, shaHashB, TFINGER_FILE_FAMILY, groupFileNameCol, sizeInBytesB);
				/* Insert hash in group table */
				HbaseManager.putValue(GROUP_TABLE_NAME, groupIdB, TGROUP_FINGER_FAMILY, hashFileNameCol, sizeInBytesB);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//				return -1;
			}
		}
	}
	
	public long saveMe() {
		
		try {
			this.groupTable = new TransHTable(GROUP_TABLE_NAME);
			this.fingerTable = new TransHTable(FINGER_TABLE_NAME);
			this.fingerbookId = insertGroupInfo(this);
		
			if(this.fingerbookId < 0) {
				return -1;
			}
			
//			this.groupRowLock = groupTable.lockRow(groupIdB);
			
			if(this.fingerPrints != null) {
				this.saveFingerprints(this.fingerPrints);
			}
			this.commitSave();
			
			return this.fingerbookId;
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
	}
	
	public void saveFingerprints(Fingerprints fingerprints) {
		
		if(fingerprints == null) {
			return;
		}
		try {
			byte[] groupIdB = Bytes.toBytes(this.fingerbookId);
			List<FileInfo> files = fingerprints.getFiles();
			
			if(files == null) {
				return;
			}
			for(FileInfo fileInfo: files) {
			
				String shaHash = fileInfo.getShaHash();
				String fileName = fileInfo.getName();
				//byte[] shaHashB = Bytes.toBytes(shaHash);
				
				//byte[] groupFileNameCol = createGroupFileNameCol(fingerbookId, fileName);
				byte[] hashFileNameCol = createHashFileNameCol(shaHash, fileName);
				
				byte[] sizeInBytesB = Bytes.toBytes(fileInfo.getSizeInBytes());
				
				/* Insert groupId, fileName and size in finger table */
//				fingerTable.put(shaHashB, Bytes.toBytes(TFINGER_FILE_FAMILY), groupFileNameCol, sizeInBytesB);
				
				/* Insert hash, fileName and size in group table */
				groupTable.put(groupIdB, Bytes.toBytes(TGROUP_FINGER_FAMILY), hashFileNameCol, sizeInBytesB);
			}
			groupTable.commit();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//				return -1;
		}
	}
	
	public void commitSave() {
		this.updateFingerTable();
		this.commitAndDestroy();
	}
	
	public int rollBackSave() {
		byte[] groupIdB = Bytes.toBytes(this.fingerbookId);
		
		try {
			this.groupTable.deleteRow(groupIdB);
			this.commitAndDestroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	
	private void updateFingerTable() {
		
		Fingerprints auxFingerprints = null;
		
		auxFingerprints = loadFingerPrintsByFingerBook(this.fingerbookId);
		if(auxFingerprints == null) {
			return;
		}
		try {
//			byte[] groupIdB = Bytes.toBytes(this.fingerbookId);
			List<FileInfo> files = auxFingerprints.getFiles();
			
			if(files == null) {
				return;
			}
			for(FileInfo fileInfo: files) {
			
				String shaHash = fileInfo.getShaHash();
				String fileName = fileInfo.getName();
				byte[] shaHashB = Bytes.toBytes(shaHash);
				
				byte[] groupFileNameCol = createGroupFileNameCol(fingerbookId, fileName);
//				byte[] hashFileNameCol = createHashFileNameCol(shaHash, fileName);
				
				byte[] sizeInBytesB = Bytes.toBytes(fileInfo.getSizeInBytes());
				
				/* Insert groupId, fileName and size in finger table */
				fingerTable.put(shaHashB, Bytes.toBytes(TFINGER_FILE_FAMILY), groupFileNameCol, sizeInBytesB);
				fingerTable.commit();
				/* Insert hash, fileName and size in group table */
//				groupTable.put(groupIdB, Bytes.toBytes(TGROUP_FINGER_FAMILY), hashFileNameCol, sizeInBytesB);
//				groupTable.commit();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//				return -1;
		}
	}
	
	public void commitAndDestroy() {
		try {
			groupTable.commitAndDestroy();
			fingerTable.commitAndDestroy();
			
			fingerTable = null;
			groupTable = null;
//			groupRowLock = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Fingerbook loadMe() {
		
		Fingerbook fingerBook = new Fingerbook();
		fingerBook.setFingerbookId(fingerbookId);
		
		Fingerprints auxFingerPrints = new Fingerprints();
		List<FileInfo> auxFiles = new ArrayList<FileInfo>();
		byte[] fingerbookIdB = Bytes.toBytes(fingerbookId);
		
		NavigableMap<byte[],byte[]> familyMap = null;
		try {
			byte[] stampB = HbaseManager.getValue(GROUP_TABLE_NAME, fingerbookIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_STAMP));
			fingerBook.setStamp(Bytes.toLong(stampB));
			
			byte[] userB = HbaseManager.getValue(GROUP_TABLE_NAME, fingerbookIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_USER));
			UserInfo userInfo = new UserInfo();
			String user = "";
			if(userB != null) {
				user = Bytes.toString(userB);
			}
			userInfo.setUser(user);
			fingerBook.setUserInfo(userInfo);
			
			familyMap = HbaseManager.getMembersMap(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_FINGER_FAMILY);
			System.out.println("FingerbookId: " + fingerbookId);
			for(byte[] hashFNCol: familyMap.keySet()) {
				
//				String shaHash = Bytes.toString(shaHashB); 
				String shaHash = getHashFromHashFNCol(hashFNCol);
				String fileName = getFileNameFromHashFNCol(hashFNCol);
				long sizeInBytes = Bytes.toLong(familyMap.get(hashFNCol));
				
				FileInfo auxFileInfo = new FileInfo();
				auxFileInfo.setShaHash(shaHash);
				auxFileInfo.setName(fileName);
				auxFileInfo.setSizeInBytes(sizeInBytes);
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
	
	public static Fingerprints loadFingerPrintsByFingerBook(long fingerbookId) {
		
		Fingerprints auxFingerPrints = new Fingerprints();
		List<FileInfo> auxFiles = new ArrayList<FileInfo>();
		
		NavigableMap<byte[],byte[]> familyMap = null;
		try {
			familyMap = HbaseManager.getMembersMap(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_FINGER_FAMILY);
			if(familyMap != null) {
				for(byte[] hashFNCol: familyMap.keySet()) {
					
//					String shaHash = Bytes.toString(shaHashB); 
					String shaHash = getHashFromHashFNCol(hashFNCol);
					String fileName = getFileNameFromHashFNCol(hashFNCol);
					long sizeInBytes = Bytes.toLong(familyMap.get(hashFNCol));
					
					FileInfo auxFileInfo = new FileInfo();
					auxFileInfo.setShaHash(shaHash);
					auxFileInfo.setName(fileName);
					auxFileInfo.setSizeInBytes(sizeInBytes);
					auxFiles.add(auxFileInfo);
				}
			}
			auxFingerPrints.setFiles(auxFiles);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return auxFingerPrints;
	}
	
	public static Vector<Fingerbook> getFingerbookByHash(String hash) {
		
		Vector<Fingerbook> fingerbooks = new Vector<Fingerbook>();
		NavigableMap<byte[],byte[]> familyMap = null;
		try {
			familyMap = HbaseManager.getMembersMap(FINGER_TABLE_NAME, Bytes.toBytes(hash), TFINGER_FILE_FAMILY);
			if(familyMap == null) {
				return fingerbooks;
			}
			
			Vector<Long> loaded = new Vector<Long>();
			for(byte[] groupFNCol: familyMap.keySet()) {
				
//				long fingerbookId = Bytes.toLong(fingerbookIdB);
//				long stamp = Bytes.toLong(familyMap.get(fingerbookIdB));
//				String fileName = getFileNameFromGroupFNCol(groupFNCol);
				
				long fingerbookId = getGroupIdFromCol(groupFNCol);
				if(!loaded.contains(fingerbookId)) {
//					long stamp = Bytes.toLong(familyMap.get(groupFNCol));
					
					Fingerbook auxFingerbook = new Fingerbook();
					auxFingerbook.setFingerbookId(fingerbookId);
//					auxFingerbook.setStamp(stamp);
					
					PersistentFingerbook persFingerbook = new PersistentFingerbook(auxFingerbook);
					auxFingerbook = persFingerbook.loadMe();
					
					Fingerprints auxFingerPrints = loadFingerPrintsByFingerBook(fingerbookId);
					auxFingerbook.setFingerPrints(auxFingerPrints);
					
					fingerbooks.add(auxFingerbook);
					loaded.add(fingerbookId);
				}
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
		families.add(TFINGER_FILE_FAMILY);
		
		HbaseManager.createTable(FINGER_TABLE_NAME, families);
		
		families = new Vector<String>();
		families.add(TGROUP_FINGER_FAMILY);
		families.add(TGROUP_INFO_FAMILY);
		
		HbaseManager.createTable(GROUP_TABLE_NAME, families);
	}
	
	private static synchronized long insertGroupInfo(Fingerbook fingerbook) {
		
		try {
			long auxFingerbookId = getNextGroupId();
			if(auxFingerbookId < 0) {
				return -1;
			}
			byte[] groupIdB = Bytes.toBytes(auxFingerbookId);
			byte[] stampB = Bytes.toBytes(fingerbook.getStamp());
			byte[] userB = null;
			UserInfo userInfo = fingerbook.getUserInfo();
		
			/* Insert stamp in group table */
			HbaseManager.putValue(GROUP_TABLE_NAME, groupIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_STAMP), stampB);
			if(userInfo != null && userInfo.getUser() != null) {
				userB = Bytes.toBytes(userInfo.getUser());
				HbaseManager.putValue(GROUP_TABLE_NAME, groupIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_USER), userB);
			}
			
			return auxFingerbookId;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
	}
	
	private static synchronized long getNextGroupId() throws IOException {
		
		long maxGroupId = 0;
		HBaseConfiguration config = new HBaseConfiguration();
	    HTable table = new HTable(config, GROUP_TABLE_NAME);
	    
	    byte[] rowB = Bytes.toBytes(Long.MAX_VALUE);
	    byte[] familyB = Bytes.toBytes(TGROUP_INFO_FAMILY);
	    
		Result result = table.getRowOrBefore(rowB, familyB);
		
		if(result != null) {
			maxGroupId = Bytes.toLong(result.getRow());			
		}
		
		
		return maxGroupId + 1;
	}
	
	public static byte[] createGroupFileNameCol(long groupId, String fileName) {
		
		byte[] column = null;
		
		byte[] groupIdB = Bytes.toBytes(groupId);
		byte[] fileNameB = Bytes.toBytes(fileName);
		column = new byte[groupIdB.length + fileNameB.length];
		
		System.arraycopy(groupIdB, 0, column, 0, groupIdB.length);
	    System.arraycopy(fileNameB, 0, column, groupIdB.length, fileNameB.length);
		
//		String auxCol = String.valueOf(groupId) + fileName;
//		column = Bytes.toBytes(auxCol);
		
		return column;
	}
	
	public static long getGroupIdFromCol(byte[] column) {
		
		long groupId = -1;
		
		groupId = Bytes.toLong(column, 0, 8);
		
		return groupId;
	}
	
	public static String getFileNameFromGroupFNCol(byte[] column) {
		
		String fileName = null;
		
		fileName = Bytes.toString(column, 8, column.length);
		
		return fileName;
	}
	
	public static byte[] createHashFileNameCol(String hash, String fileName) {
		
		byte[] column = null;
		
		String auxCol = hash + String.valueOf((char) separator) + fileName;
		column = Bytes.toBytes(auxCol);
		
		return column;
	}
	
	public static String getFileNameFromHashFNCol(byte[] column) {
		
		String fileName = null;
		
		String aux = Bytes.toString(column);
		String[] auxArr = aux.split(String.valueOf((char) separator));
		
		fileName = auxArr[1];
		
		return fileName;
	}
	
	public static String getHashFromHashFNCol(byte[] column) {
		
		String hash = null;
		
		String aux = Bytes.toString(column);
		String[] auxArr = aux.split(String.valueOf((char) separator));
		
		hash = auxArr[0];
		
		return hash;
	}
}