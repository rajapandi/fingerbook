package com.fingerbook.models.transfer;

import java.io.Serializable;
import java.util.Vector;

import com.fingerbook.models.CompositeFingerbook;

public class CompositeFingerbookList extends BaseFeed implements Serializable {

	private static final long serialVersionUID = -443073741994367695L;
	
	protected Vector<CompositeFingerbook> compFbs;

	public CompositeFingerbookList() {
		
	}

	public Vector<CompositeFingerbook> getCompFbs() {
		return compFbs;
	}

	public void setCompFbs(Vector<CompositeFingerbook> compFbs) {
		this.compFbs = compFbs;
	}

}
