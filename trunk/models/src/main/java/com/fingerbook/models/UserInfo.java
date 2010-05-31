package com.fingerbook.models;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = -1196485527525649224L;

	private Long userInfoId;
	private String user;
	private String mail;
	protected String ticket;
	
	public Long getUserInfoId() {
		return userInfoId;
	}
	public void setUserInfoId(Long userInfoId) {
		this.userInfoId = userInfoId;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
}
