package com.fingerbook.rest.service;

import com.fingerbook.rest.domain.Fingerbook;
import junit.framework.TestCase;

public class FingerbookServicesTest extends TestCase {

	public void testGetFingerbook() throws Exception {
		FingerbookServices controller = new FingerbookServices();
		Fingerbook fingerbook = controller.getFingerbook(new Long(1));
		if (fingerbook != null) {
			assertNotNull(fingerbook.getFingerbookId());
			assertNotNull(fingerbook.getFingerPrints());
			assertNotNull(fingerbook.getStamp());
			assertNotNull(fingerbook.getUserInfo());
			assertEquals(new Long(1), fingerbook.getFingerbookId());
		}
	}
	
	public void testEmptyGetFingerbook() throws Exception {
		FingerbookServices controller = new FingerbookServices();
		assertNull(controller.getFingerbook(new Long(-1)));
	}
	
	public void testGetFingerprintsWithHash() throws Exception {
	}
	
	public void testEmptyGetFingerprintsWithHash() throws Exception {
		FingerbookServices controller = new FingerbookServices();
		assertNull(controller.getFingerprintsWithHash(null));
	}
}
