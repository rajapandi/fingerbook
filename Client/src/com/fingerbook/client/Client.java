package com.fingerbook.client;

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
		new Scanner(params.dir);
	}
	
	private static void initGUI() {
		new Front();
	}
}