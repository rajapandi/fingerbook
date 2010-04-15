package com.fingerbook.client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileHashCalculator {

		private MessageDigest md;
		
		public enum Method {
			MD5, SHA1
		}
	
		public FileHashCalculator(Method m) {
			if(m == Method.SHA1) {
				try {
					this.md = MessageDigest.getInstance("SHA1");
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}				
			} else {
				try {
					this.md = MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}	
			}
			
		}
		
		
		
}
