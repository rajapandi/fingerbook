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

public class ResumePMan {
	/* Configuration Items */
	private static final String SCANDIR  = "scanDir";		//$NON-NLS-1$
	private static final String CTICKET  = "cTicket";		//$NON-NLS-1$
	private static final String TICKET  = "ticket";		//$NON-NLS-1$
	private static final String RECURSIVE  = "recursive";	//$NON-NLS-1$
	
	/* Recovery files */
	private static File file0 = null;
	private static File fileA = null;
	private static File fileB = null;
	
	/* Last Parameters files */
	private static File fileC = null;
	
	/* 1=A, 2=B */
	private static int nextFileToUse = 0;
	
	public ResumePMan() {
		/* Only once */
		if(nextFileToUse == 0) {
			file0 = new File(".fbp0");
			fileA = new File(".fbpa");
			fileB = new File(".fbpb");
			fileC = new File(".fbpc");
			/* Start with file A */
			nextFileToUse = 1;
		}
	}

	public void storeInitialFilesPath (List<File> files) throws IOException {
		/* Erase file0 */
		file0.delete();
		BufferedWriter file = new BufferedWriter(new FileWriter(file0));
		/* Write initials paths to file 0 */
		for(File f : files)
			file.write(f.getAbsolutePath() + "\n");
		file.close();
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
			} catch (FileNotFoundException e) { continue; }
			if (scan.hasNext()) {
				/* Read last path scaned */
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
		file0.delete();
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
		if (file0.length() > 0 && (fileA.length() + fileB.length() > 0) && fileC.length() > 0)
			return true;
		return false;
	}

	public List<File> getDirs() {
		/* Get inital paths from last scan */
		List<File> files = new ArrayList<File>();
		try {
			java.util.Scanner dirs = new java.util.Scanner(file0);
			while (dirs.hasNext())
				files.add(new File(dirs.next()));
			return files;
		} catch (Exception e) {
			return null;
		}
	}

	public Map<String, String> getLastParams() {
		/* Get configuration from last scan */
		Map<String, String> configuration = new HashMap<String, String>();
		java.util.Scanner scan;
		
		/* Get Last Parameters. If the file is invalid, then use default params */
		try {
			scan = new java.util.Scanner(fileC);
			if (scan.hasNext())
				configuration.put(SCANDIR, scan.next());
			else
				configuration.put(SCANDIR, ".");
			if (scan.hasNext())
				configuration.put(RECURSIVE, scan.next());
			else
				configuration.put(RECURSIVE, "false");
			if (scan.hasNext())
				configuration.put(CTICKET, scan.next());
			else
				configuration.put(CTICKET, "false");
			if (scan.hasNext())
				configuration.put(TICKET, scan.next());
			else
				configuration.put(TICKET, "");

		} catch (Exception e) {
			return null;
		}
		return configuration;
	}

	public void saveActualParams(Map<String, String> configuration) {
		/* Save configuration from this scan */
		String scanDir = new String(configuration.get(SCANDIR));		//$NON-NLS-1$
		String recursive = new String(configuration.get(RECURSIVE));	//$NON-NLS-1$
		String cTicket = new String(configuration.get(CTICKET));		//$NON-NLS-1$
		String ticket = new String(configuration.get(TICKET));			//$NON-NLS-1$
		
		/* Delete and recreate file 0 */
		fileC.delete();
		try {
			BufferedWriter file = new BufferedWriter(new FileWriter(fileC));
			file.write(scanDir + "\n" + recursive + "\n" +cTicket + "\n" + ticket);
			file.close();
		}
		catch (Exception e) {}
	}
}
