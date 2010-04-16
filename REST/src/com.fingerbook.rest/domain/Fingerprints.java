package fingerbook.domain;

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
}
