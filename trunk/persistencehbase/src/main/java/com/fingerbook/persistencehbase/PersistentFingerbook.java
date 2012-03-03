package com.fingerbook.persistencehbase;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;

import com.fingerbook.models.Auth;
import com.fingerbook.models.FileInfo;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.models.UserInfo;
import com.fingerbook.models.transfer.FingerbookList;
import com.fingerbook.models.transfer.FingerprintsFeed;
import com.fingerbook.models.transfer.SimilaritiesFeed;
import com.fingerbook.persistencehbase.hbase.HbaseManager;
import com.fingerbook.persistencehbase.hbase.TransHTable;
import com.fingerbook.persistencehbase.svc.Sha1;
import com.fingerbook.persistencehbase.svc.ValueComparator;

//public class PersistentFingerbook extends Fingerbook{
public class PersistentFingerbook {
	public static final int FILE_INFO_PAGINATION_LIMIT = 2;
	
	private static String TICKET_KEY = "fingerbook";
	private static String TRANSACTION_ID_KEY = "fingerbook_trans_key";
	
	public static int separator = 30;
	
	public static String FINGER_TABLE_NAME = "tfinger";
	public static String TFINGER_GROUP_FAMILY = "group_info";
	public static String TFINGER_GROUP_STR_FAMILY = "group_str";
	public static String TFINGER_INFO_FAMILY = "general";
	public static String TFINGER_COLUMN_TOTAL = "total";
//	public static String TFINGER_FILE_SIZE_FAMILY = "file_size";
//	public static String TFINGER_FILE_NAME_FAMILY = "file_name";
//	public static String TFINGER_FILE_PATH_FAMILY = "file_path";
	
	public static String GROUP_TABLE_NAME = "tgroup";
	public static String TGROUP_FINGER_FAMILY = "finger";
//	public static String TGROUP_FINGER_FILE_NAME_FAMILY = "file_name";
//	public static String TGROUP_FINGER_FILE_SIZE_FAMILY = "file_size";
	public static String TGROUP_INFO_FAMILY = "info";
	public static String TGROUP_TAG_FAMILY = "tag";
	public static String COLUMN_STAMP = "stamp";
	public static String COLUMN_USER = "user";
	public static String COLUMN_TICKET = "ticket";
	public static String COLUMN_COMMENT = "comment";
	public static String TGROUP_COLUMN_TOTAL = "total";
	
	/* RowId = TransactionId */
	public static String TMP_TABLE_NAME = "ttmp";
	public static String TTMP_STATE_FAMILY = "state";
	public static String TTMP_INFO_FAMILY = "info";
	public static String TTMP_COLUMN_LAST_ACTION = "last_action";
	public static String TTMP_COLUMN_GROUP_ID = "group_id";
	public static String TTMP_COLUMN_USER = "user";
	public static String TTMP_COLUMN_TICKET = "ticket";
	
	public static String GENERAL_TABLE_NAME = "tgeneral";
	public static String TGENERAL_INFO_FAMILY = "general";
	public static String COLUMN_LAST_GROUP = "last_group";
	public static long TGENERAL_ROW_ID = 0L;
	
	public static String TICKET_TABLE_NAME = "tticket";
	public static String TTICKET_GROUP_ID_FAMILY = "group_id";
	public static String TTICKET_INFO_FAMILY = "general";
	public static String TTICKET_COLUMN_TOTAL = "total";
	
	public static String USER_TABLE_NAME = "tuser";
	public static String TUSER_GROUP_ID_FAMILY = "group_id";
	public static String TUSER_INFO_FAMILY = "general";
	public static String TUSER_COLUMN_TOTAL = "total";
	
	public static String SIMIL_TABLE_NAME = "tsimilarities";
	public static String TSIMIL_GROUP_FID_FAMILY = "group_fid";

//	public PersistentFingerbook(Fingerbook fingerBook) {
//		this.fingerbookId = fingerBook.getFingerbookId();
//		this.fingerPrints = fingerBook.getFingerPrints();
//		this.userInfo = fingerBook.getUserInfo();
//		this.stamp = fingerBook.getStamp();
//		this.tags = fingerBook.getTags();
//		this.comment = fingerBook.getComment();
//	}
	
