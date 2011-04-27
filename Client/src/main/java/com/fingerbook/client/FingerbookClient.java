package com.fingerbook.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fingerbook.client.gui.Front;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerbook.STATE;
import com.fingerbook.models.Response;
import com.fingerbook.models.UserInfo;

public class FingerbookClient {
	@Autowired
	protected RestTemplate restTemplate;
	private UserInfo ui; 
	private String baseUrl;
	private String ticket;
	private String user;
	
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
		return restTemplate.getForObject( baseUrl + "fingerbooks/" + getAuthM() + "/" + hash, List.class );
	}
	
	public Response postHashes(Fingerbook fb) throws RestClientException {
		fb.setUserInfo(ui);
		return restTemplate.postForObject(this.baseUrl + "fingerbooks/" + getAuthM() + "/put", fb, Response.class);
	}

	public Response startHashTransaction(String ticket) {
		Fingerbook fb = setFB(STATE.START);

		return restTemplate.postForObject(this.baseUrl + "fingerbooks/" + getAuthM() + "/put", fb, Response.class);
	}
	
	public Response resumeHashTransaction(String ticket) {
		Fingerbook fb = setFB(STATE.RESUME);
		
		return restTemplate.postForObject(this.baseUrl + "fingerbooks/" + getAuthM() + "/put", fb, Response.class);
	}
	
	private Fingerbook setFB(STATE start) {
		this.ticket = new String(ticket);
		user = new String(Front.getConfiguration().get("user"));
		
		Fingerbook fb = new Fingerbook();
		ui = new UserInfo();
		ui.setUser(user);
		ui.setTicket(this.ticket);
		fb.setState(STATE.START);
		fb.setUserInfo(ui);
		return fb;
	}
	
	private String getAuthM() {
		if (Front.getConfiguration().get("authM").equals("auth"))
			return "authenticated";
		if (Front.getConfiguration().get("authM").equals("semi"))
			return "semiauthenticated";
		return "anonymous";
	}
}
