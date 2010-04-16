package com.fingerbook.client.marshalling;

import org.simpleframework.xml.Element;

class UserInfo {
	@Element
	private String user;
	
	@Element
	private String mail;

	public UserInfo(String user, String mail) {
		this.user = user;
		this.mail = mail;
	}
	
	public String getUser() {
		return user;
	}

	public String getMail() {
		return mail;
	}
}
