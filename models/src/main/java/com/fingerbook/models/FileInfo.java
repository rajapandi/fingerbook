package com.fingerbook.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FileInfo implements Serializable {

	private Long fileInfoId;
	private String path;
	private String name;
	private String shaHash;
	private Long sizeInBytes;
	
	public FileInfo() {
		
	}
	
	public FileInfo(String path, String name, String hash, Long size) {
		this.path = path;
		this.name = name;
		this.shaHash = hash;
		this.sizeInBytes = size;
	}
	
	
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
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSizeInBytes(Long sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}
	
	
}
