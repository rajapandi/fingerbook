package com.fingerbook.models.transfer;

import java.io.Serializable;
import java.util.Vector;

import com.fingerbook.models.Fingerbook;

public class FingerbookList implements Serializable {

	private static final long serialVersionUID = -7782888089388216232L;
	
	protected Vector<Fingerbook> fbs;
	protected int totalFbs;
	protected int limit;
	protected int offset;
	
	public FingerbookList() {
		
	}

	public Vector<Fingerbook> getFbs() {
		return fbs;
	}

	public void setFbs(Vector<Fingerbook> fbs) {
		this.fbs = fbs;
	}

	public int getTotalFbs() {
		return totalFbs;
	}

	public void setTotalFbs(int totalFbs) {
		this.totalFbs = totalFbs;
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

}
