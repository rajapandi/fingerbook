package com.fingerbook.models.transfer;

import com.fingerbook.models.CompositeFingerbook;

public class CompositeFingerbookFeed extends BaseFeed {

	private static final long serialVersionUID = -8378947014906803628L;

	protected CompositeFingerbook compositeFingerbook;
	protected FingerprintsFeed fingerprintsFeed;
	
	
	public CompositeFingerbookFeed() {
		
	}


	public CompositeFingerbookFeed(int totalresults, int limit, int offset) {
		super(totalresults, limit, offset);
	}


	public CompositeFingerbook getCompositeFingerbook() {
		return compositeFingerbook;
	}


	public void setCompositeFingerbook(CompositeFingerbook compositeFingerbook) {
		this.compositeFingerbook = compositeFingerbook;
	}


	public FingerprintsFeed getFingerprintsFeed() {
		
		if(fingerprintsFeed != null) {
			if(fingerprintsFeed.getFingerPrints() == null) {
				if(compositeFingerbook != null) {
					fingerprintsFeed.setFingerPrints(compositeFingerbook.getFingerPrints());
				}
			}
		}
		
		return fingerprintsFeed;
	}


	public void setFingerprintsFeed(FingerprintsFeed fingerprintsFeed) {
		this.fingerprintsFeed = fingerprintsFeed;
	}

}
