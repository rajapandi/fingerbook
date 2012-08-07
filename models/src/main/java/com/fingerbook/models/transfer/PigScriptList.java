package com.fingerbook.models.transfer;

import java.io.Serializable;
import java.util.Vector;

import com.fingerbook.models.pig.PigScript;

public class PigScriptList extends BaseFeed implements Serializable {

	private static final long serialVersionUID = -7208076616675890729L;
	
	protected Vector<PigScript> pigScripts;

	public PigScriptList() {
		
	}

	public Vector<PigScript> getPigScripts() {
		return pigScripts;
	}

	public void setPigScripts(Vector<PigScript> pigScripts) {
		this.pigScripts = pigScripts;
	}

}
