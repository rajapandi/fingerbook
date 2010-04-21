package com.fingerbook.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerprints;



@Component( "FingerprintsClient" )
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
	public List<Fingerprints> getGroups(String hash)
	{
		return restTemplate.getForObject( baseUrl + "fingerprints/" + hash, List.class );
	}
	
	public void postHashes(Fingerbook fb) {
		restTemplate.postForObject(this.baseUrl + "fingerprints/new", fb, Fingerbook.class);
	}

//	public FileInfo getFileInfo(String hash) {
//		return restTemplate.getForObject(this.baseUrl + "knownfigerprint/" + hash , FileInfo.class);
//	}
	
	
}
