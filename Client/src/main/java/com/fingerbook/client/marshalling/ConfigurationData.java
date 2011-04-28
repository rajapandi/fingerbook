package com.fingerbook.client.marshalling;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/* XML struct class */
@Root
public class ConfigurationData {
	/* Login Info */
	@Element
	private String auth;
	@Element (required=false)
	private String user;
	@Element (required=false)
	private String pass;
	
	/* Semi-Authenticated Info */
	@Element
	private String semiAuth;
	@Element (required=false)
	private String ticket;
	
	/* Paths Info */
	@ElementList (required=false, entry="path")
	private List<String> paths;
	@Element
	private String recursive;
	
	/* Transaction ID */
	@Element
	private String transID;
	
	public ConfigurationData() {
		/* Set default values */
		auth = "false";
		user = "";
		pass = "";
		semiAuth = "false";
		ticket = "";
		paths = new ArrayList<String>();
		recursive = "false";
		transID = "";
	}
	
	/* Getters and Setters */
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getAuth() {
		return auth;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUser() {
		return user;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getPass() {
		return pass;
	}
	public void setSemiAuth(String semiAuth) {
		this.semiAuth = semiAuth;
	}
	public String getSemiAuth() {
		return semiAuth;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getTicket() {
		return ticket;
	}
	public void setRecursive(String recursive) {
		this.recursive = recursive;
	}
	public String getRecursive() {
		return recursive;
	}
	public void setPaths(List<String> paths) {
		this.paths = paths;
	}
	public List<String> getPaths() {
		return paths;
	}
	public void setTransId(String id) {
		transID = id;
	}	
	public String getTransId() {
		return transID;
	}
}
