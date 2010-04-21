package com.fingerbook.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fingerbook.models.FileInfo;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.models.UserInfo;


public class FingerbookServices {

	protected final Log logger = LogFactory.getLog(getClass());

	public Fingerbook getFingerbook(Long fingerbookId) {
		// TODO Robot service
		
		if(fingerbookId != null && fingerbookId > 0) {
			
			Fingerbook fingerbook = new Fingerbook();
			fingerbook.setFingerbookId(fingerbookId);
			
			// UserInfo
			UserInfo userInfo = new UserInfo();
			userInfo.setUser("simon");
			userInfo.setMail("simon@fingerbook.com");
			userInfo.setUserInfoId(new Long(1));
			fingerbook.setUserInfo(userInfo);
			
			//Fingerprint(files)
			Fingerprints fingerPrints = new Fingerprints();
			fingerPrints.setFingerprintsId(new Long(1));
			ArrayList<FileInfo> files = new ArrayList<FileInfo>();
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileInfoId(new Long(1));
			fileInfo.setName("myfile.exe");
			fileInfo.setShaHash("DAFDSFSDFGSG3432VSDV");
			fileInfo.setSizeInBytes(new Long(3324));
			files.add(fileInfo);
			fingerPrints.setFiles(files);
			fingerbook.setFingerPrints(fingerPrints);
			
			return fingerbook;
		} else {
			return null;
		}
	}

	public List<Fingerprints> getFingerprintsWithHash(String hash) {
		// TODO Robot service
		
		if(hash != null) {
			ArrayList<Fingerprints> fingerprintsList = new ArrayList<Fingerprints>();
			
			// fingerprint1(files)
			Fingerprints fingerPrints = new Fingerprints();
			fingerPrints.setFingerprintsId(new Long(1));
			ArrayList<FileInfo> files = new ArrayList<FileInfo>();
			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileInfoId(new Long(1));
			fileInfo.setName("myfile1.exe");
			fileInfo.setShaHash(hash);
			fileInfo.setSizeInBytes(new Long(3324));
			files.add(fileInfo);
			fingerPrints.setFiles(files);
			
			fingerprintsList.add(fingerPrints);
			
			// fingerprint2(files)
			fingerPrints = new Fingerprints();
			fingerPrints.setFingerprintsId(new Long(2));
			files = new ArrayList<FileInfo>();
			fileInfo = new FileInfo();
			fileInfo.setFileInfoId(new Long(2));
			fileInfo.setName("myfile2.exe");
			fileInfo.setShaHash("GFHJFG54364HGFd54634");
			fileInfo.setSizeInBytes(new Long(9325425));
			files.add(fileInfo);
			fingerPrints.setFiles(files);
			
			fingerprintsList.add(fingerPrints);
			
			return fingerprintsList;
		} else {
			return null;
		}
	}

	public Fingerbook getFingerbook(Fingerprints fingerprints) {
		Fingerbook fingerbook = new Fingerbook();
		fingerbook.setFingerPrints(fingerprints);
		return fingerbook;
	}	
}
