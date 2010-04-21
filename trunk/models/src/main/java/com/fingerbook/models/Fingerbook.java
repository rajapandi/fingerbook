package com.fingerbook.models;

import java.io.Serializable;

public class Fingerbook implements Serializable {
	private static final long serialVersionUID = 3826603447160435399L;

	protected Long fingerbookId;
	protected Fingerprints fingerPrints;
	protected UserInfo userInfo;
	protected long stamp;
	
	public Long getFingerbookId() {
		return fingerbookId;
	}
	public void setFingerbookId(Long fingerbookId) {
		this.fingerbookId = fingerbookId;
	}
	public Fingerprints getFingerPrints() {
		return fingerPrints;
	}
	public void setFingerPrints(Fingerprints fingerPrints) {
		this.fingerPrints = fingerPrints;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public long getStamp() {
		return stamp;
	}
	public void setStamp(long stamp) {
		this.stamp = stamp;
	}
	
	
}
