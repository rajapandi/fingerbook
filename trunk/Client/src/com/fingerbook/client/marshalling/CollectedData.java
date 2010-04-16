package com.fingerbook.client.marshalling;

import java.io.File;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "fingerbook")
public class CollectedData {
	public static String user;
	public static String mail;	
	
	public static void setUser(String user) {
		CollectedData.user = user;
	}

	public static void setMail(String mail) {
		CollectedData.mail = mail;
	}

	@Element
	private Fingerprints fingerprints;
	
	@Element
	private UserInfo userInfo;

	public CollectedData(List<File> fileList) {
		fingerprints = new Fingerprints(fileList);
		userInfo = new UserInfo(user, mail);
	}
	
	public Fingerprints getFingerprints(List<File> fileList) {
		return fingerprints;
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}	
}