package com.fingerbook.client;

import java.util.List;
import java.io.File;
import java.util.ArrayList;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.fingerbook.client.FileHashCalculator.Method;
import com.fingerbook.client.marshalling.CollectedData;

public class Scanner {

	static ClientParams params;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		params = new ClientParams(args);
		System.out.println(params.toString());

		FileHashCalculator fhc = new FileHashCalculator(Method.SHA1);

		scanDirectory(params.dir, fhc);
		/*
		 * URL url = new URL("http://..."); HttpURLConnection con =
		 * (HttpURLConnection) url.openConnection();
		 * con.setRequestMethod("POST");
		 * 
		 * con.setRequestProperty("Content-Type",
		 * "text/xml; charset=ISO-8859-1"); con.setUseCaches (false);
		 * con.setDoInput(true); con.setDoOutput(true); DataOutputStream wr =
		 * new DataOutputStream (con.getOutputStream ());
		 * 
		 * wr.writeBytes (urlParameters); wr.flush (); wr.close ();
		 */
		
		// RestClient rc = new RestClient();
		//
		// File f = rc.getXML("http://www.wergehthin.de/xml/User/");
		// rc.postXML(f, "http://www.wergehthin.de/xml/User/");
		// System.out.println(f.toString());

	}

	public static void scanDirectory(String dir, FileHashCalculator fhc)
			throws Exception {
		File actual = new File(dir);
		List<File> fileList = new ArrayList<File>();

		for (File f : actual.listFiles()) {
			if (f.isFile())
				fileList.add(f);
		}

		CollectedData.setUser(params.user);
		CollectedData.setMail(params.mail);
		
		Serializer serializer = new Persister();
		CollectedData data = new CollectedData(fileList);
		
		File result = new File("data.xml");
		serializer.write(data, result);
	}
}
