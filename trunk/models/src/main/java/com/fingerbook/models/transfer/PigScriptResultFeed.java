package com.fingerbook.models.transfer;

import com.fingerbook.models.pig.PigScriptResult;

public class PigScriptResultFeed extends BaseFeed {

	private static final long serialVersionUID = 4817172802007743265L;
	
	protected PigScriptResult scriptResult;

	public PigScriptResultFeed() {
		
	}

	public PigScriptResult getScriptResult() {
		return scriptResult;
	}

	public void setScriptResult(PigScriptResult scriptResult) {
		this.scriptResult = scriptResult;
	}

}
