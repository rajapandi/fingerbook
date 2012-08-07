package com.fingerbook.models.pig;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PigScriptResult implements Serializable {
	
	private static final long serialVersionUID = 5616968398809230436L;

	public static String defaultDateFormat = "yyyy-MM-dd";

	protected int scriptResultId;
//	protected String scriptName;
//	protected String scriptFilePath;
	protected PigScript pigScript;
	
//	protected int returnCode = -1;
	protected Integer returnCode;
    
	protected String returnMessage;
	
	protected boolean successful = false;
	
	protected String errorMessage;
//	protected int errorCode = -1;
	protected Integer errorCode;
	
	protected long duration = 0;
	
	protected Date execDate;

	public PigScriptResult() {
		super();
	}

//	public String getScriptName() {
//		return scriptName;
//	}
//
//	public void setScriptName(String scriptName) {
//		this.scriptName = scriptName;
//	}
//
//	public String getScriptFilePath() {
//		return scriptFilePath;
//	}
//
//	public void setScriptFilePath(String scriptFilePath) {
//		this.scriptFilePath = scriptFilePath;
//	}

	public PigScript getPigScript() {
		return pigScript;
	}

	public int getScriptResultId() {
		return scriptResultId;
	}

	public void setScriptResultId(int scriptResultId) {
		this.scriptResultId = scriptResultId;
	}

	public void setPigScript(PigScript pigScript) {
		this.pigScript = pigScript;
	}
	
	public int getScriptId() {
		
		int auxScriptId = -1;
		
		if(pigScript != null) {
			auxScriptId = pigScript.getScriptId();
		}
		
		return auxScriptId;
	}

	public Integer getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(Integer returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Date getExecDate() {
		return execDate;
	}

	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}
	
	public String getFormattedExecDate() {
		
		String execDateStr = "";
		
		try {
			if(execDate != null) {
				SimpleDateFormat sdf = new SimpleDateFormat();
				sdf.applyPattern(defaultDateFormat);
//				execDateStr = sdf.format(new Date(execDate));
				execDateStr = sdf.format(execDate);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return execDateStr;
	}

	@Override
	public String toString() {
		
		StringBuffer ret = new StringBuffer();
		ret.append("PigScriptResult [ \n");
		
//		ret.append("scriptName: " + this.scriptName + "\n");
//		ret.append("scriptFilePath: " + this.scriptFilePath + "\n");
		
		ret.append("scriptResultId: " + this.scriptResultId + "\n");
		
		if(pigScript != null) {
			ret.append(this.pigScript.toString() + "\n");
		}
		
		ret.append("returnCode: " + this.returnCode + "\n");
		ret.append("returnMessage: " + this.returnMessage + "\n");
		ret.append("successful: " + (this.successful ? "YES" : "NO") + "\n");
		ret.append("errorCode: " + this.errorCode + "\n");
		ret.append("errorMessage: " + this.errorMessage + "\n");
		ret.append("duration: " + this.duration + "\n");
		ret.append("execDate: " + getFormattedExecDate() + "\n");
		ret.append("] \n");
		
		return ret.toString();
		
//		return "PigScriptResult [scriptName=" + scriptName
//				+ ", scriptFilePath=" + scriptFilePath + ", returnCode="
//				+ returnCode + ", returnMessage=" + returnMessage
//				+ ", successful=" + successful + ", errorMessage="
//				+ errorMessage + ", errorCode=" + errorCode + ", duration="
//				+ duration + "]";
	}
	
	
}
