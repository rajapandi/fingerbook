package com.fingerbook;

import java.io.File;

public class Scanner {

	static ClientParams params;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main( String[] args ) throws Exception {

		params = new ClientParams(args);
		System.out.println(params.toString());
		
		File actual = new File(params.dir);
		
		System.out.println("\nFiles Found:");
        for( File f : actual.listFiles()) {
        	if (f.isFile())
        		System.out.println( f.getName() );
        }
        
        //TODO: Implement FastMD5 Library (much faster than Java native md5
	}
}
