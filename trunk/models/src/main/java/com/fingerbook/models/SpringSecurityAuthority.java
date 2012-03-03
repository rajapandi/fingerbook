package com.fingerbook.models;

import java.io.Serializable;

public class SpringSecurityAuthority implements Serializable {
	private static final long serialVersionUID = 5422718074787117629L;

	private String username;
	private String authority;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
