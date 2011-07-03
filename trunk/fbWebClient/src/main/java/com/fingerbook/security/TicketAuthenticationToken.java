package com.fingerbook.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class TicketAuthenticationToken extends UsernamePasswordAuthenticationToken {

	public TicketAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
		// TODO Auto-generated constructor stub
	}

}
