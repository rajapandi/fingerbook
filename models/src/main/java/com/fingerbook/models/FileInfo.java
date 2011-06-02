package com.fingerbook.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FileInfo implements Serializable {

	private transient String path;
	private String shaHash;
/*	private Long fileInfoId;
	private String name;
	private Long sizeInBytes;
*/	
	public FileInfo() {
	}
	
	public FileInfo(String path, String hash) {
		this.path = path;
		this.shaHash = hash;
/*		this.name = name;
		this.sizeInBytes = size;
*/
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getShaHash() {
		return shaHash;
	}
	public void setShaHash(String shaHash) {
		this.shaHash = shaHash;
	}
		
/*	public Long getFileInfoId() {
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

	public Long getSizeInBytes() {
		return sizeInBytes;
	}
	public void setSizeInBytes(Long sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}
*/	
}
