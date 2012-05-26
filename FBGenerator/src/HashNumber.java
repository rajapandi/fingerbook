

import java.security.MessageDigest;

public class HashNumber {

	public static String encode(Integer myInt) throws Exception {
		// Compute digest
		String plaintext = myInt.toString();
		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		byte[] digest = sha1.digest((plaintext).getBytes());
		
		return bytes2String(digest);
	}
	
	private static String bytes2String(byte[] bytes) {
	   StringBuilder string = new StringBuilder();
	   for (byte b : bytes) {
	       String hexString = Integer.toHexString(0x00FF & b);
	       string.append(hexString.length() == 1 ? "0" + hexString : hexString);
	   }
	   return string.toString();
	}
}