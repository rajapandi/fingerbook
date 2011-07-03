package com.fingerbook.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {

	public UserAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
		// TODO Auto-generated constructor stub
	}

}
