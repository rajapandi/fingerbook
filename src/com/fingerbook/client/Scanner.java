package com.fingerbook.client;

import java.io.DataOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fingerbook.client.FileHashCalculator.Method;

public class Scanner {

	static ClientParams params;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main( String[] args ) throws Exception {

		params = new ClientParams(args);
		System.out.println(params.toString());
		
		FileHashCalculator fhc = new FileHashCalculator(Method.SHA1);
		
        scanDirectory(params.dir, fhc);
    /*    
        URL url = new URL("http://...");  
        HttpURLConnection con = (HttpURLConnection) url.openConnection();  
        con.setRequestMethod("POST");  
        
        con.setRequestProperty("Content-Type", "text/xml; charset=ISO-8859-1");
        con.setUseCaches (false);
        con.setDoInput(true);
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream (con.getOutputStream ());
        
        wr.writeBytes (urlParameters);
        wr.flush ();
        wr.close ();
      */  
        RestClient rc = new RestClient();
       
        File f = rc.getXML("http://www.wergehthin.de/xml/User/");
        rc.postXML(f, "http://www.wergehthin.de/xml/User/");
        System.out.println(f.toString());
        //TODO: Implement FastMD5 Library (much faster than Java native md5
	}
	
	public static void scanDirectory(String dir, FileHashCalculator fhc) {
		File actual = new File(dir);
		
		System.out.println("\nFiles Found:");
        for( File f : actual.listFiles()) {
        	if (f.isFile()) {
        		System.out.println("Filename: " +  f.getName() + "--------- Hash: " + fhc.getFileHash(f));
        	}
        }
	}
}
