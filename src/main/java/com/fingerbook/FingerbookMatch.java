package com.fingerbook;

import java.util.Set;

import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerbook.STATE;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.models.UserInfo;

public class FingerbookMatch {
	
	protected Fingerbook fingerbook;
	protected Double match;

	
	public FingerbookMatch() {
		
	}


	public FingerbookMatch(Fingerbook fingerbook, Double match) {
		
		this.fingerbook = fingerbook;
		this.match = match;
	}


	public Long getFingerbookId() {
		return fingerbook.getFingerbookId();
	}

	
	public void setFingerbookId(Long fingerbookId) {
		
		fingerbook.setFingerbookId(fingerbookId);
	}

	
	public Fingerprints getFingerPrints() {
		return fingerbook.getFingerPrints();
	}

	
	public void setFingerPrints(Fingerprints fingerPrints) {
		
		fingerbook.setFingerPrints(fingerPrints);
	}

	
	public UserInfo getUserInfo() {
		return fingerbook.getUserInfo();
	}

	
	public void setUserInfo(UserInfo userInfo) {
		
		fingerbook.setUserInfo(userInfo);
	}

	
	public long getStamp() {
		return fingerbook.getStamp();
	}

	
	public void setStamp(long stamp) {
		
		fingerbook.setStamp(stamp);
	}

	
	public STATE getState() {
		return fingerbook.getState();
	}

	
	public void setState(STATE state) {
		
		fingerbook.setState(state);
	}

	
	public String toString() {
		return fingerbook.toString();
	}

	
	public String getTransactionId() {
		return fingerbook.getTransactionId();
	}

	
	public void setTransactionId(String transactionId) {
		
		fingerbook.setTransactionId(transactionId);
	}

	
	public Set<String> getTags() {
		return fingerbook.getTags();
	}

	
	public void setTags(Set<String> tags) {
		
		fingerbook.setTags(tags);
	}

	
	public String getComment() {
		return fingerbook.getComment();
	}

	
	public void setComment(String comment) {
		
		fingerbook.setComment(comment);
	}

	
	public String getStampDate() {
		
		return fingerbook.getStampDate();
	}

	public Double getMatch() {
		return match;
	}

	public void setMatch(Double match) {
		this.match = match;
	}
	
	

}
