package com.fingerbook.pig;

import java.io.File;
import java.io.IOException;

import org.apache.pig.PigRunner;
import org.apache.pig.PigServer;
import org.apache.pig.scripting.BoundScript;
import org.apache.pig.scripting.Pig;
import org.apache.pig.tools.pigstats.PigStats;

import com.fingerbook.models.pig.PigScriptResult;
import com.fingerbook.pig.svc.PigExecutorManager;

public class PigExecutor {
	
	public static void main(String[] args) {
		
		String scriptName = "script.pig";
		String scriptFilePath = "bin/script.pig";
		
		PigScriptResult scriptResult = null;
		
		System.out.println("START: PigExecutor");
		
//		scriptResult = PigExecutorManager.execScript(scriptName, scriptFilePath);
		
		System.out.println("END: PigExecutor");
	}
	
	public static void mainOLD(String[] args) {
		 try {
	            Class klass = Class.forName("org.apache.pig.PigRunner");
	            
//	            String script = "/home/pampa/Documents/workspace4/PigExecutor/bin/script.pig";
	            String script = "bin/script.pig";
	            
//	            if (!new File(script).exists()) {
//	                throw new RuntimeException("Error: Pig script file [" + script + "] does not exist");
//	            }
	            
	            File scriptFile = new File(script);
	            
	            String parentPath = scriptFile.getParentFile().getAbsolutePath();
	            String path = scriptFile.getPath();
	            String absolutePath = scriptFile.getAbsolutePath();
	            String canonicalPath = "EMPTY";
	            try {
					canonicalPath = scriptFile.getCanonicalPath();
				} catch (IOException e) {
					canonicalPath = "IOException";
					e.printStackTrace();
				}
				
				System.out.println("parentPath: " + parentPath);
				System.out.println("path: " + path);
				System.out.println("absolutePath: " + absolutePath);
				System.out.println("canonicalPath: " + canonicalPath);
	            
	            if (!scriptFile.exists()) {
	                throw new RuntimeException("Error: Pig script file [" + script + "] does not exist");
	            }
	            
//	            PigStats stats = org.apache.pig.PigRunner.run( new String[] { "-version" }, null);
//	            PigStats stats = PigRunner.run( new String[] { "-x", "local", "-file", script, "-logfile", "/home/pampa/Documents/workspace4/PigExecutor/bin" }, null);
	            PigStats stats = PigRunner.run( new String[] { "-x", "local", "-file", script, "-logfile", parentPath }, null);
	            
	            // isSuccessful is the API from 0.9 supported by both PigStats and
	            // EmbeddedPigStats
	            if (!stats.isSuccessful()) {
	             System.out.println("MAAL");
	             System.out.println(stats.getErrorCode() + " " + stats.getErrorMessage());
	            } else {
	            	System.out.println("BIEEN!");
	            }
	        }
	        catch (ClassNotFoundException ex) {
	            System.out.println("NOT FOUND!!");;
	        }

//		System.exit(0);
//		PigServer pigServer;
//		try {
//			pigServer = new PigServer(ExecType.LOCAL);
//			try {
//				ScriptPigContext.set(pigServer.getPigContext(), ScriptEngine.getInstance("jython"));
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			try {
//				runMyQuery(pigServer);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} catch (ExecException e1) {
//			e1.printStackTrace();
//		}
	}

	public static void runMyQuery(PigServer pigServer)
			throws IOException {
//		pigServer.registerQuery("loaded1 = LOAD 'hbase://tfinger' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage" +
//				"('group_str:*', '-loadKey true') AS (id:chararray, group:map[]);");
//		pigServer.store("loaded1", "myoutput");
		BoundScript bs = Pig.compile("loaded1 = LOAD 'hbase://tfinger' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage" +
				"('group_str:*', '-loadKey true') AS (id:chararray, group:map[]);\n dump loaded1; ").bind();
		PigStats ps = bs.runSingle();
		if(ps.isSuccessful()) {
			System.out.println("TERMINO BIEN!");
		} else {
			System.out.println("FALLO!!");
		}
	}
}

