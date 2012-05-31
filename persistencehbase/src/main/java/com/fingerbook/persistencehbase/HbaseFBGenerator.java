package com.fingerbook.persistencehbase;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.hadoop.hbase.util.Bytes;

import com.fingerbook.models.Auth;
import com.fingerbook.models.FileInfo;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.persistencehbase.hbase.TransHTable;

public class HbaseFBGenerator {
	
	public static void insertFbs(Vector<Fingerbook> fbs) {
		
		System.out.println("INSERT START");
		
		if(fbs != null) {
			
			int i = 0;
			
			for(Fingerbook auxFb: fbs) {
				
				i++;
				
				System.out.println("INSERT IDX: " + i);
				
				Long fbIdStart = saveMe(auxFb, Auth.AUTH_AUTHENTICATED);
				
				System.out.println("INSERT fbIdStart " + fbIdStart);
				
//				Long fbIdAdd = PersistentFingerbook.saveFingerprints(auxFb);
				
				Long fbIdCom = commitSave(auxFb);
				
				System.out.println("INSERT fbIdCom " + fbIdCom);
				
			}
			
		}
		
		System.out.println("INSERT END");
	}


	public static long saveMe(Fingerbook fingerbook, int authMethod) {
		
		try {
			
			if(fingerbook == null) {
				return -2;
			}
			
			long fingerbookId = PersistentFingerbook.insertGroupInfo(fingerbook, authMethod);
			fingerbook.setFingerbookId(fingerbookId);
		
			if(fingerbookId < 0) {
				return -1;
			}
			
			String transactionId = PersistentFingerbook.createTransactionId(fingerbookId);
			fingerbook.setTransactionId(transactionId);
			
//			createTmpState(fingerbook, authMethod);
			
			Fingerprints fingerPrints = fingerbook.getFingerPrints();
			
			if(fingerPrints != null) {
				saveFingerprints(fingerbook);
			}
			
			return fingerbookId;
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
	}
	
	public static long saveFingerprints(Fingerbook fingerbook) {
		
		if(fingerbook == null) {
			return -1;
		}
		
		long fingerbookId = fingerbook.getFingerbookId();
		Fingerprints fingerprints = fingerbook.getFingerPrints();
		
		if(fingerprints == null) {
			return -2;
		}
		try {
			
//			if(!fingerprintsSaveAllowed(fingerbook)) {
//				return -3;
//			}
			
			TransHTable groupTable = new TransHTable(PersistentFingerbook.GROUP_TABLE_NAME);
			byte[] groupIdB = Bytes.toBytes(fingerbookId);
			List<FileInfo> files = fingerprints.getFiles();
			
			if(files == null) {
				return -4;
			}
			for(FileInfo fileInfo: files) {
			
				String shaHash = fileInfo.getShaHash();
				byte[] shaHashB = Bytes.toBytes(shaHash);
				
				groupTable.incrementColumnValue(groupIdB, Bytes.toBytes(PersistentFingerbook.TGROUP_FINGER_FAMILY), shaHashB, 1L);
				
			}
			groupTable.commitAndDestroy();
//			updateTmpState(fingerbookId);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
				return -5;
		}
		return fingerbookId;
	}
	
	public static long commitSave(Fingerbook fingerbook) {
		
//		try {
//			if(!fingerprintsSaveAllowed(fingerbook)) {
//				return -2;
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return -1;
//		}
		
		long fingerbookId = fingerbook.getFingerbookId();
		
		try {
			
			PersistentFingerbook.updateFingerbookTotalFiles(fingerbookId);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -3;
		}
		PersistentFingerbook.updateFingerTable(fingerbookId);
		
		PersistentFingerbook.addFingerbookToUser(fingerbookId);
		
//		deleteFingerbookFromTmp(fingerbookId);

		return fingerbookId;
	}

}
