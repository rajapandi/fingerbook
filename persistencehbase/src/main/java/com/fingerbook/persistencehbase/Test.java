package com.fingerbook.persistencehbase;

import java.io.IOException;

import com.fingerbook.models.Fingerbook;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	public static void main(String[] args) throws IOException {
		
		PersistentFingerbook.createFingerTables();
//		
//		FingerGroup g1 = new FingerGroup(1, System.currentTimeMillis());
		String fp1 = "aa11aa11aa";
//		FingerElem f1 = new FingerElem(fp1, g1);
		
//		FingerGroup g2 = new FingerGroup(2, System.currentTimeMillis());
//		FingerElem f2 = new FingerElem(fp1, g2);
		
//		Fingerbook fb1 = new FingerbookServices().getFingerbook(new Long(15));
//		PersistentFingerbook pfb1 = new PersistentFingerbook(fb1);
//		pfb1.saveMe();
		
//		try {
//			PersistentFinger.putFinger(f2);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		long maxGroupId = PersistentFingerbook.getNextGroupId();
		System.out.println("MAX GROUPID: " + maxGroupId);
		
//		Vector<FingerGroup> groups = PersistentFinger.getGroupsByFingerPrint(fp1);
//		Vector<FingerElem> fingers = PersistentFinger.getFingerElemsByGroupId(1);
//		
//		for(FingerGroup group: groups) {
//			System.out.println("GROUP: " + group.getGroupId());
//		}
//		
//		for(FingerElem finger: fingers) {
//			System.out.println("FINGER: " + finger.getFingerPrint());
//		}
		
//		Vector<byte[]> scanValues = HbaseManager.scanRows(FingerPersistence.TABLE_NAME, null, null);
//		
//		for(byte[] scanVal:scanValues) {
//			
//			try {
//				FingerRow auxFingerRow = FingerManager.readFingerRow(scanVal);
//				System.out.println("ROW: " + auxFingerRow.toString());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
	
//	public static void main(String[] args) throws IOException {
//		
//		String tableName = "table1";
//		String rowId = null;
//		String columnFamily = "f1";
//		String columnName = null;
//		String value = null;
//		
//		HBaseConfiguration conf = new HBaseConfiguration();
//		ClientZKWatcher zkWatcher = new ClientZKWatcher(conf);
//		zkWatcher.getZooKeeperWrapper();

//		for(int i = 1; i < 3; i++) {
//			for(int j = 1; j < 3; j++) {
//				
//				rowId = String.valueOf(i);
//				columnName = String.valueOf(j);
//				value = "value" + rowId + columnName;
//				HbaseManager.putValue(tableName, rowId, columnFamily, columnName, value);
//			}
//		}
		
//		for(int i = 1; i < 3; i++) {
//			for(int j = 1; j < 3; j++) {
//				
//				rowId = String.valueOf(i);
//				columnName = String.valueOf(j);
//				String retVal = HbaseManager.getValue(tableName, rowId, columnFamily, columnName);
//				System.out.println("ROW: " + rowId + " COLUMN: " + columnFamily + ":" + columnName + " VALUE: " + retVal);
//			}
//		}
		
//		Vector<String> scanValues = HbaseManager.scanTable(tableName, null, null);
//		
//		for(String scanVal:scanValues) {
//			System.out.println(scanVal);
//		}
		
//		Vector<String> rowsFiltered = HbaseManager.filterRows(tableName, columnFamily, "1", "value21");
//		
//		for(String auxRow:rowsFiltered) {
//			System.out.println(auxRow);
//		}
//	}

}
