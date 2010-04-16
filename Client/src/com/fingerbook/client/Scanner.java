package com.fingerbook.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fingerbook.client.FileHashCalculator.Method;
import com.fingerbook.client.marshalling.CollectedData;
import com.fingerbook.client.marshalling.FileInfo;
import fingerbook.domain.Fingerprints;

public class Scanner {

	static ClientParams params;



	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Fingerprints f;
		
		params = new ClientParams(args);
		System.out.println(params.toString());

		FileHashCalculator fhc = new FileHashCalculator(Method.SHA1);

		f = scanDirectory(params.dir, fhc);
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
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(	"applicationContext.xml" );
		FingerbookClient fiClient = applicationContext.getBean( "FingerprintsClient", FingerbookClient.class );
		fiClient.setBaseUrl("http://localhost:8080/REST/");
		FileInfo fi = new FileInfo("data.xml", "53870f83687822d7c768e6162cb32cf5e87567da", 1000L);
		List<fingerbook.domain.Fingerprints> list = fiClient.getGroups(fi);
		for (fingerbook.domain.Fingerprints fingerprints : list) {
			for (fingerbook.domain.FileInfo file: fingerprints.getFiles()) {
				System.out.println(file.getName() + "--------------------" + file.getShaHash());				
			}
		}
//		 RestClient rc = new RestClient();
//		
//		 File f = rc.getXML("http://www.wergehthin.de/xml/User/");
//		 rc.postXML(f, "http://www.wergehthin.de/xml/User/");
//		 System.out.println(f.toString());
//		 RestTemplate rt = new RestTemplate();
//		 String result = rt.getForObject("http://www.wergehthin.de/xml/User/", String.class);
//		 System.out.println(result);
//		 
		 //Delete the file before leaving
		 //f.delete();
	}

	public static Fingerprints scanDirectory(String dir, FileHashCalculator fhc)
			throws Exception {
		File actual = new File(dir);
		List<File> fileList = new ArrayList<File>();

		for (File f : actual.listFiles()) {
			if (f.isFile())
				fileList.add(f);
		}

		CollectedData.setUser(params.user);
		CollectedData.setMail(params.mail);
		
		
		Fingerprints data = new Fingerprints(fileList);
//		Serializer serializer = new Persister();
//		File result = new File("data.xml");
//		serializer.write(data, result);
		return data;
	}
}
