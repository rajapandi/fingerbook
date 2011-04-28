package com.fingerbook.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.fingerbook.client.marshalling.ConfigurationData;

public class ResumePMan {
	/* Configuration Items */
	private static final String SCANDIR  = "scanDir";		//$NON-NLS-1$
	private static final String CLOGIN  = "cLogin";			//$NON-NLS-1$
	private static final String USER  = "user";				//$NON-NLS-1$
	private static final String PASS  = "pass";				//$NON-NLS-1$
	private static final String CTICKET  = "cTicket";		//$NON-NLS-1$
	private static final String TICKET  = "ticket";			//$NON-NLS-1$
	private static final String RECURSIVE  = "recursive";	//$NON-NLS-1$
	private static final String TRANSID = "transactionId";	//$NON-NLS-1$
	
	/* Recovery files */
	private static File fileA = null;
	private static File fileB = null;
	
	/* Last Parameters files */
	private static File fileC = null;
	
	/* 1=A, 2=B */
	private static int nextFileToUse = 0;
	
	public ResumePMan() {
		/* Only once */
		if(nextFileToUse == 0) {
			fileA = new File(".fbpa");
			fileB = new File(".fbpb");
			fileC = new File("conf.xml");
			/* Start with file A */
			nextFileToUse = 1;
		}
	}
	
	public File init() {
		File l = scanFiles();
		/* Once I have the info, discard the resume files */
		clean();
		return l;		
	}
	
	private File scanFiles() {
		File l;
		java.util.Scanner scan;
		
		/* Search for file A or B. Look if there is a valid path in it.
		 * If exists return it, if not return null */
		for (int i=0; i<2; i++) {
			try {
				scan = new java.util.Scanner(getNextFile());
				scan.useDelimiter("\n");
			} catch (FileNotFoundException e) { continue; }
			if (scan.hasNext()) {
				/* Read last path scanned */
				l = new File(scan.next());
				if(l.exists())
					return l;
			}
		}
		return null;
	}

	private File getNextFile() {
		/* Return next file available for use. Round-Robin btw A and B */
		File ans = fileA;
		if(nextFileToUse == 2)
			ans = fileB;
		switchFile();
		return ans;
	}
	
	public void clean() {
		/* Delete resume files */
		fileA.delete();
		fileB.delete();
	}

	public void save(String path) throws IOException {
		/* Create the file */
		BufferedWriter file = new BufferedWriter(new FileWriter(getNextFile()));
		file.write(path);
		file.close();
		EraseCurrent();
	}
	
	private void EraseCurrent() {
		/* Delete next file to use */
		if(nextFileToUse == 1)
			fileA.delete();
		else
			fileB.delete();
	}
		
	private void switchFile() {
		/* Set next file to use */
		if(nextFileToUse == 1)
			nextFileToUse = 2;
		else
			nextFileToUse = 1;		
	}

	public boolean checkResume() {
		/* Check if a resume is possible */
		if ((fileA.length() + fileB.length()) > 0 && fileC.length() > 0)
			return true;
		return false;
	}

	/* Deprecated */
//	public List<File> getDirs() {
//		/* Get inital paths from last scan */
//		List<File> files = new ArrayList<File>();
//		try {
//			java.util.Scanner dirs = new java.util.Scanner(file0);
//			dirs.useDelimiter("\n");
//			while (dirs.hasNext())
//				files.add(new File(dirs.next()));
//			return files;
//		} catch (Exception e) {
//			return null;
//		}
//	}

	/* Deprecated */
//	public void storeInitialFilesPath (List<File> files) throws IOException {
//		/* Erase file0 */
//		file0.delete();
//		BufferedWriter file = new BufferedWriter(new FileWriter(file0));
//		/* Write initials paths to file 0 */
//		for(File f : files)
//			file.write(f.getAbsolutePath() + "\n");
//		file.close();
//	}

	public Map<String, String> getLastParams() {
		/* Get configuration from last scan */
		Map<String, String> configuration = new HashMap<String, String>();
		StringBuffer paths = new StringBuffer("");		
		Serializer serializer = new Persister();

		try {
			/* Unmarshall */
			ConfigurationData config = serializer.read(ConfigurationData.class, fileC);

			/* Build string from List */
			for (String s:config.getPaths())
				paths.append(s + ";");
			
			/* Save parameters to Configuration Map */
			configuration.put(CLOGIN, config.getAuth());
			configuration.put(USER, config.getUser());
			configuration.put(PASS, config.getPass());
			configuration.put(RECURSIVE, config.getRecursive());
			configuration.put(CTICKET, config.getSemiAuth());
			configuration.put(TICKET, config.getTicket());
			configuration.put(SCANDIR, paths.toString());	
			configuration.put(TRANSID, config.getTransId());	
		} catch (Exception e) {
			/* If error ocurrs, use default params */
			configuration.put(CLOGIN, "false");
			configuration.put(USER, "");
			configuration.put(PASS, "");
			configuration.put(SCANDIR, ".");
			configuration.put(RECURSIVE, "false");
			configuration.put(CTICKET, "false");
			configuration.put(TICKET, "");
			configuration.put(TRANSID, "");
		}
		return configuration;
	}

	public void saveActualParams(Map<String, String> configuration) {
		/* Save configuration from this scan */
		Serializer serializer = new Persister();
		ConfigurationData config = new ConfigurationData();
		
		/* Build List from String */
		List<String> paths = new ArrayList<String>();
		java.util.Scanner scan = new java.util.Scanner(configuration.get(SCANDIR));
		scan.useDelimiter(";");
		while (scan.hasNext())
			paths.add(scan.next());
		
		/* Set parameters before marshalling */
		config.setAuth(configuration.get(CLOGIN));
		config.setUser(configuration.get(USER));
		config.setPass(configuration.get(PASS));
		config.setSemiAuth(configuration.get(CTICKET));
		config.setTicket(configuration.get(TICKET));
		config.setRecursive(configuration.get(RECURSIVE));
		config.setPaths(paths);
		config.setTransId(configuration.get(TRANSID));

		try {
			/* write xml persistent configuration file */
			serializer.write(config, fileC);
		} catch (Exception e) {}
	}
}
