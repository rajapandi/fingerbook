package com.fingerbook.models.transfer;

import com.fingerbook.models.pig.PigScript;

public class PigScriptFeed extends BaseFeed {

	private static final long serialVersionUID = 200428273395499700L;
	
	protected PigScript pigScript;

	public PigScriptFeed() {
		
	}

	public PigScript getPigScript() {
		return pigScript;
	}

	public void setPigScript(PigScript pigScript) {
		this.pigScript = pigScript;
	}

}
