package com.fingerbook.models.transfer;

import java.io.Serializable;
import java.util.Vector;

import com.fingerbook.models.pig.PigScriptResult;

public class PigScriptResultList extends BaseFeed implements Serializable {

	private static final long serialVersionUID = 6686062947329474461L;
	
	protected Vector<PigScriptResult> pigScriptResults;

	public PigScriptResultList() {
		
	}

	public Vector<PigScriptResult> getPigScriptResults() {
		return pigScriptResults;
	}

	public void setPigScriptResults(Vector<PigScriptResult> pigScriptResults) {
		this.pigScriptResults = pigScriptResults;
	}
	
}
