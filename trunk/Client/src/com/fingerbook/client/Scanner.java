package com.fingerbook.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fingerbook.client.FileHashCalculator.Method;
import com.fingerbook.client.marshalling.FileInfo;

import fingerbook.domain.Fingerprints;

public class Scanner {

	public Scanner(String dir) throws Exception {
		FileHashCalculator fhc = new FileHashCalculator(Method.SHA1);

		scanDirectory(dir, fhc);

//		FileInfo fi = new FileInfo("data.xml",
//				"53870f83687822d7c768e6162cb32cf5e87567da", 1000L);
//		List<fingerbook.domain.Fingerprints> list = fiClient.getGroups("53870f83687822d7c768e6162cb32cf5e87567da");
//		for (fingerbook.domain.Fingerprints fingerprints : list) {
//			for (fingerbook.domain.FileInfo file : fingerprints.getFiles()) {
//				System.out.println(file.getName() + "--------------------"
//						+ file.getShaHash());
//			}
//		}
//		// RestClient rc = new RestClient();
		//		
		// File f = rc.getXML("http://www.wergehthin.de/xml/User/");
		// rc.postXML(f, "http://www.wergehthin.de/xml/User/");
		// System.out.println(f.toString());
		// RestTemplate rt = new RestTemplate();
		// String result = rt.getForObject("http://www.wergehthin.de/xml/User/",
		// String.class);
		// System.out.println(result);
	}

	public static Fingerprints scanDirectory(String dir, FileHashCalculator fhc)
			throws Exception {
		File actual = new File(dir);
		List<File> fileList = new ArrayList<File>();

		for (File f : actual.listFiles()) {
			if (f.isFile())
				fileList.add(f);
		}

		Fingerprints data = new Fingerprints(fileList);
		// Serializer serializer = new Persister();
		// File result = new File("data.xml");
		// serializer.write(data, result);
		return data;
	}

}
