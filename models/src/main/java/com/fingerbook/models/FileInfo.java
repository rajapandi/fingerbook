package com.fingerbook.models;

import java.io.Serializable;

public class FileInfo implements Serializable {

	private Long fileInfoId;
	private String name;
	private String shaHash;
	private Integer sizeInBytes;
	
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
	public Integer getSizeInBytes() {
		return sizeInBytes;
	}
	public void setSizeInBytes(Integer sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}
	
	
}
