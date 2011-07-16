package com.fingerbook.models.transfer;

import java.io.Serializable;

import com.fingerbook.models.Response;

public class BaseFeed implements Serializable {
	
	private static final long serialVersionUID = 3885456691959170883L;
	
	protected Response response;
	protected int totalresults;
	protected int limit;
	protected int offset;
	
	public BaseFeed() {
		super();
	}

	public BaseFeed(int totalresults, int limit, int offset) {
		super();
		this.totalresults = totalresults;
		this.limit = limit;
		this.offset = offset;
	}

	public int getTotalresults() {
		return totalresults;
	}

	public void setTotalresults(int totalresults) {
		this.totalresults = totalresults;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

}
