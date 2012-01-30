package com.fingerbook.models.transfer;

import java.io.Serializable;
import java.util.Map;

import com.fingerbook.models.Fingerbook;

public class SimilaritiesFeed extends BaseFeed implements Serializable {

	private static final long serialVersionUID = -3404471724883315305L;
	
	protected Map<Fingerbook, Double> similsFbs;

	public SimilaritiesFeed() {
		super();
	}

	public SimilaritiesFeed(Map<Fingerbook, Double> similsFbs) {
		super();
		this.similsFbs = similsFbs;
	}

	public SimilaritiesFeed(int totalresults, int limit, int offset) {
		super(totalresults, limit, offset);
		// TODO Auto-generated constructor stub
	}

	public Map<Fingerbook, Double> getSimilsFbs() {
		return similsFbs;
	}

	public void setSimilsFbs(Map<Fingerbook, Double> similsFbs) {
		this.similsFbs = similsFbs;
	}
	
	

}
