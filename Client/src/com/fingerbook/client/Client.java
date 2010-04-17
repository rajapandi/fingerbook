package com.fingerbook.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fingerbook.client.gui.Front;
import com.fingerbook.client.marshalling.CollectedData;

public class Client {

	static ClientParams params;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		params = new ClientParams(args);
		System.out.println(params.toString());
		
		if (params.gui.equals("yes")) {
			initGUI();
			return;
		}

		CollectedData.setUser(params.user);
		CollectedData.setMail(params.mail);
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		FingerbookClient fiClient = applicationContext.getBean("FingerprintsClient", FingerbookClient.class);
		fiClient.setBaseUrl("http://localhost:8080/REST/");
		new Scanner(params.dir);
	}
	
	private static void initGUI() {
		new Front();
	}
}