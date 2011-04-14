package com.fingerbook.rest.service;

import java.util.Vector;

import com.fingerbook.models.Fingerbook;

import junit.framework.TestCase;

public class FingerbookServicesTest extends TestCase {
	
	private FingerbookServices services= new FingerbookServices();

	public void testGetFingerbookWithHash() throws Exception {
		//Vector<Fingerbook> fingerbooks = services.getFingerbooksWithHash("hashtest");
		//assertNotNull(fingerbooks);
		//assertEquals(new Long(1), fingerbooks.getFingerbookId());
	
	}
	
	public void testEmptyGetFingerbook() throws Exception {
//		FingerbookServices controller = new FingerbookServices();
//		assertNull(controller.getFingerbook(new Long(-1)));
	}
	
	public void testGetFingerprintsWithHash() throws Exception {
	}
	
	public void testEmptyGetFingerprintsWithHash() throws Exception {
		//assertNull(services.getFingerprintsWithHash(null));
	}
	
}
