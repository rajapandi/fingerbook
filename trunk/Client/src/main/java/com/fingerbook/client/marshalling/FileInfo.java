package com.fingerbook.client.marshalling;

import org.simpleframework.xml.Element;

public class FileInfo {
	@Element
	private String name;

	@Element
	private String shaHash;

	@Element
	private Long sizeInBytes;

	public FileInfo(String name, String hash, Long size) {
		this.name = name;
		this.shaHash = hash;
		this.sizeInBytes = size;
	}

	public String getName() {
		return name;
	}

	public String getHash() {
		return shaHash;
	}
	
	public Long getSize() {
		return sizeInBytes;
	}
}
