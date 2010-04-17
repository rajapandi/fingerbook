package com.fingerbook.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Hex;


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
		
		@SuppressWarnings("static-access")
		public String getFileHash(File f) {
			
			InputStream is;
			try {
				is = new FileInputStream(f);
			} catch (FileNotFoundException e1) {
				//e1.printStackTrace();
				return null;
			}				
			String output = null;
			byte[] buffer = new byte[8192];
			Hex h = new Hex(CharEncoding.ISO_8859_1);
			
			int read = 0;
			try {
				while( (read = is.read(buffer)) > 0) {
					this.md.update(buffer, 0, read);
				}		
				byte[] md5sum = this.md.digest();
				//BigInteger bigInt = new BigInteger(1, md5sum);
				//output = bigInt.toString(16);
				output = new String(h.encodeHex(md5sum, true));
			}
			catch(IOException e) {
				throw new RuntimeException("Unable to process file for MD5", e);
			}
			finally {
				try {
					is.close();
				}
				catch(IOException e) {
					throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
				}
			}		
			
			return output;

		}
		
		
		
}
