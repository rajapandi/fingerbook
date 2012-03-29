package com.fingerbook.persistencehbase;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import com.fingerbook.models.Fingerbook;

public class CreateFingerTables {
	
	public static void main(String[] args) throws IOException {
		
		long fingerbookId = 1L;
		
//		Map<Long, Double> simils = null;
//		simils = PersistentFingerbook.getSimilarities(fingerbookId, 0.45);
//		
//		for(Entry<Long, Double> entry: simils.entrySet()) {
//			
//			System.out.println(entry.getKey() + " --> " + entry.getValue());
//		}
		
		Map<Fingerbook, Double> similsFbs = null;
		
		similsFbs = PersistentFingerbook.getSimilarFbs(fingerbookId, 0.35, 2, 0);
		
		for(Entry<Fingerbook, Double> entry: similsFbs.entrySet()) {
			
			System.out.println(entry.getKey() + " --> " + entry.getValue());
		}
		
		
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void mainOld(String[] args) throws IOException {

//		PersistentFingerbook.createFingerTables();
		
		String hash = "7738ca5c0259e34fba0fd32939b517e6";
		
		//String ticket = "de6f29589e4186411c5e877a29fda3b56a97db95";
		
		System.out.println("Normal");
//		Fingerprints auxFingerPrints = PersistentFingerbook.loadFingerPrintsByFingerBook(2);
		
		Vector<Fingerbook> fingerbooks = PersistentFingerbook.getFingerbookByHash(hash);
//		Vector<Fingerbook> fingerbooks = PersistentFingerbook.getFingerbookByTicket(ticket);
		
		System.out.println(fingerbooks.toString());
		
//		if(auxFingerPrints != null) {
//			
//			List<FileInfo> auxFiles = auxFingerPrints.getFiles();
//			
//			if(auxFiles != null) {
//				
//				System.out.println(auxFingerPrints);
//				
//			}
//		}
		
		System.out.println("END NORMAL");
		
		System.out.println("Pagination");
		
		int nextOffset = 0;
		long lastFingerbookId = -1;
		boolean isFinished = false;
		
//		Fingerprints auxFingerPrintsPag = new Fingerprints();
//		int nextOffset = PersistentFingerbook.loadFingerPrintsByFingerBookPag(auxFingerPrintsPag, 2, 11, 2);
		
		while(!isFinished) {
			Fingerbook auxFingerbook = new Fingerbook();
			
			nextOffset = PersistentFingerbook.getNextFingerbookByHash(auxFingerbook, hash, lastFingerbookId, nextOffset);
//			nextOffset = PersistentFingerbook.getNextFingerbookByTicket(auxFingerbook, ticket, lastFingerbookId, nextOffset);
			
			lastFingerbookId = auxFingerbook.getFingerbookId().longValue();
			
			if(nextOffset >= 0) {
				System.out.println(auxFingerbook);
				System.out.println("NEXT OFFSET: " + nextOffset);
			}
			else {
				isFinished = true;
			}
		}
		
//		if(auxFingerPrintsPag != null) {
//			
//			List<FileInfo> auxFilesPag = auxFingerPrintsPag.getFiles();
//			
//			if(auxFilesPag != null) {
//				
//				System.out.println(auxFingerPrintsPag);
//				
//			}
//		}
		
		System.out.println("END Pagination");
		
	}

}
