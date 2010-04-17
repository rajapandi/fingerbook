package fingerbook.domain;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fingerbook.client.FileHashCalculator;
import com.fingerbook.client.FileHashCalculator.Method;
import fingerbook.domain.FileInfo;

public class Fingerprints implements Serializable {
	private static final long serialVersionUID = -6294557277922170935L;
	private Long fingerprintsId;
	private List<FileInfo> files;
	
	public Fingerprints(List<File> fileList) {
		FileHashCalculator fhc = new FileHashCalculator(Method.SHA1);
		String name;
		String hash;
		Long size;
		
		files = new ArrayList<FileInfo>();
		
		System.out.println("\nFiles Found:");
		for (File f : fileList) {
			name = f.getName();
			hash = fhc.getFileHash(f);
			size = f.length();
			files.add(new FileInfo(name, hash, size));
			
			System.out.println("Filename: " + f.getName() + "--------- Hash: " + fhc.getFileHash(f));
		}
	}

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
}
