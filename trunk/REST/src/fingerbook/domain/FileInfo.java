package fingerbook.domain;

public class FileInfo {

	private String name;
	private String shaHash;
	private Integer sizeInBytes;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShaHash() {
		return shaHash;
	}
	public void setShaHash(String shaHash) {
		this.shaHash = shaHash;
	}
	public Integer getSizeInBytes() {
		return sizeInBytes;
	}
	public void setSizeInBytes(Integer sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}
	
	
}
