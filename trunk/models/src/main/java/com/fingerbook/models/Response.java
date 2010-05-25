package com.fingerbook.models;

public class Response {

	// Error code null means success!
	private Integer errorCode;
	private Long rid;
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
	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
