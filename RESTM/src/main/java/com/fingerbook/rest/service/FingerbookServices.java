package com.fingerbook.rest.service;

import java.util.List;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.models.transfer.FingerbookList;
import com.fingerbook.persistencehbase.PersistentFingerbook;


public class FingerbookServices {
	protected final Log logger = LogFactory.getLog(getClass());

	public Vector<Fingerbook> getFingerbooksWithHash(String hash) {	
		if(hash != null) {
			Vector<Fingerbook> fingerbooks = PersistentFingerbook.getFingerbookByHash(hash);
			return fingerbooks;
		} else {
			return null;
		}
	}
	
	public Vector<Fingerbook> getFingerbooksByTicket(String ticket) {	
		if(ticket != null) {
			Vector<Fingerbook> fingerbooks = PersistentFingerbook.getFingerbookByTicket(ticket);
			return fingerbooks;
		} else {
			return null;
		}
	}
	
	public Vector<Fingerbook> getFingerbooksByTicket(String ticket, int limit, int offset) {	
		if(ticket != null) {
//			Vector<Fingerbook> fingerbooks = PersistentFingerbook.getFingerbookByTicket(ticket);
			Vector<Fingerbook> fingerbooks = PersistentFingerbook.getFingerbooksByTicketPag(ticket, limit, offset);
			return fingerbooks;
		} else {
			return null;
		}
	}
	
	public FingerbookList getFingerbookListByUser(String user, int limit, int offset) {	
		if(user != null) {
			FingerbookList fingerbookList = PersistentFingerbook.getFingerbookListByUserPag(user, limit, offset);
			return fingerbookList;
		} else {
			return null;
		}
	}
	
	public Vector<Fingerbook> getFingerbooksByUser(String user, int limit, int offset) {	
		if(user != null) {
			Vector<Fingerbook> fingerbooks = PersistentFingerbook.getFingerbooksByUserPag(user, limit, offset);
			return fingerbooks;
		} else {
			return null;
		}
	}
	
	public Vector<Fingerbook> getFingerbooksByUser(String user) {	
		if(user != null) {
			Vector<Fingerbook> fingerbooks = PersistentFingerbook.getFingerbookByUser(user);
			return fingerbooks;
		} else {
			return null;
		}
	}
	
	public Fingerbook getFingerbookById(Long id) {	
		if(id != null) {
			
			Fingerbook fingerbook = new Fingerbook();
			fingerbook.setFingerbookId(id);
			PersistentFingerbook pf = new PersistentFingerbook(fingerbook);
			fingerbook = pf.loadMe(true);
			return fingerbook;
		} else {
			return null;
		}
	}
	
	public Fingerbook getFingerbookById(Long id, int size, int offset ) {	
		if(id != null) {
			
			Fingerbook fingerbook = new Fingerbook();
			fingerbook.setFingerbookId(id);
			PersistentFingerbook pf = new PersistentFingerbook(fingerbook);
			fingerbook = pf.loadMe(true, size, offset);
			return fingerbook;
		} else {
			return null;
		}
	}

	public List<Fingerprints> getFingerprintsWithHash(String hash) {
		
		
	// TODO Robot service
		return null;
	}

	public String generateTicket() {
		
		// TODO: Use hash(something)
		/*
		try {
			Random rand = new Random();
			MessageDigest md = MessageDigest.getInstance("SHA1");
			String output = null;
			byte[] buffer = new byte[8192];
			Hex h = new Hex(CharEncoding.ISO_8859_1);
			
			buffer = rand.nextDouble();
			md.update(buffer, 0, read);
			
			byte[] md5sum = md.digest();
			//BigInteger bigInt = new BigInteger(1, md5sum);
			//output = bigInt.toString(16);
			output = new String(h.encodeHex(md5sum, true));	
			return output;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}*/
		
		return "FSGFGS32432fDSG";
	}

	/*public Fingerbook getFingerbook(Fingerprints fingerprints) {
		Fingerbook fingerbook = new Fingerbook();
		fingerbook.setFingerPrints(fingerprints);
		return fingerbook;
	}	*/
}
