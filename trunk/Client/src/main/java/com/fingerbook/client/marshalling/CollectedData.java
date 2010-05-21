package com.fingerbook.client.marshalling;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.fingerbook.client.FileHashCalculator;
import com.fingerbook.client.FileHashCalculator.Method;

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

	@ElementList
	private List<FileInfo> fingerprints;
	
	@Element
	private UserInfo userInfo;

	public CollectedData(List<File> fileList) {
		FileHashCalculator fhc = new FileHashCalculator(Method.SHA1);
		String name;
		String hash;
		Long size;
		
		fingerprints = new ArrayList<FileInfo>();
		
		System.out.println("\nFiles Found:");
		for (File f : fileList) {
			name = f.getName();
			hash = fhc.getFileHash(f);
			size = f.length();
			fingerprints.add(new FileInfo(name, hash, size));
			
			System.out.println("Filename: " + f.getName()
					+ "--------- Hash: " + fhc.getFileHash(f));
		}
		userInfo = new UserInfo(user, mail);
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}	
}