package com.fingerbook.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Response;
import com.fingerbook.models.UserInfo;
import com.fingerbook.models.Fingerbook.STATE;

public class FingerbookClient {
	@Autowired
	protected RestTemplate restTemplate;
	private String baseUrl;
	
	public FingerbookClient() {
	}
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public FingerbookClient(String Url) {
		super();
		this.baseUrl = Url;
	}
	
	@SuppressWarnings( "unchecked" )
	public List<Fingerbook> getGroups(String hash)
	{
		return restTemplate.getForObject( baseUrl + "fingerbooks/" + hash, List.class );
	}
	
	public Response postHashes(Fingerbook fb) throws RestClientException {
		return restTemplate.postForObject(this.baseUrl + "fingerprints/new", fb, Response.class);
	}

	public Response startHashTransaction(UserInfo userInfo) {
		Fingerbook fb = new Fingerbook();
		fb.setState(STATE.START);
		fb.setUserInfo(userInfo);
		return restTemplate.postForObject(this.baseUrl + "fingerprints/new", fb, Response.class);
	}
	
	
//	public FileInfo getFileInfo(String hash) {
//		return restTemplate.getForObject(this.baseUrl + "knownfigerprint/" + hash , FileInfo.class);
//	}
	
	
}
