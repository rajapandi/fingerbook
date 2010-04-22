package com.fingerbook.client;

import java.io.File;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fingerbook.client.gui.Front;
import com.fingerbook.client.marshalling.CollectedData;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.models.Response;
import com.fingerbook.models.UserInfo;

public class Client {

	static ClientParams params;
	static Scanner scanner;
	public static ApplicationContext applicationContext;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		params = new ClientParams(args);

		System.out.println(params.toString());

		UserInfo userInfo = new UserInfo();
		userInfo.setUser(params.user);
		userInfo.setMail(params.mail);

		FingerbookClient fiClient = applicationContext.getBean(
				"FingerprintsClient", FingerbookClient.class);
		fiClient.setBaseUrl(params.url);
		scanner = new Scanner(params.path, userInfo);

		if (params.gui.equals("yes")) {
			initGUI();
		} else {
			CollectedData.setUser(params.user);
			CollectedData.setMail(params.mail);
			
			console();
		}
	}

	private static void console() {
		Response resp = null;
		
		if (params.action.equals("put")) {
			try {
				resp = Client.getScanner().scanDirectory(params.path);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if (resp == null || resp.getErrorCode() != null) {
				System.out.println("Error!:\n" + resp != null ? resp.getDesc()
						: "");
			} else {
				System.out.println("Success!:\n" + resp.getDesc());
			}
		}
		else {
			try {
				File f = new File(params.path);
				FileHashCalculator fhc = Client.applicationContext.getBean("fileHashCalculator", FileHashCalculator.class);
				FingerbookClient fiClient = Client.applicationContext.getBean("FingerprintsClient", FingerbookClient.class);
				List<Fingerprints> list = fiClient.getGroups(fhc.getFileHash(f));
				
				System.out.println(list.toString());
			} catch (Exception ex) {ex.printStackTrace();}
		}
		
	}

	private static void initGUI() {
		new Front();
	}

	public static Scanner getScanner() {
		return scanner;
	}

}