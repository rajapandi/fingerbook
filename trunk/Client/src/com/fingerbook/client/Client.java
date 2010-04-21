package com.fingerbook.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fingerbook.client.gui.Front;
import com.fingerbook.client.marshalling.CollectedData;
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
		applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		params = new ClientParams(args);
		System.out.println(params.toString());
		
		UserInfo userInfo = new UserInfo();
		userInfo.setUser(params.user);
		userInfo.setMail(params.mail);
		
		
		
		FingerbookClient fiClient = applicationContext.getBean("FingerprintsClient", FingerbookClient.class);
		fiClient.setBaseUrl(params.url);
		scanner = new Scanner(params.dir, userInfo);
		
		if (params.gui.equals("yes")) {
			initGUI();
			return;
		}

		CollectedData.setUser(params.user);
		CollectedData.setMail(params.mail);
		
		
	}
	
	private static void initGUI() {
		new Front();
	}

	public static Scanner getScanner() {
		return scanner;
	}

}