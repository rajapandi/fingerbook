package com.fingerbook.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

public class RestClient {

	private HttpClient client;

	public RestClient() {
		super();
		this.client = new HttpClient();
	}

	public void postXML(File f, String url) {

		PostMethod post = new PostMethod(url);

		RequestEntity entity = new FileRequestEntity(f,"text/xml; charset=ISO-8859-1");
		post.setRequestEntity(entity);

		try {
			int result = this.client.executeMethod(post);

			System.out.println("Response status code: " + result);
			System.out.println("Response body: ");
			System.out.println(post.getResponseBodyAsString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
	}

	public File getXML(String url) {
		String responseBody = null;
		GetMethod get = new GetMethod(url);
		get.setFollowRedirects(true);
		File f = new File("get.xml");

		try {
			int iGetResultCode = client.executeMethod(get);
			responseBody = get.getResponseBodyAsString();
			System.out.println(responseBody);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			get.releaseConnection();
		}

		if (responseBody != null) {
			InputStream in = new ByteArrayInputStream(responseBody.getBytes());
			OutputStream out = null;
			try {
				 out = new FileOutputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
			
			byte buf[] = new byte[1024];
		    int len;
		    try {
		    	while((len=in.read(buf))>0) {
		    		out.write(buf,0,len);
		    	}
		    	out.close();
		    	in.close();
		    } catch (IOException e){
		    	e.printStackTrace();
		    	return null;
		    }
		}
		
		return f;

	}

}