	public static long saveMe(Fingerbook fingerbook, int authMethod) {
		
		try {
			
			if(fingerbook == null) {
				return -2;
			}
			
//			this.fingerbookId = insertGroupInfo(fingerBook, authMethod);
			long fingerbookId = insertGroupInfo(fingerbook, authMethod);
			fingerbook.setFingerbookId(fingerbookId);
		
			if(fingerbookId < 0) {
				return -1;
			}
			
			createTmpState(fingerbook, authMethod);
			
			Fingerprints fingerPrints = fingerbook.getFingerPrints();
			
			if(fingerPrints != null) {
				saveFingerprints(fingerbook);
			}
			
			return fingerbookId;
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
	}
	
	public static long saveFingerprints(Fingerbook fingerbook) {
//		return saveFingerprints(fingerbook.getFingerbookId(), fingerbook.getFingerPrints());
		
		if(fingerbook == null) {
			return -1;
		}
		
		long fingerbookId = fingerbook.getFingerbookId();
		Fingerprints fingerprints = fingerbook.getFingerPrints();
		
		if(fingerprints == null) {
			return -2;
		}
		try {
			
			if(!fingerprintsSaveAllowed(fingerbook)) {
				return -3;
			}
			TransHTable groupTable = new TransHTable(GROUP_TABLE_NAME);
			byte[] groupIdB = Bytes.toBytes(fingerbookId);
			List<FileInfo> files = fingerprints.getFiles();
			
			if(files == null) {
				return -4;
			}
			for(FileInfo fileInfo: files) {
			
				String shaHash = fileInfo.getShaHash();
				byte[] shaHashB = Bytes.toBytes(shaHash);
				
				groupTable.incrementColumnValue(groupIdB, Bytes.toBytes(TGROUP_FINGER_FAMILY), shaHashB, 1L);
				
			}
			groupTable.commitAndDestroy();
			updateTmpState(fingerbookId);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
				return -5;
		}
		return fingerbookId;
	}
	
	
	public static long commitSave(Fingerbook fingerbook) {
//		return commitSave(fingerbook.getFingerbookId());
		
		try {
			if(!fingerprintsSaveAllowed(fingerbook)) {
				return -2;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		long fingerbookId = fingerbook.getFingerbookId();
		
		try {
			updateFingerbookTotalFiles(fingerbookId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -3;
		}
		updateFingerTable(fingerbookId);
		addFingerbookToUser(fingerbookId);
		deleteFingerbookFromTmp(fingerbookId);
//		this.commitAndDestroy();
		return fingerbookId;
	}
	
	private static void updateFingerbookTotalFiles(long fingerbookId) throws IOException {
		
		int total = 0;
		byte[] groupIdB = Bytes.toBytes(fingerbookId);
		
		total = HbaseManager.getMembersMapCount(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_FINGER_FAMILY);
		
		Integer auxTotal = new Integer(total);
		
		long totalL = auxTotal.longValue();
		
		HbaseManager.putValue(GROUP_TABLE_NAME, groupIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(TGROUP_COLUMN_TOTAL), Bytes.toBytes(totalL));
		
	}

//	public static long commitSave(long fingerbookId) {
//		
//		try {
//			if(!fingerprintsSaveAllowed(fingerbookId)) {
//				return -2;
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return -1;
//		}
//		updateFingerTable(fingerbookId);
//		deleteFingerbookFromTmp(fingerbookId);
////		this.commitAndDestroy();
//		return fingerbookId;
//	}
	
	public static int cleanExpired(long timeout) {
		
		Vector<Long> expiredIds;
		try {
			expiredIds = getExpiredFingerbooksIds(timeout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		for(Long id: expiredIds) {
			rollBackSave(id.longValue());
		}
		
		return 0;
	}
	
	public static int rollBackSave(long fingerbookId) {
		
		if(deleteFingerbookFromTGroup(fingerbookId) < 0) {
			return -1;
		}
		if(deleteFingerbookFromTmp(fingerbookId) < 0) {
			return -2;
		}
//			this.commitAndDestroy();
		
		return 0;
	}
	
	private static void addFingerbookToUser(long fingerbookId) {
		
		UserInfo userInfo = loadUserInfoByFingerbookId(fingerbookId);
		
		if(userInfo == null) {
			return;
		}
		if(userInfo.getUser() != null && !userInfo.getUser().isEmpty()) {
			updateUserTable(fingerbookId, userInfo.getUser());
		}
		else if(userInfo.getTicket() != null && !userInfo.getTicket().isEmpty()) {
			updateTicketTable(fingerbookId, userInfo.getTicket());
		}
	}
	
	private static void updateTicketTable(long fingerbookId, String ticket) {
		
		TransHTable ticketTable = null;
		try {
			if(ticket != null) {
				ticketTable = new TransHTable(TICKET_TABLE_NAME);
				//byte[] fingerbookIdB = Bytes.toBytes(fingerbookId);
				byte[] ticketB = Bytes.toBytes(ticket);
				
				//byte[] auxValueB = Bytes.toBytes("");
				
				/* Modifico para que se guarden ordenados de recientes a anteriores */
				long modFingerbookId = Long.MAX_VALUE - fingerbookId;
				byte[] modFingerbookIdB = Bytes.toBytes(modFingerbookId);
				ticketTable.put(ticketB, Bytes.toBytes(TTICKET_GROUP_ID_FAMILY), modFingerbookIdB, null);
//				ticketTable.put(ticketB, Bytes.toBytes(TTICKET_GROUP_ID_FAMILY), fingerbookIdB, auxValueB);
				
				ticketTable.incrementColumnValue(ticketB, Bytes.toBytes(TTICKET_INFO_FAMILY), Bytes.toBytes(TTICKET_COLUMN_TOTAL), 1L);
				
				ticketTable.commitAndDestroy();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void updateUserTable(long fingerbookId, String user) {
		
		TransHTable userTable = null;
		try {
			if(user != null) {
				userTable = new TransHTable(USER_TABLE_NAME);
				//byte[] fingerbookIdB = Bytes.toBytes(fingerbookId);
				byte[] userB = Bytes.toBytes(user);
				
//				userTable.put(userB, Bytes.toBytes(TUSER_GROUP_ID_FAMILY), fingerbookIdB, null);
				
				/* Modifico para que se guarden ordenados de recientes a anteriores */
				long modFingerbookId = Long.MAX_VALUE - fingerbookId;
				byte[] modFingerbookIdB = Bytes.toBytes(modFingerbookId);
				userTable.put(userB, Bytes.toBytes(TUSER_GROUP_ID_FAMILY), modFingerbookIdB, null);
				
				userTable.incrementColumnValue(userB, Bytes.toBytes(TUSER_INFO_FAMILY), Bytes.toBytes(TUSER_COLUMN_TOTAL), 1L);
				
				userTable.commitAndDestroy();
							
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void updateFingerTable(long fingerbookId) {
		
		Fingerprints auxFingerprints = null;
		
		auxFingerprints = loadFingerPrintsByFingerBook(fingerbookId);
		if(auxFingerprints == null) {
			return;
		}
		try {
			byte[] fingerbookIdB = Bytes.toBytes(fingerbookId);
			String fingerbookIdStr = String.valueOf(fingerbookId);
			TransHTable fingerTable = new TransHTable(FINGER_TABLE_NAME);
			List<FileInfo> files = auxFingerprints.getFiles();
			
			if(files == null) {
				return;
			}
			for(FileInfo fileInfo: files) {
			
				String shaHash = fileInfo.getShaHash();
//				String fileName = fileInfo.getName();
//				String path = fileInfo.getPath();
				
				byte[] shaHashB = Bytes.toBytes(shaHash);
//				byte[] pathB = Bytes.toBytes(path);
//				byte[] fileNameB = Bytes.toBytes(fileName);
//				byte[] sizeInBytesB = Bytes.toBytes(fileInfo.getSizeInBytes());
//				
////				byte[] groupFileNameCol = createGroupFileNameCol(fingerbookId, fileName);
//				byte[] groupPathCol = createGroupPathCol(fingerbookId, path);
				
				/* Insert groupId, fileName and size in finger table */
//				fingerTable.put(shaHashB, Bytes.toBytes(TFINGER_FILE_FAMILY), groupFileNameCol, sizeInBytesB);
				
				/* Insert groupId, path, fileName and size in finger table */
//				fingerTable.put(shaHashB, Bytes.toBytes(TFINGER_FILE_PATH_FAMILY), groupPathCol, pathB);
//				fingerTable.put(shaHashB, Bytes.toBytes(TFINGER_FILE_NAME_FAMILY), groupPathCol, fileNameB);
//				fingerTable.put(shaHashB, Bytes.toBytes(TFINGER_FILE_SIZE_FAMILY), groupPathCol, sizeInBytesB);
				
				fingerTable.incrementColumnValue(shaHashB, Bytes.toBytes(TFINGER_GROUP_FAMILY), fingerbookIdB, 1L);
				fingerTable.incrementColumnValue(shaHashB, Bytes.toBytes(TFINGER_GROUP_STR_FAMILY), Bytes.toBytes(fingerbookIdStr), 1L);
				fingerTable.incrementColumnValue(shaHashB, Bytes.toBytes(TFINGER_INFO_FAMILY), Bytes.toBytes(TFINGER_COLUMN_TOTAL), 1L);
//				
			}
			fingerTable.commitAndDestroy();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//				return -1;
		}
	}
	
//	public void commitAndDestroy() {
//		try {
//			groupTable.commitAndDestroy();
//			fingerTable.commitAndDestroy();
//			
//			fingerTable = null;
//			groupTable = null;
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public static Fingerbook loadMe(long fingerbookId) {
		
		return loadMe(fingerbookId, true);
	}
	
	public static Fingerbook loadMe(long fingerbookId, boolean loadFingerPrints) {
		
		Fingerbook fingerBook = new Fingerbook();
		fingerBook.setFingerbookId(fingerbookId);
		
//		Fingerprints auxFingerPrints = new Fingerprints();
		Fingerprints auxFingerPrints = null;
		byte[] fingerbookIdB = Bytes.toBytes(fingerbookId);
		
		try {
			byte[] stampB = HbaseManager.getValue(GROUP_TABLE_NAME, fingerbookIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_STAMP));
			fingerBook.setStamp(Bytes.toLong(stampB));
			
//			byte[] userB = HbaseManager.getValue(GROUP_TABLE_NAME, fingerbookIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_USER));
//			UserInfo userInfo = new UserInfo();
//			String user = "";
//			if(userB != null) {
//				user = Bytes.toString(userB);
//			}
//			userInfo.setUser(user);
			
			UserInfo userInfo = null;
			userInfo = loadUserInfoByFingerbookId(fingerbookId);
			fingerBook.setUserInfo(userInfo);
			
			Set<String> tags = loadTagsByFingerbookId(fingerbookId);
			fingerBook.setTags(tags);
			
			String comment = "";
			byte[] commentB = HbaseManager.getValue(GROUP_TABLE_NAME, fingerbookIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_COMMENT));
			if(commentB != null) {
				comment = Bytes.toString(commentB);
			}
			fingerBook.setComment(comment);
			
			if(loadFingerPrints) {
				/* Get the fingerprints by this fingerbook id */
				auxFingerPrints = loadFingerPrintsByFingerBook(fingerbookId);
			}
			
			fingerBook.setFingerPrints(auxFingerPrints);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fingerBook;
	}
	
	public static Fingerbook loadFingerbookStampTags(long fingerbookId) {
		
		Fingerbook fingerBook = new Fingerbook();
		fingerBook.setFingerbookId(fingerbookId);
//		Fingerprints auxFingerPrints = null;
		byte[] fingerbookIdB = Bytes.toBytes(fingerbookId);
		
		try {
			byte[] stampB = HbaseManager.getValue(GROUP_TABLE_NAME, fingerbookIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_STAMP));
			fingerBook.setStamp(Bytes.toLong(stampB));
			
			
//			UserInfo userInfo = null;
//			userInfo = loadUserInfoByFingerbookId(fingerbookId);
//			fingerBook.setUserInfo(userInfo);
			
			Set<String> tags = loadTagsByFingerbookId(fingerbookId);
			fingerBook.setTags(tags);
			
//			String comment = "";
//			byte[] commentB = HbaseManager.getValue(GROUP_TABLE_NAME, fingerbookIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_COMMENT));
//			if(commentB != null) {
//				comment = Bytes.toString(commentB);
//			}
//			fingerBook.setComment(comment);
			
//			if(loadFingerPrints) {
//				/* Get the fingerprints by this fingerbook id */
//				auxFingerPrints = loadFingerPrintsByFingerBook(fingerbookId);
//			}
//			fingerBook.setFingerPrints(auxFingerPrints);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fingerBook;
	}
	
	public static Fingerbook loadMe(long fingerbookId, boolean loadFingerPrints, int fingerprintsSize, int fingerprintsOffset) {
		
		Fingerbook fingerBook = new Fingerbook();
		fingerBook.setFingerbookId(fingerbookId);
		
//		Fingerprints auxFingerPrints = new Fingerprints();
		Fingerprints auxFingerPrints = new Fingerprints();
		byte[] fingerbookIdB = Bytes.toBytes(fingerbookId);
		
		try {
			byte[] stampB = HbaseManager.getValue(GROUP_TABLE_NAME, fingerbookIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_STAMP));
			fingerBook.setStamp(Bytes.toLong(stampB));
			
//			byte[] userB = HbaseManager.getValue(GROUP_TABLE_NAME, fingerbookIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_USER));
//			UserInfo userInfo = new UserInfo();
//			String user = "";
//			if(userB != null) {
//				user = Bytes.toString(userB);
//			}
//			userInfo.setUser(user);
			
			UserInfo userInfo = null;
			userInfo = loadUserInfoByFingerbookId(fingerbookId);
			fingerBook.setUserInfo(userInfo);
			
			Set<String> tags = loadTagsByFingerbookId(fingerbookId);
			fingerBook.setTags(tags);
			
			String comment = "";
			byte[] commentB = HbaseManager.getValue(GROUP_TABLE_NAME, fingerbookIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_COMMENT));
			if(commentB != null) {
				comment = Bytes.toString(commentB);
			}
			fingerBook.setComment(comment);
			
			if(loadFingerPrints) {
				/* Get the fingerprints by this fingerbook id */
//				auxFingerPrints = loadFingerPrintsByFingerBook(fingerbookId);
				loadFingerPrintsByFingerBookPag(auxFingerPrints, fingerbookId, fingerprintsSize, fingerprintsOffset);
			}
			
			fingerBook.setFingerPrints(auxFingerPrints);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fingerBook;
	}
	
	public static UserInfo loadUserInfoByFingerbookId(long fingerbookId) {
		
		UserInfo userInfo = new UserInfo();
		userInfo.setTicket("");
		userInfo.setUser("");
		
		NavigableMap<byte[],byte[]> familyMapGroupInfo = null;
		
		try {
			familyMapGroupInfo = HbaseManager.getMembersMap(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_INFO_FAMILY);
			
			if(familyMapGroupInfo != null) {
				for(byte[] colNameB: familyMapGroupInfo.keySet()) {
					
					String colName = Bytes.toString(colNameB);
					if(colName.equals(COLUMN_TICKET)) {
						userInfo.setTicket(Bytes.toString(familyMapGroupInfo.get(colNameB)));
					}
					else if(colName.equals(COLUMN_USER)) {
						userInfo.setUser(Bytes.toString(familyMapGroupInfo.get(colNameB)));
					}
				}
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return userInfo;
	}
	
	public static boolean validateOwnerUser(String user, long fingerbookId) {
		
		boolean isValid = false;
		byte[] fingerbookIdB = Bytes.toBytes(fingerbookId);
		
		if(user != null & !user.isEmpty()) {
			String savedUser = "";
			try {
				byte[] savedUserB = HbaseManager.getValue(GROUP_TABLE_NAME, fingerbookIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_USER));
				if(savedUserB != null) {
					savedUser = Bytes.toString(savedUserB);
					if(user.equals(savedUser)) {
						isValid = true;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return isValid;
	}
	
	public static boolean validateOwnerTicket(String ticket, long fingerbookId) {
		
		boolean isValid = false;
		byte[] fingerbookIdB = Bytes.toBytes(fingerbookId);
		
		if(ticket != null & !ticket.isEmpty()) {
			String savedTicket = "";
			try {
				byte[] savedTicketB = HbaseManager.getValue(GROUP_TABLE_NAME, fingerbookIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_TICKET));
				if(savedTicketB != null) {
					savedTicket = Bytes.toString(savedTicketB);
					if(ticket.equals(savedTicket)) {
						isValid = true;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return isValid;
	}
	
	public static Set<String> loadTagsByFingerbookId(long fingerbookId) {
		
		Set<String> tags = new HashSet<String>();
		
		NavigableMap<byte[],byte[]> familyMapTag = null;
		
		try {
			familyMapTag = HbaseManager.getMembersMap(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_TAG_FAMILY);
			
			if(familyMapTag != null) {
				for(byte[] colNameB: familyMapTag.keySet()) {
					
					String tag = Bytes.toString(colNameB);
					tags.add(tag);
				}
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return tags;
	}
	
	public static Fingerprints loadFingerPrintsByFingerBook(long fingerbookId) {
		
		Fingerprints auxFingerPrints = new Fingerprints();
		List<FileInfo> auxFiles = new ArrayList<FileInfo>();
		
		NavigableMap<byte[],byte[]> familyMap = null;
//		NavigableMap<byte[],byte[]> familyMapFileName = null;
//		NavigableMap<byte[],byte[]> familyMapFileSize = null;
		try {
			familyMap = HbaseManager.getMembersMap(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_FINGER_FAMILY);
//			familyMapFileName = HbaseManager.getMembersMap(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_FINGER_FILE_NAME_FAMILY);
//			familyMapFileSize = HbaseManager.getMembersMap(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_FINGER_FILE_SIZE_FAMILY);
			
			if(familyMap != null) {
				for(byte[] hashB: familyMap.keySet()) {
					
//					String shaHash = getHashFromHashPathCol(hashPathCol);
//					String path = getPathFromHashPathCol(hashPathCol);
					
					String shaHash = Bytes.toString(hashB);
					
					FileInfo auxFileInfo = new FileInfo();
					auxFileInfo.setShaHash(shaHash);
//					auxFileInfo.setPath(path);
//					auxFileInfo.setName(fileName);
//					auxFileInfo.setSizeInBytes(sizeInBytes);
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
	
	public static FingerprintsFeed getFingerprintsFeedByFingerBookPag(long fingerbookId, int limit, int offset) {
		
		FingerprintsFeed fingerprintsFeed = new FingerprintsFeed();
		Fingerprints auxFingerPrints = new Fingerprints();
		int total = 0;
		
		byte[] totalB;
		try {
			totalB = HbaseManager.getValue(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_INFO_FAMILY, Bytes.toBytes(TGROUP_COLUMN_TOTAL));
			
			if(totalB != null) {
				long totalLong = Bytes.toLong(totalB);
				total = (int) totalLong;
			}
			
			loadFingerPrintsByFingerBookPag(auxFingerPrints, fingerbookId, limit, offset);
			
			fingerprintsFeed.setFingerPrints(auxFingerPrints);
			fingerprintsFeed.setLimit(limit);
			fingerprintsFeed.setOffset(offset);
			fingerprintsFeed.setTotalresults(total);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return fingerprintsFeed;
		
	}
	
	public static int loadFingerPrintsByFingerBookPag(Fingerprints auxFingerPrints, long fingerbookId, int limit, int offset) {
		
		List<FileInfo> auxFiles = new ArrayList<FileInfo>();
		
		int nextOffset = -1;
		
		
		NavigableMap<byte[],byte[]> familyMap = null;
//		NavigableMap<byte[],byte[]> familyMapFileName = null;
//		NavigableMap<byte[],byte[]> familyMapFileSize = null;
		try {
//			familyMap = HbaseManager.getMembersMap(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_FINGER_FAMILY);
//			familyMapFileName = HbaseManager.getMembersMapPag(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_FINGER_FILE_NAME_FAMILY, limit, offset);
//			familyMapFileSize = HbaseManager.getMembersMapPag(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_FINGER_FILE_SIZE_FAMILY, limit, offset);
			
			
			
			familyMap = HbaseManager.getMembersMapPag(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_FINGER_FAMILY, limit, offset);
			
			if(familyMap != null) {
				
				if(familyMap.size() >= limit) {
					nextOffset = offset + limit;
				}
				else if(familyMap.size() > 0) {
					nextOffset = 0;
				}
				
				for(byte[] hashB: familyMap.keySet()) {
					
					String shaHash = Bytes.toString(hashB);
					
//					String shaHash = getHashFromHashPathCol(hashPathCol);
//					String path = getPathFromHashPathCol(hashPathCol);
//					
//					String fileName = Bytes.toString(familyMapFileName.get(hashPathCol));
//					long sizeInBytes = Bytes.toLong(familyMapFileSize.get(hashPathCol));
					
					FileInfo auxFileInfo = new FileInfo();
					auxFileInfo.setShaHash(shaHash);
//					auxFileInfo.setPath(path);
//					auxFileInfo.setName(fileName);
//					auxFileInfo.setSizeInBytes(sizeInBytes);
					auxFiles.add(auxFileInfo);
				}
			}
			auxFingerPrints.setFiles(auxFiles);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nextOffset;
	}
	
	public static Vector<Fingerbook> getFingerbooksByTicketPag(String ticket, int limit, int offset) {
		
		Vector<Fingerbook> fingerbooks = new Vector<Fingerbook>();
//		Vector<Long> fingerbooksIds = new Vector<Long>();
//		long nextId = -1;
		
		NavigableMap<byte[],byte[]> familyMapTicketGroup = null;
		try {
			
//			familyMapTicketGroup = HbaseManager.getMembersMap(TICKET_TABLE_NAME, Bytes.toBytes(ticket), TTICKET_GROUP_ID_FAMILY);
			familyMapTicketGroup = HbaseManager.getMembersMapPag(TICKET_TABLE_NAME, Bytes.toBytes(ticket), TTICKET_GROUP_ID_FAMILY, limit, offset);
			
			if(familyMapTicketGroup == null) {
				return fingerbooks;
			}
			
//			Vector<Long> loaded = new Vector<Long>();
			
//			boolean isNextId = false;
			
			for(byte[] fingerbookIdB: familyMapTicketGroup.keySet()) {
				
				long fingerbookId = Bytes.toLong(fingerbookIdB);
//				fingerbooksIds.add(new Long(fingerbookId));
				
				Fingerbook auxFingerbook = new Fingerbook();
				auxFingerbook.setFingerbookId(fingerbookId);
				
//				PersistentFingerbook persFingerbook = new PersistentFingerbook(auxFingerbook);
//				auxFingerbook = persFingerbook.loadMe(false);
				auxFingerbook = PersistentFingerbook.loadMe(fingerbookId, false);
				
				fingerbooks.add(auxFingerbook);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fingerbooks;
	}
	
	public static FingerbookList getFingerbookListByTicketPag(String ticket, int limit, int offset) {
		
		FingerbookList fingerbookList = new FingerbookList();
		int total = 0;
		Vector<Fingerbook> fingerbooks = new Vector<Fingerbook>();		
		NavigableMap<byte[],byte[]> familyMapTicketGroup = null;
		
		try {
			
//			familyMapTicketGroup = HbaseManager.getMembersMap(TICKET_TABLE_NAME, Bytes.toBytes(ticket), TTICKET_GROUP_ID_FAMILY);
			familyMapTicketGroup = HbaseManager.getMembersMapPag(TICKET_TABLE_NAME, Bytes.toBytes(ticket), TTICKET_GROUP_ID_FAMILY, limit, offset);
			
			byte[] totalB = HbaseManager.getValue(TICKET_TABLE_NAME, Bytes.toBytes(ticket), TTICKET_INFO_FAMILY, Bytes.toBytes(TTICKET_COLUMN_TOTAL));
			if(totalB != null) {
				long totalLong = Bytes.toLong(totalB);
				total = (int) totalLong;
			}
			
			fingerbookList.setLimit(limit);
			fingerbookList.setOffset(offset);
			fingerbookList.setTotalresults(total);
			
			
//			if(familyMapTicketGroup == null) {
//				return fingerbooks;
//			}
			
			if(familyMapTicketGroup != null) {
				
				for(byte[] fingerbookIdB: familyMapTicketGroup.keySet()) {
					
//					long fingerbookId = Bytes.toLong(fingerbookIdB);
					long fingerbookId = Long.MAX_VALUE - Bytes.toLong(fingerbookIdB);
					
					Fingerbook auxFingerbook = new Fingerbook();
					auxFingerbook.setFingerbookId(fingerbookId);
					
//					PersistentFingerbook persFingerbook = new PersistentFingerbook(auxFingerbook);
//					auxFingerbook = persFingerbook.loadMe(false);
					auxFingerbook = PersistentFingerbook.loadMe(fingerbookId, false);
					
					fingerbooks.add(auxFingerbook);
					
				}
			}
			fingerbookList.setFbs(fingerbooks);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fingerbookList;
	}
	
	public static FingerbookList getFingerbookListByUserPag(String user, int limit, int offset) {
		
		FingerbookList fingerbookList = new FingerbookList();
		
		int total = 0;
		Vector<Fingerbook> fingerbooks = new Vector<Fingerbook>();
		NavigableMap<byte[],byte[]> familyMapUserGroup = null;
		try {
			
			familyMapUserGroup = HbaseManager.getMembersMapPag(USER_TABLE_NAME, Bytes.toBytes(user), TUSER_GROUP_ID_FAMILY, limit, offset);
			
			byte[] totalB = HbaseManager.getValue(USER_TABLE_NAME, Bytes.toBytes(user), TUSER_INFO_FAMILY, Bytes.toBytes(TUSER_COLUMN_TOTAL));
			if(totalB != null) {
				long totalLong = Bytes.toLong(totalB);
				total = (int) totalLong;
			}
			
			fingerbookList.setLimit(limit);
			fingerbookList.setOffset(offset);
			fingerbookList.setTotalresults(total);
			
//			if(familyMapUserGroup == null) {
//				return fingerbooks;
//			}
			
			
			if(familyMapUserGroup != null) {
				for(byte[] fingerbookIdB: familyMapUserGroup.keySet()) {
					
	//				long fingerbookId = Bytes.toLong(fingerbookIdB);
					long fingerbookId = Long.MAX_VALUE - Bytes.toLong(fingerbookIdB);
					
					Fingerbook auxFingerbook = new Fingerbook();
					auxFingerbook.setFingerbookId(fingerbookId);
					
//					PersistentFingerbook persFingerbook = new PersistentFingerbook(auxFingerbook);
//					auxFingerbook = persFingerbook.loadMe(false);
					auxFingerbook = PersistentFingerbook.loadMe(fingerbookId, false);
					
					fingerbooks.add(auxFingerbook);
					
				}
			}
			
			fingerbookList.setFbs(fingerbooks);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fingerbookList;
	}
	
	public static FingerbookList getFingerbookListByHashPag(String hash, int limit, int offset) {
		
		FingerbookList fingerbookList = new FingerbookList();
		
		int total = 0;
		Vector<Fingerbook> fingerbooks = new Vector<Fingerbook>();
		NavigableMap<byte[],byte[]> familyMapHashGroup = null;
		try {
			
			familyMapHashGroup = HbaseManager.getMembersMapPag(FINGER_TABLE_NAME, Bytes.toBytes(hash), TFINGER_GROUP_FAMILY, limit, offset);
			
			byte[] totalB = HbaseManager.getValue(FINGER_TABLE_NAME, Bytes.toBytes(hash), TFINGER_INFO_FAMILY, Bytes.toBytes(TFINGER_COLUMN_TOTAL));
			if(totalB != null) {
				long totalLong = Bytes.toLong(totalB);
				total = (int) totalLong;
			}
			
			fingerbookList.setLimit(limit);
			fingerbookList.setOffset(offset);
			fingerbookList.setTotalresults(total);
			
//			if(familyMapUserGroup == null) {
//				return fingerbooks;
//			}
			
			
			if(familyMapHashGroup != null) {
				for(byte[] fingerbookIdB: familyMapHashGroup.keySet()) {
					
					long fingerbookId = Bytes.toLong(fingerbookIdB);
//					long fingerbookId = Long.MAX_VALUE - Bytes.toLong(fingerbookIdB);
					
					Fingerbook auxFingerbook = new Fingerbook();
					auxFingerbook.setFingerbookId(fingerbookId);
					
//					PersistentFingerbook persFingerbook = new PersistentFingerbook(auxFingerbook);
//					auxFingerbook = persFingerbook.loadMe(false);
					auxFingerbook = PersistentFingerbook.loadMe(fingerbookId, false);
					
					fingerbooks.add(auxFingerbook);
					
				}
			}
			
			fingerbookList.setFbs(fingerbooks);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fingerbookList;
	}
	
	public static FingerbookList getFingerbookListStampTagsByHashPag(String hash, int limit, int offset) {
		
		FingerbookList fingerbookList = new FingerbookList();
		
		int total = 0;
		Vector<Fingerbook> fingerbooks = new Vector<Fingerbook>();
		NavigableMap<byte[],byte[]> familyMapHashGroup = null;
		try {
			
			familyMapHashGroup = HbaseManager.getMembersMapPag(FINGER_TABLE_NAME, Bytes.toBytes(hash), TFINGER_GROUP_FAMILY, limit, offset);
			
			byte[] totalB = HbaseManager.getValue(FINGER_TABLE_NAME, Bytes.toBytes(hash), TFINGER_INFO_FAMILY, Bytes.toBytes(TFINGER_COLUMN_TOTAL));
			if(totalB != null) {
				long totalLong = Bytes.toLong(totalB);
				total = (int) totalLong;
			}
			
			fingerbookList.setLimit(limit);
			fingerbookList.setOffset(offset);
			fingerbookList.setTotalresults(total);
			
			if(familyMapHashGroup != null) {
				for(byte[] fingerbookIdB: familyMapHashGroup.keySet()) {
					
					long fingerbookId = Bytes.toLong(fingerbookIdB);
					
					Fingerbook auxFingerbook = new Fingerbook();
					auxFingerbook.setFingerbookId(fingerbookId);
					
//					auxFingerbook = PersistentFingerbook.loadMe(fingerbookId, false);
					auxFingerbook = PersistentFingerbook.loadFingerbookStampTags(fingerbookId);
					
					fingerbooks.add(auxFingerbook);
					
				}
			}
			
			fingerbookList.setFbs(fingerbooks);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fingerbookList;
	}
	
	public static Vector<Fingerbook> getFingerbooksByUserPag(String user, int limit, int offset) {
		
		Vector<Fingerbook> fingerbooks = new Vector<Fingerbook>();
//		Vector<Long> fingerbooksIds = new Vector<Long>();
//		long nextId = -1;
		
		NavigableMap<byte[],byte[]> familyMapUserGroup = null;
		try {
			
			familyMapUserGroup = HbaseManager.getMembersMapPag(USER_TABLE_NAME, Bytes.toBytes(user), TUSER_GROUP_ID_FAMILY, limit, offset);
			
			if(familyMapUserGroup == null) {
				return fingerbooks;
			}
			
//			Vector<Long> loaded = new Vector<Long>();
			
//			boolean isNextId = false;
			
			for(byte[] fingerbookIdB: familyMapUserGroup.keySet()) {
				
//				long fingerbookId = Bytes.toLong(fingerbookIdB);
				
				long fingerbookId = Long.MAX_VALUE - Bytes.toLong(fingerbookIdB);
				
				Fingerbook auxFingerbook = new Fingerbook();
				auxFingerbook.setFingerbookId(fingerbookId);
				
//				PersistentFingerbook persFingerbook = new PersistentFingerbook(auxFingerbook);
//				auxFingerbook = persFingerbook.loadMe(false);
				auxFingerbook = PersistentFingerbook.loadMe(fingerbookId, false);
				
				fingerbooks.add(auxFingerbook);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fingerbooks;
	}
	
	public static Vector<Fingerbook> getFingerbookByUser(String user) {
		
		Vector<Fingerbook> fingerbooks = new Vector<Fingerbook>();
//		NavigableMap<byte[],byte[]> familyMapGroupInfo = null;
		NavigableMap<byte[],byte[]> familyMapUserGroup = null;
		try {
//			familyMapGroupInfo = HbaseManager.getMembersMap(FINGER_TABLE_NAME, Bytes.toBytes(hash), TFINGER_GROUP_FAMILY);
			familyMapUserGroup = HbaseManager.getMembersMap(USER_TABLE_NAME, Bytes.toBytes(user), TUSER_GROUP_ID_FAMILY);
			if(familyMapUserGroup == null) {
				return fingerbooks;
			}
			
			Vector<Long> loaded = new Vector<Long>();
			for(byte[] fingerbookIdB: familyMapUserGroup.keySet()) {
				
//				long fingerbookId = getGroupIdFromCol(groupFNCol);
				long fingerbookId = Bytes.toLong(fingerbookIdB);
				
				if(!loaded.contains(fingerbookId)) {
					
					Fingerbook auxFingerbook = new Fingerbook();
					auxFingerbook.setFingerbookId(fingerbookId);
//					auxFingerbook.setStamp(stamp);
					
//					PersistentFingerbook persFingerbook = new PersistentFingerbook(auxFingerbook);
//					auxFingerbook = persFingerbook.loadMe();
					auxFingerbook = PersistentFingerbook.loadMe(fingerbookId);
					
//					Fingerprints auxFingerPrints = loadFingerPrintsByFingerBook(fingerbookId);
//					auxFingerbook.setFingerPrints(auxFingerPrints);
					
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
	
	public static SimilaritiesFeed getSimilaritiesFeed(long fingerbookId, int limit, int offset) {
		
		return getSimilaritiesFeed(fingerbookId, 0, limit, offset);
	}
	
	public static SimilaritiesFeed getSimilaritiesFeed(long fingerbookId, double threshold, int limit, int offset) {
		
		SimilaritiesFeed similaritiesFeed = new SimilaritiesFeed();
		
		Map<Fingerbook, Double> similsFbs = new LinkedHashMap<Fingerbook, Double>();
		Map<Long, Double> simils = null;
		
//		similsFbs = getSimilarFbs(fingerbookId, threshold, limit, offset);
		
		
		
		simils = getSimilarities(fingerbookId, threshold);
		
		int idx = 0;
		int last = limit + offset;
		
		for(Entry<Long, Double> entry: simils.entrySet()) {
			
			if(idx >= offset && idx < last) {
				
				Fingerbook auxFingerbook = new Fingerbook();
				auxFingerbook.setFingerbookId(entry.getKey());
				auxFingerbook = PersistentFingerbook.loadFingerbookStampTags(entry.getKey());
				
				similsFbs.put(auxFingerbook, entry.getValue());
				
			}
			else if(idx >= last) {
				break;
			}
			idx++;
		}
		
		similaritiesFeed.setSimilsFbs(similsFbs);
		similaritiesFeed.setLimit(limit);
		similaritiesFeed.setOffset(offset);
		similaritiesFeed.setTotalresults(simils.size());
		
		return similaritiesFeed;
	}
	
	public static Map<Fingerbook, Double> getSimilarFbs(long fingerbookId, double threshold, int limit, int offset) {
		
		Map<Fingerbook, Double> similsFbs = new LinkedHashMap<Fingerbook, Double>();
		Map<Long, Double> simils = null;
		
		simils = getSimilarities(fingerbookId, threshold);
		
		int idx = 0;
		int last = limit + offset;
		
		for(Entry<Long, Double> entry: simils.entrySet()) {
			
			if(idx >= offset && idx < last) {
				
				Fingerbook auxFingerbook = new Fingerbook();
				auxFingerbook.setFingerbookId(entry.getKey());
				auxFingerbook = PersistentFingerbook.loadFingerbookStampTags(entry.getKey());
				
				similsFbs.put(auxFingerbook, entry.getValue());
				
			}
			else if(idx >= last) {
				break;
			}
			idx++;
		}
		
		return similsFbs;
	}
	
	public static Map<Long, Double> getSimilarities(long fingerbookId, double threshold) {
		
//		Vector<Fingerbook> fingerbooks = new Vector<Fingerbook>();
		NavigableMap<byte[],byte[]> familyMapSimilGroup = null;
		
		HashMap<Long, Double> simils = new HashMap<Long, Double>();
		
		ValueComparator bvc =  new ValueComparator(simils);
		TreeMap<Long,Double> similsSorted = new TreeMap<Long, Double>(bvc);
		
		String fbIdStr = String.valueOf(fingerbookId);
		
		String thresholdStr = String.valueOf(threshold);
		byte[] thresholdB = Bytes.toBytes(thresholdStr);
		
		try {
			
//			familyMapSimilGroup = HbaseManager.getMembersMap(SIMIL_TABLE_NAME, Bytes.toBytes(fbIdStr), TSIMIL_GROUP_FID_FAMILY);
			familyMapSimilGroup = HbaseManager.getMembersMapFiltered(SIMIL_TABLE_NAME, Bytes.toBytes(fbIdStr), TSIMIL_GROUP_FID_FAMILY, thresholdB, CompareOp.GREATER_OR_EQUAL);
			
			if(familyMapSimilGroup == null) {
				return similsSorted;
			}
			
//			Vector<Long> loaded = new Vector<Long>();
			for(byte[] fingerbookIdB: familyMapSimilGroup.keySet()) {
				
				
				String auxFingerbookIdStr = Bytes.toString(fingerbookIdB);
//				long auxFingerbookId = Bytes.toLong(fingerbookIdB);
				Long auxFingerbookId = Long.valueOf(auxFingerbookIdStr);
				
				String matchStr = Bytes.toString(familyMapSimilGroup.get(fingerbookIdB));
				
//				Long match = Long.valueOf(matchStr);
				Double match = Double.valueOf(matchStr);
				
				System.out.println(auxFingerbookId + " --> " + match);
				
//				if(auxFingerbookId.longValue() != fingerbookId && match.doubleValue() >= threshold) {
				if(auxFingerbookId.longValue() != fingerbookId) {
					simils.put(auxFingerbookId, match);
				}
				
//				if(!loaded.contains(auxFingerbookId)) {
//					
//					Fingerbook auxFingerbook = new Fingerbook();
//					auxFingerbook.setFingerbookId(auxFingerbookId);
//
//					auxFingerbook = PersistentFingerbook.loadMe(auxFingerbookId);
//										
//					fingerbooks.add(auxFingerbook);
//					loaded.add(auxFingerbookId);
//				}
			}
			
			similsSorted.putAll(simils);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return similsSorted;
	}
	
	public static Vector<Fingerbook> getFingerbookByTicket(String ticket) {
		
		Vector<Fingerbook> fingerbooks = new Vector<Fingerbook>();
//		NavigableMap<byte[],byte[]> familyMapGroupInfo = null;
		NavigableMap<byte[],byte[]> familyMapTicketGroup = null;
		try {
//			familyMapGroupInfo = HbaseManager.getMembersMap(FINGER_TABLE_NAME, Bytes.toBytes(hash), TFINGER_GROUP_FAMILY);
			familyMapTicketGroup = HbaseManager.getMembersMap(TICKET_TABLE_NAME, Bytes.toBytes(ticket), TTICKET_GROUP_ID_FAMILY);
			if(familyMapTicketGroup == null) {
				return fingerbooks;
			}
			
			Vector<Long> loaded = new Vector<Long>();
			for(byte[] fingerbookIdB: familyMapTicketGroup.keySet()) {
				
//				long fingerbookId = getGroupIdFromCol(groupFNCol);
				long fingerbookId = Bytes.toLong(fingerbookIdB);
				
				if(!loaded.contains(fingerbookId)) {
					
					Fingerbook auxFingerbook = new Fingerbook();
					auxFingerbook.setFingerbookId(fingerbookId);
//					auxFingerbook.setStamp(stamp);
					
//					PersistentFingerbook persFingerbook = new PersistentFingerbook(auxFingerbook);
//					auxFingerbook = persFingerbook.loadMe();
					auxFingerbook = PersistentFingerbook.loadMe(fingerbookId);
					
//					Fingerprints auxFingerPrints = loadFingerPrintsByFingerBook(fingerbookId);
//					auxFingerbook.setFingerPrints(auxFingerPrints);
					
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
	
	public static int getNextFingerbookByUser(Fingerbook auxFingerbook, String user, long lastFingerbookId, int offset) {
		
		long nextFingerbookId = lastFingerbookId;
//		int auxOffset = offset;
		
		if(offset == 0) {
			nextFingerbookId = getNextFingerbookIdByUser(user, lastFingerbookId);
//			auxOffset = 0;
		}
		
		if(nextFingerbookId < 0) {
			auxFingerbook.setFingerbookId(new Long(-1));
			return -1;
		}
		
		auxFingerbook.setFingerbookId(nextFingerbookId);
		
		
		Fingerprints auxFingerPrints = new Fingerprints();
		int nextOffset = loadFingerPrintsByFingerBookPag(auxFingerPrints, nextFingerbookId, FILE_INFO_PAGINATION_LIMIT, offset);
		
		if(nextOffset >= 0) {
			auxFingerbook.setFingerPrints(auxFingerPrints);
		}
		else {
			nextOffset = getNextFingerbookByUser(auxFingerbook, user, nextFingerbookId, 0);
		}
		
		return nextOffset;
	}
	
	public static int getNextFingerbookByTicket(Fingerbook auxFingerbook, String ticket, long lastFingerbookId, int offset) {
		
		long nextFingerbookId = lastFingerbookId;
//		int auxOffset = offset;
		
		if(offset == 0) {
			nextFingerbookId = getNextFingerbookIdByTicket(ticket, lastFingerbookId);
//			auxOffset = 0;
		}
		
		if(nextFingerbookId < 0) {
			auxFingerbook.setFingerbookId(new Long(-1));
			return -1;
		}
		
		auxFingerbook.setFingerbookId(nextFingerbookId);
		
		Fingerprints auxFingerPrints = new Fingerprints();
		int nextOffset = loadFingerPrintsByFingerBookPag(auxFingerPrints, nextFingerbookId, FILE_INFO_PAGINATION_LIMIT, offset);
		
		if(nextOffset >= 0) {
			auxFingerbook.setFingerPrints(auxFingerPrints);
		}
		else {
			nextOffset = getNextFingerbookByTicket(auxFingerbook, ticket, nextFingerbookId, 0);
		}
		

		return nextOffset;
	}
	
	public static int getNextFingerbookByHash(Fingerbook auxFingerbook, String hash, long lastFingerbookId, int offset) {
		
		long nextFingerbookId = lastFingerbookId;
//		int auxOffset = offset;
		
		if(offset == 0) {
			nextFingerbookId = getNextFingerbookIdByHash(hash, lastFingerbookId);
//			auxOffset = 0;
		}
		
		if(nextFingerbookId < 0) {
			auxFingerbook.setFingerbookId(new Long(-1));
			return -1;
		}
		
		auxFingerbook.setFingerbookId(nextFingerbookId);
				
		Fingerprints auxFingerPrints = new Fingerprints();
		int nextOffset = loadFingerPrintsByFingerBookPag(auxFingerPrints, nextFingerbookId, FILE_INFO_PAGINATION_LIMIT, offset);
		
		if(nextOffset >= 0) {
			auxFingerbook.setFingerPrints(auxFingerPrints);
		}
		else {
			nextOffset = getNextFingerbookByHash(auxFingerbook, hash, nextFingerbookId, 0);
		}
		
		return nextOffset;
	}
	
	public static long getNextFingerbookIdByUser(String user, long lastFingerbookId) {
		
//		Vector<Long> fingerbooksIds = new Vector<Long>();
		long nextId = -1;
		
		NavigableMap<byte[],byte[]> familyMapUserGroup = null;
		try {
			familyMapUserGroup = HbaseManager.getMembersMap(USER_TABLE_NAME, Bytes.toBytes(user), TUSER_GROUP_ID_FAMILY);
			if(familyMapUserGroup == null) {
				return nextId;
			}
			
//			Vector<Long> loaded = new Vector<Long>();
			
			boolean isNextId = false;
			
			if(lastFingerbookId < 0) {
				isNextId = true;
			}
			
			for(byte[] fingerbookIdB: familyMapUserGroup.keySet()) {
				
				long fingerbookId = Bytes.toLong(fingerbookIdB);
				
				if(isNextId) {
					nextId = fingerbookId;
					return nextId;
				}
				
				if(fingerbookId == lastFingerbookId) {
					isNextId = true;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nextId;
	}
	
	public static long getNextFingerbookIdByTicket(String ticket, long lastFingerbookId) {
		
//		Vector<Long> fingerbooksIds = new Vector<Long>();
		long nextId = -1;
		
		NavigableMap<byte[],byte[]> familyMapTicketGroup = null;
		try {
			familyMapTicketGroup = HbaseManager.getMembersMap(TICKET_TABLE_NAME, Bytes.toBytes(ticket), TTICKET_GROUP_ID_FAMILY);
			if(familyMapTicketGroup == null) {
				return nextId;
			}
			
//			Vector<Long> loaded = new Vector<Long>();
			
			boolean isNextId = false;
			
			if(lastFingerbookId < 0) {
				isNextId = true;
			}
			
			for(byte[] fingerbookIdB: familyMapTicketGroup.keySet()) {
				
				long fingerbookId = Bytes.toLong(fingerbookIdB);
				
				if(isNextId) {
					nextId = fingerbookId;
					return nextId;
				}
				
				if(fingerbookId == lastFingerbookId) {
					isNextId = true;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nextId;
	}
	
	public static long getNextFingerbookIdByHash(String hash, long lastFingerbookId) {
		
//		Vector<Long> fingerbooksIds = new Vector<Long>();
		long nextId = -1;
		
		NavigableMap<byte[],byte[]> familyMapGroupInfo = null;
		try {
			familyMapGroupInfo = HbaseManager.getMembersMap(FINGER_TABLE_NAME, Bytes.toBytes(hash), TFINGER_GROUP_FAMILY);
			if(familyMapGroupInfo == null) {
				return nextId;
			}
			
//			Vector<Long> loaded = new Vector<Long>();
			
			boolean isNextId = false;
			
			if(lastFingerbookId < 0) {
				isNextId = true;
			}
			
			for(byte[] fingerbookIdB: familyMapGroupInfo.keySet()) {
				
				long fingerbookId = Bytes.toLong(fingerbookIdB);
				
				if(isNextId) {
					nextId = fingerbookId;
					return nextId;
				}
				
				if(fingerbookId == lastFingerbookId) {
					isNextId = true;
				}
				
//				if(!fingerbooksIds.contains(fingerbookId)) {
//					
//					if(isNextId) {
//						nextId = fingerbookId;
//						return nextId;
//					}
//					
//					if(fingerbookId == lastFingerbookId) {
//						isNextId = true;
//					}
//					
//					fingerbooksIds.add(fingerbookId);
//				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nextId;
	}
	
	
	public static Vector<Fingerbook> getFingerbookByHash(String hash) {
		
		Vector<Fingerbook> fingerbooks = new Vector<Fingerbook>();
		NavigableMap<byte[],byte[]> familyMapGroupInfo = null;
		try {
//			familyMap = HbaseManager.getMembersMap(FINGER_TABLE_NAME, Bytes.toBytes(hash), TFINGER_FILE_FAMILY);
			familyMapGroupInfo = HbaseManager.getMembersMap(FINGER_TABLE_NAME, Bytes.toBytes(hash), TFINGER_GROUP_FAMILY);
			if(familyMapGroupInfo == null) {
				return fingerbooks;
			}
			
			Vector<Long> loaded = new Vector<Long>();
			for(byte[] fingerbookIdB: familyMapGroupInfo.keySet()) {
				
//				long fingerbookId = getGroupIdFromCol(groupFNCol);
				long fingerbookId = Bytes.toLong(fingerbookIdB);
				
				if(!loaded.contains(fingerbookId)) {
					
					Fingerbook auxFingerbook = new Fingerbook();
					auxFingerbook.setFingerbookId(fingerbookId);
//					auxFingerbook.setStamp(stamp);
					
//					PersistentFingerbook persFingerbook = new PersistentFingerbook(auxFingerbook);
//					auxFingerbook = persFingerbook.loadMe();
					auxFingerbook = PersistentFingerbook.loadMe(fingerbookId);
					
//					Fingerprints auxFingerPrints = loadFingerPrintsByFingerBook(fingerbookId);
//					auxFingerbook.setFingerPrints(auxFingerPrints);
					
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
	public static void deleteFingerTables() throws IOException {
		if(HbaseManager.tableExists(FINGER_TABLE_NAME)) {
			HbaseManager.deleteTable(FINGER_TABLE_NAME);
		}
		if(HbaseManager.tableExists(GROUP_TABLE_NAME)) {
			HbaseManager.deleteTable(GROUP_TABLE_NAME);
		}
		if(HbaseManager.tableExists(TMP_TABLE_NAME)) {
			HbaseManager.deleteTable(TMP_TABLE_NAME);
		}
		if(HbaseManager.tableExists(GENERAL_TABLE_NAME)) {
			HbaseManager.deleteTable(GENERAL_TABLE_NAME);
		}
		if(HbaseManager.tableExists(TICKET_TABLE_NAME)) {
			HbaseManager.deleteTable(TICKET_TABLE_NAME);
		}
		if(HbaseManager.tableExists(USER_TABLE_NAME)) {
			HbaseManager.deleteTable(USER_TABLE_NAME);
		}
	}
	
	public static void createFingerTables() throws IOException {
		
		Vector<String> families = new Vector<String>();
		
		/* TFINGER table */
		families.add(TFINGER_GROUP_FAMILY);
		families.add(TFINGER_GROUP_STR_FAMILY);
		families.add(TFINGER_INFO_FAMILY);
		HbaseManager.createTable(FINGER_TABLE_NAME, families);
		
		/* TGROUP table */
		families = new Vector<String>();
		families.add(TGROUP_FINGER_FAMILY);
		families.add(TGROUP_INFO_FAMILY);
		families.add(TGROUP_TAG_FAMILY);
//		families.add(TGROUP_FINGER_FILE_NAME_FAMILY);
//		families.add(TGROUP_FINGER_FILE_SIZE_FAMILY);
		HbaseManager.createTable(GROUP_TABLE_NAME, families);
		
		/* TTMP table */
		families = new Vector<String>();
		families.add(TTMP_STATE_FAMILY);
		families.add(TTMP_INFO_FAMILY);
		HbaseManager.createTable(TMP_TABLE_NAME, families);
		
		/* TGENERAL table */
		families = new Vector<String>();
		families.add(TGENERAL_INFO_FAMILY);
		HbaseManager.createTable(GENERAL_TABLE_NAME, families);
		
		/* TTICKET table */
		families = new Vector<String>();
		families.add(TTICKET_GROUP_ID_FAMILY);
		families.add(TTICKET_INFO_FAMILY);
		HbaseManager.createTable(TICKET_TABLE_NAME, families);
		
		/* TUSER table */
		families = new Vector<String>();
		families.add(TUSER_GROUP_ID_FAMILY);
		families.add(TUSER_INFO_FAMILY);
		HbaseManager.createTable(USER_TABLE_NAME, families);
	}
	
	private static synchronized long insertGroupInfo(Fingerbook fingerbook, int authMethod) {
		
		try {
			long auxFingerbookId = getNextGroupId();
			if(auxFingerbookId < 0) {
				return -1;
			}
			
			long nowStamp = System.currentTimeMillis();
			
			byte[] groupIdB = Bytes.toBytes(auxFingerbookId);
//			byte[] stampB = Bytes.toBytes(fingerbook.getStamp());
			byte[] stampB = Bytes.toBytes(nowStamp);
			
			byte[] userB = null;
			UserInfo userInfo = fingerbook.getUserInfo();
			String ticket = null;
			String user = null;
			
			/* Insert stamp in group table */
			HbaseManager.putValue(GROUP_TABLE_NAME, groupIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_STAMP), stampB);
			if(userInfo != null) {
				
				if(authMethod == Auth.AUTH_AUTHENTICATED) {
					user = userInfo.getUser();
					System.out.println("\n\n\nUSER: " + userInfo.getUser());
					
					if(user != null && !user.isEmpty()) {
						userB = Bytes.toBytes(userInfo.getUser());
						HbaseManager.putValue(GROUP_TABLE_NAME, groupIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_USER), userB);
					}
				}
				else if(authMethod == Auth.AUTH_SEMI_AUTHENTICATED) {
					ticket = userInfo.getTicket();
				}
				
			}
			else {
				userInfo = new UserInfo();
				fingerbook.setUserInfo(userInfo);
			}
			
			if(authMethod != Auth.AUTH_AUTHENTICATED) {
				boolean isValidTicket = isValidTicket(ticket);
				if(!isValidTicket) {
					ticket = createTicket(auxFingerbookId);
				}
				HbaseManager.putValue(GROUP_TABLE_NAME, groupIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_TICKET), Bytes.toBytes(ticket));
				userInfo.setTicket(ticket);
			}
			
			//Set<String> tags = fingerbook.getTags();
			//int tagsInserted = insertTags(auxFingerbookId, tags);
			
			//String comment = fingerbook.getComment();
			//int commentInserted = insertComment(auxFingerbookId, comment);
			
			
			
			return auxFingerbookId;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public static int updateTagsComment(Fingerbook fingerbook) {
		
		Set<String> tags = null;
		String comment = null;
		long fingerbookId = -1;
		
		if(fingerbook == null) {
			return -1;
		}
		
		fingerbookId = fingerbook.getFingerbookId();
		if(fingerbookId < 0) {
			return -2;
		}
		
		byte[] groupIdB = Bytes.toBytes(fingerbookId);
		tags = fingerbook.getTags();
		comment = fingerbook.getComment();
		
		if(comment == null) {
			comment = "";
		}
		insertComment(fingerbookId, comment);
		
		try {
			HbaseManager.deleteFamily(GROUP_TABLE_NAME, groupIdB, TGROUP_TAG_FAMILY);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -3;
		}
		if(tags != null) {
			insertTags(fingerbookId, tags);
		}
		
		return 0;
		
	}
	
	public static int insertTags(long fingerbookId, Set<String> tags) {
		
		byte[] groupIdB = Bytes.toBytes(fingerbookId);
		
		System.out.println("InsertTags");

		try {
			if(tags != null) {
				
				for(String tag: tags) {
					System.out.println("Tag: " + tag);
					if(tag != null && !tag.isEmpty()) {
						HbaseManager.putValue(GROUP_TABLE_NAME, groupIdB, TGROUP_TAG_FAMILY, Bytes.toBytes(tag), Bytes.toBytes(0));
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}
	
	public static int insertComment(long fingerbookId, String comment) {
		
		byte[] groupIdB = Bytes.toBytes(fingerbookId);
		
		try {
			if(comment != null) {
				HbaseManager.putValue(GROUP_TABLE_NAME, groupIdB, TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_COMMENT), Bytes.toBytes(comment));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}
	
	private static synchronized long getNextGroupId() throws IOException {
		
		long maxGroupId = 0;
//		HBaseConfiguration config = new HBaseConfiguration();
//	    HTable table = new HTable(config, GENERAL_TABLE_NAME);
	    TransHTable table = new TransHTable(GENERAL_TABLE_NAME);
	    
	    byte[] rowB = Bytes.toBytes(TGENERAL_ROW_ID);
	    byte[] familyB = Bytes.toBytes(TGENERAL_INFO_FAMILY);
	    byte[] columnB = Bytes.toBytes(COLUMN_LAST_GROUP);
	    
	    maxGroupId = table.incrementColumnValue(rowB, familyB, columnB, 1L);
	    
	    table.commitAndDestroy();
	    
		return maxGroupId;
	}
	
//	private static synchronized long getNextGroupId() throws IOException {
//		
//		long maxGroupId = 0;
//		HBaseConfiguration config = new HBaseConfiguration();
//	    HTable table = new HTable(config, GROUP_TABLE_NAME);
//	    
//	    byte[] rowB = Bytes.toBytes(Long.MAX_VALUE);
//	    byte[] familyB = Bytes.toBytes(TGROUP_INFO_FAMILY);
//	    
//		Result result = table.getRowOrBefore(rowB, familyB);
//		
//		if(result != null) {
//			maxGroupId = Bytes.toLong(result.getRow());			
//		}
//		
//		
//		return maxGroupId + 1;
//	}
	
	/* Creates column name appending groupId and file name. (For tfinger table) */
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
	
	/* Creates column name appending groupId and file path. (For tfinger table) */
	public static byte[] createGroupPathCol(long groupId, String path) {
		
		byte[] column = null;
		
		byte[] groupIdB = Bytes.toBytes(groupId);
		byte[] pathB = Bytes.toBytes(path);
		column = new byte[groupIdB.length + pathB.length];
		
		System.arraycopy(groupIdB, 0, column, 0, groupIdB.length);
	    System.arraycopy(pathB, 0, column, groupIdB.length, pathB.length);
		
//		String auxCol = String.valueOf(groupId) + fileName;
//		column = Bytes.toBytes(auxCol);
		
		return column;
	}
	
	public static long getGroupIdFromCol(byte[] column) {
		
		long groupId = -1;
		
		groupId = Bytes.toLong(column, 0, 8);
		
		return groupId;
	}
	
	/* Returns the file name from the column name "groupId + file name". (For tfinger table) */
	public static String getFileNameFromGroupFNCol(byte[] column) {
		
		String fileName = null;
		
		fileName = Bytes.toString(column, 8, column.length);
		
		return fileName;
	}
	
	/* Returns the path from the column name "groupId + path". (For tfinger table) */
	public static String getPathFromGroupFNCol(byte[] column) {
		
		String path = null;
		
		path = Bytes.toString(column, 8, column.length);
		
		return path;
	}
	
	/* Creates column name appending hash and file name. (For tgroup table) */
	public static byte[] createHashFileNameCol(String hash, String fileName) {
		
		byte[] column = null;
		
		String auxCol = hash + String.valueOf((char) separator) + fileName;
		column = Bytes.toBytes(auxCol);
		
		return column;
	}
	
	/* Creates column name appending hash and path. (For tgroup table) */
	public static byte[] createHashPathCol(String hash, String path) {
		
		byte[] column = null;
		
		String auxCol = hash + String.valueOf((char) separator) + path;
		column = Bytes.toBytes(auxCol);
		
		return column;
	}
	
	/* Returns the path from the column name "hash + path" (For tgroup table) */
	public static String getPathFromHashPathCol(byte[] column) {
		
		String path = null;
		
		String aux = Bytes.toString(column);
		String[] auxArr = aux.split(String.valueOf((char) separator));
		
		path = auxArr[1];
		
		return path;
	}
	
	/* Returns the hash from the column name "hash + path" (For tgroup table) */
	public static String getHashFromHashPathCol(byte[] column) {
		
		String hash = null;
		
		String aux = Bytes.toString(column);
		String[] auxArr = aux.split(String.valueOf((char) separator));
		
		hash = auxArr[0];
		
		return hash;
	}
	
	/* Returns the file name from the column name "hash + file name" (For tgroup table) */
	public static String getFileNameFromHashFNCol(byte[] column) {
		
		String fileName = null;
		
		String aux = Bytes.toString(column);
		String[] auxArr = aux.split(String.valueOf((char) separator));
		
		fileName = auxArr[1];
		
		return fileName;
	}
	
	/* Returns the hash from the column name "hash + file name" (For tgroup table) */
	public static String getHashFromHashFNCol(byte[] column) {
		
		String hash = null;
		
		String aux = Bytes.toString(column);
		String[] auxArr = aux.split(String.valueOf((char) separator));
		
		hash = auxArr[0];
		
		return hash;
	}
	
//	private static void updateTmpState(long fingerbookId) throws IOException {
//		
//		long nowStamp = System.currentTimeMillis();
//		byte[] groupIdB = Bytes.toBytes(fingerbookId);
//		byte[] nowStampB = Bytes.toBytes(nowStamp);
//		
//		/* Agregar TransactionID para cuando se quiere resumir obtener el FBID */
//		
//		/* Insert stamp in tmp table */
//		HbaseManager.putValue(TMP_TABLE_NAME, groupIdB, TTMP_STATE_FAMILY, Bytes.toBytes(COLUMN_LAST_ACTION), nowStampB);
//	}
	
	private static void updateTmpState(long fingerbookId) throws IOException {
		
		String auxTransId = createTransactionId(fingerbookId);
		
		long nowStamp = System.currentTimeMillis();
		//byte[] groupIdB = Bytes.toBytes(fingerbookId);
		byte[] nowStampB = Bytes.toBytes(nowStamp);
		
		byte[] auxTransIdB = Bytes.toBytes(auxTransId);
		
		/* Insert stamp in tmp table */
		HbaseManager.putValue(TMP_TABLE_NAME, auxTransIdB, TTMP_STATE_FAMILY, Bytes.toBytes(TTMP_COLUMN_LAST_ACTION), nowStampB);
//		HbaseManager.putValue(TMP_TABLE_NAME, auxTransIdB, TTMP_INFO_FAMILY, Bytes.toBytes(TTMP_COLUMN_GROUP_ID), groupIdB);
	}
	
	private static void createTmpState(Fingerbook fingerbook, int authMethod) throws IOException {
		
		long fingerbookId = fingerbook.getFingerbookId();
		String auxTransId = createTransactionId(fingerbookId);
		
		long nowStamp = System.currentTimeMillis();
		byte[] groupIdB = Bytes.toBytes(fingerbookId);
		byte[] nowStampB = Bytes.toBytes(nowStamp);
		
		byte[] auxTransIdB = Bytes.toBytes(auxTransId);
		
		/* Insert stamp in tmp table */
		HbaseManager.putValue(TMP_TABLE_NAME, auxTransIdB, TTMP_STATE_FAMILY, Bytes.toBytes(TTMP_COLUMN_LAST_ACTION), nowStampB);
		HbaseManager.putValue(TMP_TABLE_NAME, auxTransIdB, TTMP_INFO_FAMILY, Bytes.toBytes(TTMP_COLUMN_GROUP_ID), groupIdB);
		
		if(authMethod == Auth.AUTH_AUTHENTICATED) {
			
			byte[] userB = Bytes.toBytes(fingerbook.getUserInfo().getUser());
			HbaseManager.putValue(TMP_TABLE_NAME, auxTransIdB, TTMP_INFO_FAMILY, Bytes.toBytes(TTMP_COLUMN_USER), userB);
		}
		else {
			byte[] ticketB = Bytes.toBytes(fingerbook.getUserInfo().getTicket());
			HbaseManager.putValue(TMP_TABLE_NAME, auxTransIdB, TTMP_INFO_FAMILY, Bytes.toBytes(TTMP_COLUMN_TICKET), ticketB);
		}
	}
	
	public static Vector<Long> getExpiredFingerbooksIds(long timeout) throws IOException {
		
		Vector<Long> ids = new Vector<Long>();
		long nowStamp = System.currentTimeMillis();
		long expiration = nowStamp - timeout;
		byte[] expStampB = Bytes.toBytes(expiration);
		
//		Vector<byte[]> rows = HbaseManager.filterRows(TMP_TABLE_NAME, Bytes.toBytes(TTMP_STATE_FAMILY), Bytes.toBytes(COLUMN_LAST_ACTION), expStampB, CompareOp.LESS);
//		SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(TTMP_STATE_FAMILY), Bytes.toBytes(COLUMN_LAST_ACTION), CompareOp.LESS, expStampB);
		
//		Vector<byte[]> rows = HbaseManager.getColumnValuesFromFilteredRows(TMP_TABLE_NAME, Bytes.toBytes(TTMP_STATE_FAMILY), Bytes.toBytes(TTMP_COLUMN_LAST_ACTION), expStampB, CompareOp.LESS, Bytes.toBytes(TTMP_STATE_FAMILY), Bytes.toBytes(TTMP_COLUMN_GROUP_ID));
		Vector<byte[]> rows = HbaseManager.getColumnValuesFromFilteredRows(TMP_TABLE_NAME, Bytes.toBytes(TTMP_STATE_FAMILY), Bytes.toBytes(TTMP_COLUMN_LAST_ACTION), expStampB, CompareOp.LESS, Bytes.toBytes(TTMP_INFO_FAMILY), Bytes.toBytes(TTMP_COLUMN_GROUP_ID));
		
		for(byte[] row: rows) {
			ids.add(Bytes.toLong(row));
		}
		
		return ids;
	}
	
	private static int deleteFingerbookFromTmp(long fingerbookId) {
		
		String transactionId = createTransactionId(fingerbookId);
		byte[] transactionIdB = Bytes.toBytes(transactionId);
		
//		byte[] groupIdB = Bytes.toBytes(fingerbookId);
		try {
//			HbaseManager.deleteRow(TMP_TABLE_NAME, groupIdB);
			HbaseManager.deleteRow(TMP_TABLE_NAME, transactionIdB);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}
	
	private static int deleteFingerbookFromTGroup(long fingerbookId) {
		
		byte[] groupIdB = Bytes.toBytes(fingerbookId);
		try {
			HbaseManager.deleteRow(GROUP_TABLE_NAME, groupIdB);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}
	
	public static boolean validateResume(Fingerbook fingerbook, int authMethod) {
		
		boolean ret = true;
		NavigableMap<byte[],NavigableMap<byte[],byte[]>> tmpData = null;
		
		//NavigableMap<byte[],byte[]> familyMapState = null;
		NavigableMap<byte[],byte[]> familyMapInfo = null;
		
		String transactionId = null;
		String ticket = null;
		String user = null;
		long fingerbookId = -1;
		
		if(fingerbook != null && fingerbook.getTransactionId() != null) {
			transactionId = fingerbook.getTransactionId();
			
			tmpData = getTmpDataByTransactionId(transactionId);
			
			if(tmpData != null) {
				familyMapInfo = tmpData.get(Bytes.toBytes(TTMP_INFO_FAMILY));
				
				if(familyMapInfo != null) {
					byte[] groupIdB = familyMapInfo.get(Bytes.toBytes(TTMP_COLUMN_GROUP_ID));
					byte[] ticketB = familyMapInfo.get(Bytes.toBytes(TTMP_COLUMN_TICKET));
					byte[] userB = familyMapInfo.get(Bytes.toBytes(TTMP_COLUMN_USER));
					
					if(groupIdB != null && groupIdB.length > 0) {
						fingerbookId = Bytes.toLong(groupIdB);
					}
					if(ticketB != null && ticketB.length > 0) {
						ticket = Bytes.toString(ticketB);
					}
					if(userB != null && userB.length > 0) {
						user = Bytes.toString(userB);
					}
				}
			}
			
			if(fingerbookId < 0) {
				/* Invalid or expired Transaction id */
				ret = false;
			}
			else {
				if(authMethod == Auth.AUTH_AUTHENTICATED) {
					/* Autenticated: validate user */
					if(user == null || fingerbook.getUserInfo() == null || !user.equals(fingerbook.getUserInfo().getUser())) {
						ret = false;
					}
				}
				else if(authMethod == Auth.AUTH_SEMI_AUTHENTICATED) {
					/* Semi-Autenticated: validate ticket */
					if(ticket == null || fingerbook.getUserInfo() == null || !ticket.equals(fingerbook.getUserInfo().getTicket())) {
						ret = false;
					}
				}
				else {
					/* Anonymous: Stored ticket should match a new ticket created using fingerbookId */
					if(ticket == null || !ticket.equals(createTicket(fingerbookId))) {
						ret = false;
					}
					else {
						UserInfo userInfo = fingerbook.getUserInfo();
						if(userInfo == null) {
							userInfo = new UserInfo();
						}
						userInfo.setTicket(ticket);
						fingerbook.setUserInfo(userInfo);
					}
				}
			}
		}
		else {
			ret = false;
		}
		
		if(ret == true) {
			fingerbook.setFingerbookId(fingerbookId);
		}
		
		return ret;
	}
	
	private static boolean fingerprintsSaveAllowed(Fingerbook fingerbook) throws IOException {
		
		boolean ret = false;
		
		//TODO:DEBUG
//		fingerbook.setTransactionId(createTransactionId(fingerbook.getFingerbookId()));
		
		if(fingerbook != null && fingerbook.getFingerbookId() != null && fingerbook.getTransactionId() != null) {
			
			long fingerbookId = fingerbook.getFingerbookId();
			String auxTransId = createTransactionId(fingerbookId);
			if(auxTransId.equals(fingerbook.getTransactionId())) {
				ret = fingerprintsSaveAllowedState(fingerbookId);
			}
		}
		return ret;
	}
	
//	private static boolean fingerprintsSaveAllowedState(long fingerbookId) throws IOException {
//		
//		byte[] groupIdB = Bytes.toBytes(fingerbookId);
//		boolean ret = HbaseManager.rowExists(TMP_TABLE_NAME, groupIdB);
//		return ret;
//	}
	
	private static boolean fingerprintsSaveAllowedState(long fingerbookId) throws IOException {
		
		String transactionId = createTransactionId(fingerbookId);
		
		byte[] transactionIdB = Bytes.toBytes(transactionId);
		boolean ret = HbaseManager.rowExists(TMP_TABLE_NAME, transactionIdB);
		return ret;
	}
	
	public static String createTicket(long fingerbookId) {
		String ticket = null;
		String tmpTicket = TICKET_KEY + String.valueOf(fingerbookId);
		try {
			ticket = (new Sha1()).getHash(tmpTicket);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ticket;
	}
	
	public static String getTicket(long fingerbookId) {
		String ticket = null;
		
		try {
			byte[] ticketB = HbaseManager.getValue(GROUP_TABLE_NAME, Bytes.toBytes(fingerbookId), TGROUP_INFO_FAMILY, Bytes.toBytes(COLUMN_TICKET));
			if(ticketB != null) {
				ticket = Bytes.toString(ticketB);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ticket;
	}
	
	public static long getFingerbookIdByTransactionId(String transactionId) {
		long fingerbookId = -1;
		
		try {
//			byte[] groupIdB = HbaseManager.getValue(TMP_TABLE_NAME, Bytes.toBytes(transactionId), TTMP_STATE_FAMILY, Bytes.toBytes(TTMP_COLUMN_GROUP_ID));
			byte[] groupIdB = HbaseManager.getValue(TMP_TABLE_NAME, Bytes.toBytes(transactionId), TTMP_INFO_FAMILY, Bytes.toBytes(TTMP_COLUMN_GROUP_ID));
			if(groupIdB != null) {
				fingerbookId = Bytes.toLong(groupIdB);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fingerbookId;
	}
	
	public static NavigableMap<byte[],NavigableMap<byte[],byte[]>> getTmpDataByTransactionId(String transactionId) {
		
		NavigableMap<byte[],NavigableMap<byte[],byte[]>> tmpData = null;
		
		try {
			
			tmpData = HbaseManager.getFullRowMap(TMP_TABLE_NAME, Bytes.toBytes(transactionId));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tmpData;
	}
	
	public static boolean isValidTicket(String ticket) throws IOException {
		
		boolean ret = false;
		
		if(ticket != null && !ticket.isEmpty()) {
			
			byte[] ticketB = Bytes.toBytes(ticket);
			ret = HbaseManager.rowExists(TICKET_TABLE_NAME, ticketB);
			
		}
		return ret;
	}
	
	public static String createTransactionId(long fingerbookId) {
		String transactionId = null;
		String tmpTransactionId = TRANSACTION_ID_KEY + String.valueOf(fingerbookId);
		try {
			transactionId = (new Sha1()).getHash(tmpTransactionId);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transactionId;
	}
}