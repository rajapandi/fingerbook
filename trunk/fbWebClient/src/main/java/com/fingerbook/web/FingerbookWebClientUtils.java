package com.fingerbook.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.codec.Base64;

public class FingerbookWebClientUtils {

	public static StringBuffer makeBasicPostRequest(String urlString, String parameters, String username, String password)  throws
		EmptyResultDataAccessException, BadCredentialsException, Exception{
		
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		
		// Basic Authentication: Setting Auth Cred
        conn.setRequestProperty("Authorization", "Basic " +
        		new String(Base64.encode(new String(username + ":" + password).getBytes())));    
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
		conn.setRequestProperty("Content-Language", "en-US"); 

		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(true);
		
		//Send request
	    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
	    wr.writeBytes(parameters);
	    wr.flush();
	    wr.close();
	      
		// Get the response
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		
		return sb;
	}
}
