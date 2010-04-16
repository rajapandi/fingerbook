package fingerbook.domain;

import java.io.Serializable;
import java.util.List;

public class Fingerprints implements Serializable {

	private List<FileInfo> files;

	public List<FileInfo> getFiles() {
		return files;
	}

	public void setFiles(List<FileInfo> files) {
		this.files = files;
	}
}
