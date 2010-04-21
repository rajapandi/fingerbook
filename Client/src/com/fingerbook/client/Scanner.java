package com.fingerbook.client;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.fingerbook.client.FileHashCalculator.Method;

import fingerbook.domain.Fingerbook;
import fingerbook.domain.Fingerprints;
import fingerbook.domain.UserInfo;

public class Scanner {
	private FileHashCalculator fhc;
	private FingerbookClient fiClient;
	private String dir;
	private UserInfo userInfo;

	public Scanner(String dir, FingerbookClient fiClient, UserInfo userInfo) throws Exception {
		
		this.fiClient = fiClient;
		this.dir = dir;
		this.fhc = new FileHashCalculator(Method.SHA1);
		this.userInfo = userInfo;
		//Fingerprints fis = scanDirectory(dir, fhc);

		
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

	public Fingerbook scanDirectory()
			throws Exception {
		File actual = new File(dir);
		List<File> fileList = new ArrayList<File>();

		for (File f : actual.listFiles()) {
			if (f.isFile())
				fileList.add(f);
		}

		Fingerprints data = new Fingerprints(fileList);
		
		Fingerbook fb = new Fingerbook();
		fb.setUserInfo(this.userInfo);
		fb.setFingerPrints(data);
		fb.setStamp(new GregorianCalendar().getTimeInMillis());
		fiClient.postHashes(fb);
		// Serializer serializer = new Persister();
		// File result = new File("data.xml");
		// serializer.write(data, result);
		return fb;
	}

}
