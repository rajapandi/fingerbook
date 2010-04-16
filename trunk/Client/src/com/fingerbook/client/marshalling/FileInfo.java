package com.fingerbook.client.marshalling;

import org.simpleframework.xml.Element;

class FileInfo {
	@Element
	private String name;
	
	@Element
	private String hash;
	
	@Element
	private Long size;
	
	public FileInfo(String name, String hash, Long size) {
		this.name = name;
		this.hash = hash;
		this.size = size;
	}
	
	public String getName() {
		return name;
	}
	
	public String getHash() {
		return hash;
	}
	
	public Long getSize() {
		return size;
	}
}