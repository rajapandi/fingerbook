package com.fingerbook.models;

import java.io.Serializable;
import java.util.Set;

public class CompositeFingerbook implements Serializable {

	
	private static final long serialVersionUID = -644692372990225729L;
	
	protected String compFingerbookId;
	protected Long fingerbookId1;
	protected Long fingerbookId2;
	protected Set<String> tags;
	protected Fingerprints fingerPrints;
	protected int totalFingers;
	
	
	public CompositeFingerbook() {
		
	}

	public CompositeFingerbook(String compFingerbookId) {
		
		this.compFingerbookId = compFingerbookId;
	}

	public CompositeFingerbook(Long fingerbookId1, Long fingerbookId2) {
		
		this.fingerbookId1 = fingerbookId1;
		this.fingerbookId2 = fingerbookId2;
	}

	public String getCompFingerbookId() {
		return compFingerbookId;
	}

	public void setCompFingerbookId(String compFingerbookId) {
		this.compFingerbookId = compFingerbookId;
	}

	public Long getFingerbookId1() {
		return fingerbookId1;
	}

	public void setFingerbookId1(Long fingerbookId1) {
		this.fingerbookId1 = fingerbookId1;
	}

	public Long getFingerbookId2() {
		return fingerbookId2;
	}

	public void setFingerbookId2(Long fingerbookId2) {
		this.fingerbookId2 = fingerbookId2;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public Fingerprints getFingerPrints() {
		return fingerPrints;
	}

	public void setFingerPrints(Fingerprints fingerPrints) {
		this.fingerPrints = fingerPrints;
	}

	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("ID: " + this.compFingerbookId + "\n");
		ret.append("fingerbookId1: " + this.fingerbookId1 + "\n");
		ret.append("fingerbookId2: " + this.fingerbookId2 + "\n");
		ret.append("TotalFingers: " + this.totalFingers + "\n");
		if(this.tags != null) {
			ret.append("Tags: {");
			boolean first = true;
			for (String tag : this.tags) {
				if(!first) {
					ret.append(", ");
				}
				ret.append(tag);
				first = false;
			}
			ret.append("}\n");
		}
		if(this.fingerPrints != null && this.fingerPrints.getFiles() != null) {
			for (FileInfo fi : this.fingerPrints.getFiles()) {
				ret.append(" \nHash: ");
				ret.append(fi.getShaHash());
				ret.append(" \n\n");
			}
		}
		return ret.toString();
	}

	public int getTotalFingers() {
		return totalFingers;
	}

	public void setTotalFingers(int totalFingers) {
		this.totalFingers = totalFingers;
	}
	
}
