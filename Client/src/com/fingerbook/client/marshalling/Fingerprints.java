package com.fingerbook.client.marshalling;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;

import com.fingerbook.client.FileHashCalculator;
import com.fingerbook.client.FileHashCalculator.Method;

public class Fingerprints {
	@ElementList
	private List<FileInfo> fileInfos = new ArrayList<FileInfo>();
	
	public Fingerprints(List<File> fileList) {
		FileHashCalculator fhc = new FileHashCalculator(Method.SHA1);
		String name;
		String hash;
		Long size;
		
		fileInfos.clear();		
		for (File f : fileList) {
			name = f.getName();
			hash = fhc.getFileHash(f);
			size = f.length();
			fileInfos.add(new FileInfo(name, hash, size));
			
			System.out.println("Filename: " + f.getName()
					+ "--------- Hash: " + fhc.getFileHash(f));
		}
	}
	
	public List<FileInfo> getFileInfo() {
		return fileInfos;
	}
}
