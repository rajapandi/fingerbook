package com.fingerbook.models.transfer;

import java.io.Serializable;

import com.fingerbook.models.Fingerprints;

public class FingerprintsFeed extends BaseFeed implements Serializable {

	private static final long serialVersionUID = -7530243700871600980L;
	
	protected Fingerprints fingerPrints;

	public FingerprintsFeed() {
		
	}

	public Fingerprints getFingerPrints() {
		return fingerPrints;
	}

	public void setFingerPrints(Fingerprints fingerPrints) {
		this.fingerPrints = fingerPrints;
	}

}
