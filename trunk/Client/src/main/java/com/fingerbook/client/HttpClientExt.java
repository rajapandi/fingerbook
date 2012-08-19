package com.fingerbook.client;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

public class HttpClientExt extends HttpClient {
	public void setCredentials(String user, String pass) {
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, pass);
		this.getState().setCredentials(new AuthScope(AuthScope.ANY), credentials);
	}
}
