package com.fingerbook.models;

import java.io.Serializable;
import java.util.List;

public class Fingerprints implements Serializable {

	private Long fingerprintsId;
	private List<FileInfo> files;

	public Long getFingerprintsId() {
		return fingerprintsId;
	}
	
	public void setFingerprintsId(Long fingerprintsId) {
		this.fingerprintsId = fingerprintsId;
	}

	public List<FileInfo> getFiles() {
		return files;
	}

	public void setFiles(List<FileInfo> files) {
		this.files = files;
	}
	
	public String toString() {
		StringBuffer ret = new StringBuffer();
		for (FileInfo fi : this.files) {
			ret.append("File: ");
			ret.append(fi.getName());
			ret.append(" \n");
			ret.append("Hash: ");
			ret.append(fi.getShaHash());
			ret.append(" \n");
			ret.append("Size: ");
			ret.append(fi.getSizeInBytes());
			ret.append(" \n\n");
		}
		return ret.toString();
	}
}
