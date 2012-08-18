package com.fingerbook.pig.svc;

import java.io.File;
import java.util.Date;
import java.util.Random;

import org.apache.pig.PigRunner;
import org.apache.pig.PigRunner.ReturnCode;
import org.apache.pig.tools.pigstats.PigStats;

import com.fingerbook.models.pig.PigScript;
import com.fingerbook.models.pig.PigScriptResult;


public class PigExecutorManager {

//	public static PigScriptResult execScript(String scriptName, String scriptFilePath) {
	public static PigScriptResult execScript(PigScript pigScript) {
		
		PigScriptResult scriptResult = new PigScriptResult();
		
		String returnMessage = null;
		
//		scriptResult.setScriptName(scriptName);
//		scriptResult.setScriptFilePath(scriptFilePath);
		
		scriptResult.setPigScript(pigScript);
		
		if(pigScript != null) {
			String scriptFilePath = pigScript.getScriptFilePath();
			
			scriptResult.setExecDate(new Date());
			
			try {
	            @SuppressWarnings({ "rawtypes", "unused" })
				Class klass = Class.forName("org.apache.pig.PigRunner");
	            
	//            String script = "bin/script.pig";
	            
	            File scriptFile = new File(scriptFilePath);
	            
	            String path = scriptFile.getPath();
	            String absolutePath = scriptFile.getAbsolutePath();
	            String canonicalPath = "EMPTY";
	            
	            System.out.println("path: " + path);
				System.out.println("absolutePath: " + absolutePath);
				System.out.println("canonicalPath: " + canonicalPath);
	          
	            String parentPath = scriptFile.getParentFile().getAbsolutePath();
				
				System.out.println("parentPath: " + parentPath);
	            
	            if (!scriptFile.exists()) {
	            	
	            	returnMessage = "Error: Pig script file [" + scriptFilePath + "] does not exist";
	            	
	            	scriptResult.setReturnCode(ReturnCode.FAILURE);
	            	scriptResult.setReturnMessage(returnMessage);
	            	
	            	scriptResult.setSuccessful(false);
	            	
	            	return scriptResult;
	            	
	//                throw new RuntimeException("Error: Pig script file [" + scriptFilePath + "] does not exist");
	            }
	            
//	            scriptResult.setExecDate(new Date());
	            
	//            PigStats stats = org.apache.pig.PigRunner.run( new String[] { "-version" }, null);
	//            PigStats stats = PigRunner.run( new String[] { "-x", "local", "-file", script, "-logfile", "/home/pampa/Documents/workspace4/PigExecutor/bin" }, null);
//	            PigStats stats = PigRunner.run( new String[] { "-x", "local", "-file", scriptFilePath, "-logfile", parentPath }, null);
	            String outputPath = "out_fb_pig_" + System.currentTimeMillis();
	            PigStats stats = PigRunner.run( new String[] {"-Dpig.additional.jars=/home/pampa/tpf/pigscripts/FBUDFS.jar", "-x", "local", "-param", "outpath=\"" + parentPath + "/" + outputPath + "\"", "-file", scriptFilePath, "-logfile",
	            		parentPath}, null);
	            
	            scriptResult.setReturnCode(stats.getReturnCode());
	            scriptResult.setDuration(stats.getDuration());
	            
	            // isSuccessful is the API from 0.9 supported by both PigStats and
	            // EmbeddedPigStats
	            if (!stats.isSuccessful()) {
	            	
	            	scriptResult.setSuccessful(false);
	            	scriptResult.setErrorCode(stats.getErrorCode());
	            	scriptResult.setErrorMessage(stats.getErrorMessage());
	            	
	            	returnMessage = "ERROR: could not run pig script. Please check logs in server";
	            	
	//            	System.out.println(stats.getErrorCode() + " " + stats.getErrorMessage());
	            } else {
	            	
	            	scriptResult.setSuccessful(true);
	            	returnMessage = "SUCCESS: Pig script was ran successfully.";
	            }
	            
	            scriptResult.setReturnMessage(returnMessage);
	//            System.out.println("returnMessage" + returnMessage);
	        }
	        catch (ClassNotFoundException ex) {
	            System.out.println("NOT FOUND!!");
	            ex.printStackTrace();
	        }
		}
        
        if(scriptResult != null) {
        	System.out.println(scriptResult.toString());
        }
		
        return scriptResult;
	}
}
