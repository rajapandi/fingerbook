package com.fingerbook.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fingerbook.models.FileInfo;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.models.UserInfo;
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

	public List<Fingerprints> getFingerprintsWithHash(String hash) {
		
		
	// TODO Robot service
		return null;
	}

	public Fingerbook getFingerbook(Fingerprints fingerprints) {
		Fingerbook fingerbook = new Fingerbook();
		fingerbook.setFingerPrints(fingerprints);
		return fingerbook;
	}	
}
