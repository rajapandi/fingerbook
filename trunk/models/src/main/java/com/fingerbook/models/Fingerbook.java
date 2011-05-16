package com.fingerbook.models;

import java.io.Serializable;
import java.util.Vector;

public class Fingerbook implements Serializable {
	private static final long serialVersionUID = 3826603447160435399L;

	public enum STATE {
		START, CONTENT, FINISH, TIMEOUT_ERROR, RESUME
	}
	protected Long fingerbookId;
	protected Fingerprints fingerPrints;
	protected UserInfo userInfo;
	protected long stamp;
	protected STATE state;
	protected String transactionId;
	protected Vector<String> tags;
	protected String comment;
	
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
	
	public STATE getState() {
		return state;
	}
	public void setState(STATE state) {
		this.state = state;
	}
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("ID: " + this.fingerbookId + "\n");
		ret.append("Date: " + this.stamp + "\n");
		if(this.userInfo != null) {
			ret.append("Username: " + this.userInfo.getUser() + "\n");
		}
		if(this.comment != null) {
			ret.append("Comment: " + this.comment + "\n");
		}

		if(this.tags != null) {
			ret.append("Tags: {");
			boolean first = true;
			for (String tag : this.tags) {
				if(!first) {
					ret.append(", ");
				}
				ret.append(tag);
				first = false;
			}
			ret.append("}\n");
		}
		if(this.fingerPrints != null) {
			for (FileInfo fi : this.fingerPrints.getFiles()) {
				ret.append("\nFile: ");
				ret.append(fi.getName());
				ret.append(" \nHash: ");
				ret.append(fi.getShaHash());
				ret.append(" \nSize: ");
				ret.append(fi.getSizeInBytes());
				ret.append(" \n\n");
			}
		}
		return ret.toString();
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public Vector<String> getTags() {
		return tags;
	}
	public void setTags(Vector<String> tags) {
		this.tags = tags;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
