package com.fingerbook.models.pig;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PigScript implements Serializable {
	
	private static final long serialVersionUID = -5045996943600970482L;
	public static String defaultDateFormat = "yyyy-MM-dd";
	
	protected int scriptId;
	protected String scriptName;
	protected String scriptDesc;
	protected String scriptFilePath;
	
	protected Date deleted;

	public PigScript() {
		scriptId = -1;
	}

	public int getScriptId() {
		return scriptId;
	}

	public void setScriptId(int scriptId) {
		this.scriptId = scriptId;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getScriptDesc() {
		return scriptDesc;
	}

	public void setScriptDesc(String scriptDesc) {
		this.scriptDesc = scriptDesc;
	}

	public String getScriptFilePath() {
		return scriptFilePath;
	}

	public void setScriptFilePath(String scriptFilePath) {
		this.scriptFilePath = scriptFilePath;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}
	
	public String getFormattedDeleted() {
		
		String deletedStr = "";
		
		try {
			if(deleted != null) {
				
				SimpleDateFormat sdf = new SimpleDateFormat();
				sdf.applyPattern(defaultDateFormat);
				deletedStr = sdf.format(deleted);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deletedStr;
	}

	@Override
	public String toString() {
		
		StringBuffer ret = new StringBuffer();
		ret.append("PigScript [ \n");
		
		ret.append("scriptId: " + this.scriptId + "\n");
		ret.append("scriptName: " + this.scriptName + "\n");
		ret.append("scriptDesc: " + this.scriptDesc + "\n");
		ret.append("scriptFilePath: " + this.scriptFilePath + "\n");
		
		ret.append("deleted: " + getFormattedDeleted() + "\n");
		ret.append("] \n");
		
		return ret.toString();
		
//		return "PigScript [scriptId=" + scriptId + ", scriptName=" + scriptName
//				+ ", scriptFilePath=" + scriptFilePath + ", deleted=" + deleted
//				+ "]";
	}
	
	

}
