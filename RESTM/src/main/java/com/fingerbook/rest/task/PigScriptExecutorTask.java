package com.fingerbook.rest.task;

import org.apache.commons.logging.Log;
import org.hibernate.SessionFactory;

import com.fingerbook.models.pig.PigScriptResult;
import com.fingerbook.rest.service.PigScriptServices;

public class PigScriptExecutorTask implements Runnable {
	
	private int scriptId;
	private SessionFactory sessionFactory;
	private Log logger;

	public PigScriptExecutorTask(int scriptId, SessionFactory sessionFactory, Log logger) {
		
		super();
		
		this.scriptId = scriptId;
		this.sessionFactory = sessionFactory;
		this.logger = logger;
	}

	public int getScriptId() {
		return scriptId;
	}

	public void setScriptId(int scriptId) {
		this.scriptId = scriptId;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Log getLogger() {
		return logger;
	}

	public void setLogger(Log logger) {
		this.logger = logger;
	}

	@Override
	public void run() {
		
		PigScriptResult scriptResult = null;
    	
		scriptResult = PigScriptServices.execPigScript(scriptId, sessionFactory, logger);
	}

}
