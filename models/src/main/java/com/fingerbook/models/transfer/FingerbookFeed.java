package com.fingerbook.models.transfer;

import com.fingerbook.models.Fingerbook;

public class FingerbookFeed extends BaseFeed {

	private static final long serialVersionUID = -4557681972629408975L;
	
	protected Fingerbook fingerbook;
	protected FingerprintsFeed fingerprintsFeed;
	protected SimilaritiesFeed similaritiesFeed;

	public FingerbookFeed() {
		// TODO Auto-generated constructor stub
	}

	public FingerbookFeed(int totalresults, int limit, int offset) {
		super(totalresults, limit, offset);
		// TODO Auto-generated constructor stub
	}

	public Fingerbook getFingerbook() {
		return fingerbook;
	}

	public void setFingerbook(Fingerbook fingerbook) {
		this.fingerbook = fingerbook;
	}

	public FingerprintsFeed getFingerprintsFeed() {
		
		if(fingerprintsFeed != null) {
			
			if(fingerprintsFeed.getFingerPrints() == null) {
				if(fingerbook != null) {
					fingerprintsFeed.setFingerPrints(fingerbook.getFingerPrints());
				}
			}
		}
		
		return fingerprintsFeed;
	}

	public void setFingerprintsFeed(FingerprintsFeed fingerprintsFeed) {
		this.fingerprintsFeed = fingerprintsFeed;
	}

	public SimilaritiesFeed getSimilaritiesFeed() {
		return similaritiesFeed;
	}

	public void setSimilaritiesFeed(SimilaritiesFeed similaritiesFeed) {
		this.similaritiesFeed = similaritiesFeed;
	}

}
