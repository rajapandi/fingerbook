package com.fingerbook.models.transfer;

import java.io.Serializable;
import java.util.Vector;

import com.fingerbook.models.Fingerbook;

public class FingerbookList extends BaseFeed implements Serializable {

	private static final long serialVersionUID = -7782888089388216232L;
	
	protected Vector<Fingerbook> fbs;
//	protected int totalFbs;
//	protected int limit;
//	protected int offset;
	
	public FingerbookList() {
		
	}

	public Vector<Fingerbook> getFbs() {
		return fbs;
	}

	public void setFbs(Vector<Fingerbook> fbs) {
		this.fbs = fbs;
	}

}
