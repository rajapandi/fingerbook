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
	
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("ID: " + this.fingerbookId + "\n");
		ret.append("Date: " + this.stamp + "\n");
	//	ret.append("Username: " + this.userInfo.getUser() + "\n");
	//	ret.append("Email: " + this.userInfo.getMail() + "\n");
		for (FileInfo fi : this.fingerPrints.getFiles()) {
//			//ret.append("File: ");
//			//ret.append(this.fingerbookId);
			ret.append(" \nHash: ");
			ret.append(fi.getShaHash());
//			//ret.append(" \nSize: ");
//			//ret.append(fi.getSizeInBytes());
			ret.append(" \n\n");
		}
		return ret.toString();
	}
	
}
