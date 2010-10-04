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
	
	/* Recovery files */
	private static File file0 = null;
	private static File fileA = null;
	private static File fileB = null;
	
	/* Last Parameters files */
	private static File fileC = null;
	
	/* 1=a | 2=b */
	private static int nextFileToUse = 0;
	
	public ResumePMan() {
		if(nextFileToUse == 0) {
			file0 = new File(".fbp0");
			fileA = new File(".fbpa");
			fileB = new File(".fbpb");
			fileC = new File(".fbpc");
			nextFileToUse = 1;
		}
	}

	public void storeInitialFilesPath (List<File> files) throws IOException {
		// Create the file
		BufferedWriter file = new BufferedWriter(new FileWriter(file0));
		for(File f : files)
			file.write(f.getAbsolutePath() + "\n");
		file.close();
	}
	
	public File init() {
		File l = scanFiles();
		clean();
		return l;		
	}
	
	private File scanFiles() {
		File l;
		java.util.Scanner scan;
		
		for (int i=0; i<2; i++) {
			try {
				scan = new java.util.Scanner(getNextFile());
			} catch (FileNotFoundException e) { continue; }
			if (scan.hasNext()) {
				/* read last dir scaned */
				l = new File(scan.next());
				if(l.exists())
					return l;
			}
		}
		return null;
	}

	private File getNextFile() {
		File ans = fileA;
		if(nextFileToUse == 2)
			ans = fileB;
		switchFile();
		return ans;
	}
	
	public void clean() {
		file0.delete();
		fileA.delete();
		fileB.delete();
	}

	public void save(String path) throws IOException {
		// Create the file
		BufferedWriter file = new BufferedWriter(new FileWriter(getNextFile()));
		file.write(path);
		file.close();
		// Delete unused file
		EraseCurrent();
	}
	
	private void EraseCurrent() {
		if(nextFileToUse == 1)
			fileA.delete();
		else
			fileB.delete();
	}
		
	private void switchFile() {
		if(nextFileToUse == 1)
			nextFileToUse = 2;
		else
			nextFileToUse = 1;		
	}

	public boolean checkResume() {
		if (file0.length() > 0 && (fileA.length() + fileB.length() > 0) && fileC.length() > 0)
			return true;
		return false;
	}

	public List<File> getDirs() {
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
		Map<String, String> configuration = new HashMap<String, String>();
		java.util.Scanner scan;
		
		try {
			scan = new java.util.Scanner(fileC);
			if (scan.hasNext())
				configuration.put("scanDir", scan.next());
			if (scan.hasNext())
				configuration.put("cTicket", scan.next());
			if (scan.hasNext())
				configuration.put("ticket", scan.next());
			if (scan.hasNext())
				configuration.put("recursive", scan.next());
			else
				configuration.put("recursive", "false");
		} catch (Exception e) {
			return null;
		}
		return configuration;
	}

	public void saveActualParams(Map<String, String> configuration) {
		String scanDir = new String(configuration.get("scanDir")); //$NON-NLS-1$
		String cTicket = new String(configuration.get("cTicket")); //$NON-NLS-1$
		String ticket = new String(configuration.get("ticket")); //$NON-NLS-1$
		String recursive = new String(configuration.get("recursive")); //$NON-NLS-1$
		
		// Delete last file
		fileC.delete();
		// Recreate the file
		try {
			BufferedWriter file = new BufferedWriter(new FileWriter(fileC));
			file.write(scanDir + "\n" + cTicket + "\n" + ticket + "\n" + recursive);
			file.close();
		}
		catch (Exception e) {}
	}
}
