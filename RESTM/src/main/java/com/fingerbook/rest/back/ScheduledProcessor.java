package com.fingerbook.rest.back;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fingerbook.persistencehbase.PersistentFingerbook;
	
public class ScheduledProcessor {

	protected final Log logger = LogFactory.getLog(getClass());
	
    public void process() {
    	// Transactions timeout after 5 minutes
    	logger.info("Running scheduled transaction cleaner..");
//        PersistentFingerbook.cleanExpired(1*60*1000);
    }
}
