package com.fingerbook.client;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fingerbook.client.gui.Front;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Response;
import com.fingerbook.models.UserInfo;
import com.l2fprod.common.swing.plaf.LookAndFeelAddons;

public class Client {

	static ClientParams params;
	static Scanner scanner;
	public static ApplicationContext applicationContext;
	public static Front front;
	public static ResumePMan fMan;
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		boolean resume;
		boolean populate = false;
		
		/* Init Logger */
		Logger logger = LoggerFactory.getLogger(Client.class);
		logger.debug("Application started.");

		applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		/* Set client parameters passed through the arguments */
		params = new ClientParams(args);
		System.out.println(params.toString());

		/* Set user account Info */
		UserInfo userInfo = new UserInfo();
		userInfo.setUser(params.user);
		userInfo.setMail(params.mail);

		FingerbookClient fiClient = applicationContext.getBean(FingerbookClient.class);
		fiClient.setBaseUrl(params.url);

		scanner = new Scanner();
		fMan = new ResumePMan();		
		
		/* Check if there is any scan that can be resumed */
		resume = fMan.checkResume();
		if (resume)
			populate = true;
			
		if (params.gui.equals("yes")) {
			logger.info("Starting GUI.");
			if (resume)
				logger.info("Resume detected");
			/* Start GUI */
			initGUI(populate, resume);				
		} else
			/* No GUI */
			console(resume);
	}

	// TODO: HAY QUE ACTUALIZARLO, ASI COMO ESTA NO ANDA NI EN JODA
	private static void console(boolean resume) {
		Response resp = null;
		Map<String, String> configuration = new HashMap<String, String>();

		if (params.action.equals("put")) {
			try {
				resp = Client.getScanner().scanDirectory(configuration, false);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if (resp == null || resp.getErrorCode() != null) {
				System.out.println("Error!:\n" + resp != null ? resp.getDesc()
						: "");
			} else {
				System.out.println("Success!:\n" + resp.getDesc());
			}
		} else {
			try {
				File f = new File(params.path);
				FileHashCalculator fhc = Client.applicationContext.getBean(FileHashCalculator.class);
				FingerbookClient fiClient = Client.applicationContext.getBean(FingerbookClient.class);
				List<Fingerbook> list = fiClient.getGroups(fhc.getFileHash(f));

				System.out.println(list.toString());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void initGUI(boolean populate, boolean resume) {
		try {
			/* Set OS native Look & Feel */
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			LookAndFeelAddons.setAddon(LookAndFeelAddons
					.getBestMatchAddonClassName());
		} catch (Exception e) {
		}
		/* Initial screen */
		front = new Front(populate, resume);
	}

	public static Scanner getScanner() {
		return scanner;
	}
}