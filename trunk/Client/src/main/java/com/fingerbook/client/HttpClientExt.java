package com.fingerbook.client;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

public class HttpClientExt extends HttpClient {

    public void init() {
    	UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("scott","wombat");
		this.getState().setCredentials(new AuthScope("localhost", 8080, AuthScope.ANY_REALM), credentials);
    }
}
