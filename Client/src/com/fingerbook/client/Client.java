package com.fingerbook.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fingerbook.client.gui.Front;
import com.fingerbook.client.marshalling.CollectedData;

import fingerbook.domain.UserInfo;

public class Client {

	static ClientParams params;
	static Scanner scanner;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		params = new ClientParams(args);
		System.out.println(params.toString());
		
		UserInfo userInfo = new UserInfo();
		userInfo.setUser(params.user);
		userInfo.setMail(params.mail);
		
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		FingerbookClient fiClient = applicationContext.getBean("FingerprintsClient", FingerbookClient.class);
		fiClient.setBaseUrl("http://localhost/fingerbookREST/");
		scanner = new Scanner(params.dir, fiClient, userInfo);
		
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