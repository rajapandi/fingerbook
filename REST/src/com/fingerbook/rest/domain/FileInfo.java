package com.fingerbook.rest.domain;

import java.io.Serializable;

public class FileInfo implements Serializable {

	private Long fileInfoId;
	private String name;
	private String shaHash;
	private Long sizeInBytes;
	
	public Long getFileInfoId() {
		return fileInfoId;
	}
	public void setFileInfoId(Long fileInfoId) {
		this.fileInfoId = fileInfoId;
	}
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
	public Long getSizeInBytes() {
		return sizeInBytes;
	}
	public void setSizeInBytes(Long sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}
	
	
}
