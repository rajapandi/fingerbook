package com.fingerbook.models;

public class Response {

	/*
	 * Error responses from server:
	 * 		1 - Fingerbooks: Missing arguments
	 * 		2 - Transaction with fingerbook id: xx could not be finished
	 * 		3 - Transaction: There was an unexpected error: Transaction start aborted
	 * 		4 - Invalid fingerbook state
	 * 		5 - Fingerbook could not be added to fingerbook id: xx
	 * 		6 - Unimplemented method
	 * 		null - Success!
	 * */

	private Integer errorCode;
	private Long rid;
	private String ticket;
	private String desc;
	private String transactionId;
	
	public Response(Integer errorCode, String desc) {
		super();
		this.errorCode = errorCode;
		this.desc = desc;
	}
	
	public Response(Integer errorCode, String desc, Long rid) {
		super();
		this.errorCode = errorCode;
		this.desc = desc;
		this.rid = rid;
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

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
}
