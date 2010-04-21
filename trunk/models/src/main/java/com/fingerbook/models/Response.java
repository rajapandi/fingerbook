package com.fingerbook.models;

public class Response {

	// Error code in null mean succes!
	private Integer errorCode;
	private String desc;
	
	public Response(Integer errorCode, String desc) {
		super();
		this.errorCode = errorCode;
		this.desc = desc;
	}
	
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
