package com.fingerbook.client;

import com.fingerbook.models.Response;

public class ResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private Response response;
	
	public ResponseException(String msg, Response resp) {
		super(msg);
		this.response = resp;
	}
	
	public Response getResponse() {
		return this.response;
	}
}
