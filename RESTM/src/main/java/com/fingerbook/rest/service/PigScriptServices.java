package com.fingerbook.rest.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.scheduling.annotation.Async;

import com.fingerbook.models.pig.PigScript;
import com.fingerbook.models.pig.PigScriptResult;
import com.fingerbook.models.transfer.PigScriptList;
import com.fingerbook.models.transfer.PigScriptResultList;
import com.fingerbook.pig.svc.PigExecutorManager;

public class PigScriptServices {
	
	@SuppressWarnings("unchecked")
	public static PigScript loadPigScript(int scriptId, SessionFactory sessionFactory, Log logger) {
		
		PigScript pigScript = null;
		List<Object[]> ssScripts = null;
		Session session = null;
		
		pigScript = new PigScript();
		
		pigScript.setScriptId(scriptId);
		
		try{
			session = sessionFactory.openSession();
			
//			String sql = "select username, password, enabled from users where username = '" + username + "'";
			String sql = "SELECT `script_name`, `script_desc`, `script_file_path`, `deleted` FROM `pig_script` WHERE `script_id` = " + scriptId + "";
			
			
			Query query1 = session.createSQLQuery(sql);	
			
			ssScripts = query1.list();
			
			if(ssScripts != null && ssScripts.size() > 0) {
				
				Object[] row = (Object[])ssScripts.get(0);
				
				String scriptName = (String)row[0];
				String scriptDesc = (String)row[1];
				String scriptFilePath = (String)row[2];
				Date deleted = (Date)row[3];
				
				pigScript.setScriptName(scriptName);
				pigScript.setScriptDesc(scriptDesc);
				pigScript.setScriptFilePath(scriptFilePath);
				pigScript.setDeleted(deleted);
				
			}
			
			session.close();
			
		}catch(Exception e){
			String msg = "Loading PigScript id: " + scriptId + " failed: " + e.getMessage();
//			Response response = new Response(null, msg);
	    	logger.info(msg);
			e.printStackTrace();
			
//			return null;
		}
		
		return pigScript;
		
	}
	
	@SuppressWarnings("unchecked")
	public static List<PigScript> loadAllPigScript(SessionFactory sessionFactory, Log logger) {
		
		List<PigScript> pigScripts = new Vector<PigScript>();
		List<Object[]> ssScripts = null;
		Session session = null;
		
		try{
			session = sessionFactory.openSession();
			
//			String sql = "select username, password, enabled from users where username = '" + username + "'";
			String sql = "SELECT `script_id`, `script_name`, `script_desc`, `script_file_path`, `deleted` FROM `pig_script` ";
			
			
			Query query1 = session.createSQLQuery(sql);	
			
			ssScripts = query1.list();
			
			if(ssScripts != null && ssScripts.size() > 0) {
				
				for(int i=0; i<ssScripts.size(); i++) {
					Object[] row = (Object[])ssScripts.get(i);
					
					PigScript pigScript = null;
					pigScript = new PigScript();
					
					int scriptId = (Integer)row[0];
					String scriptName = (String)row[1];
					String scriptDesc = (String)row[2];
					String scriptFilePath = (String)row[3];
					Date deleted = (Date)row[4];
					
					pigScript.setScriptId(scriptId);
					pigScript.setScriptName(scriptName);
					pigScript.setScriptDesc(scriptDesc);
					pigScript.setScriptFilePath(scriptFilePath);
					pigScript.setDeleted(deleted);
					
					pigScripts.add(pigScript);
				}
				
			}
			
			session.close();
			
		}catch(Exception e){
			String msg = "Loading all PigScripts failed: " + e.getMessage();
//			Response response = new Response(null, msg);
	    	logger.info(msg);
			e.printStackTrace();
			
//			return null;
		}
		
		return pigScripts;
		
	}
	
	@SuppressWarnings("unchecked")
	public static PigScriptList loadPigScriptsList(SessionFactory sessionFactory, Log logger, int limit, int offset) {
		
		PigScriptList pigScriptList = new PigScriptList();
		Vector<PigScript> pigScripts = new Vector<PigScript>();
		List<Object[]> ssScripts = null;
		Session session = null;
		
		try{
			
			int total = countPigScripts(sessionFactory, logger);
			
			pigScriptList.setLimit(limit);
			pigScriptList.setOffset(offset);
			pigScriptList.setTotalresults(total);
			
			session = sessionFactory.openSession();
			
			String sql = null;
			
			if(limit > 0 && offset >= 0) {
				sql = "SELECT `script_id`, `script_name`, `script_desc`, `script_file_path`, `deleted` FROM `pig_script` LIMIT " + offset + "," + limit + "";
			}
			else {
				sql = "SELECT `script_id`, `script_name`, `script_desc`, `script_file_path`, `deleted` FROM `pig_script` ";
			}
			
			
			Query query1 = session.createSQLQuery(sql);	
			
			ssScripts = query1.list();
			
			if(ssScripts != null && ssScripts.size() > 0) {
				
				for(int i=0; i<ssScripts.size(); i++) {
					Object[] row = (Object[])ssScripts.get(i);
					
					PigScript pigScript = null;
					pigScript = new PigScript();
					
					int scriptId = (Integer)row[0];
					String scriptName = (String)row[1];
					String scriptDesc = (String)row[2];
					String scriptFilePath = (String)row[3];
					Date deleted = (Date)row[4];
					
					pigScript.setScriptId(scriptId);
					pigScript.setScriptName(scriptName);
					pigScript.setScriptDesc(scriptDesc);
					pigScript.setScriptFilePath(scriptFilePath);
					pigScript.setDeleted(deleted);
					
					pigScripts.add(pigScript);
				}
				
			}
			
			pigScriptList.setPigScripts(pigScripts);
			
			session.close();
			
		}catch(Exception e){
			String msg = "Loading PigScriptsList failed: " + e.getMessage();
//			Response response = new Response(null, msg);
	    	logger.info(msg);
			e.printStackTrace();
			
//			return null;
		}
		
		return pigScriptList;
		
	}
	
	public static PigScriptResult execPigScript(int scriptId, SessionFactory sessionFactory, Log logger) {
		
		PigScriptResult scriptResult = null;
		PigScript pigScript = null;
		
		pigScript = loadPigScript(scriptId, sessionFactory, logger);
		
		scriptResult = PigExecutorManager.execScript(pigScript);
		
		int saveRet = -1;
		saveRet = savePigScriptResult(scriptResult, sessionFactory, logger);
		
		System.out.println("execPigScript saveRet: " + saveRet);
		
		return scriptResult;
		
	}
	
	@Async
	public static void execPigScriptAsync(int scriptId, SessionFactory sessionFactory, Log logger) {
		
		System.out.println("execPigScriptAsync - 1 - " + (new Date()));
		
		PigScriptResult scriptResult = null;
		PigScript pigScript = null;
		
		pigScript = loadPigScript(scriptId, sessionFactory, logger);
		
		scriptResult = PigExecutorManager.execScript(pigScript);
		
		int saveRet = -1;
		saveRet = savePigScriptResult(scriptResult, sessionFactory, logger);
		
		System.out.println("execPigScript saveRet: " + saveRet);
		
		System.out.println("execPigScriptAsync - 2 - " + (new Date()));
		
//		return scriptResult;
		
	}
	
	public static int savePigScriptResult(PigScriptResult scriptResult, SessionFactory sessionFactory, Log logger) {
		
		int saveRet = -1;
		Session session = null;
		
		if(scriptResult != null) {
			try{
				session = sessionFactory.openSession();
				
//				String hashedPassword = FingerbookUtils.toSHA1(password);				
//				String sql = "insert into users(username, password, enabled) values ('" + 
//							username + "', '" + hashedPassword + "', true);";
//				String sql2 = "insert into authorities(username, authority) values ('" + 
//							username + "', 'ROLE_" + role + "');";
				
				String sql = "INSERT INTO `pig_script_result` (`script_id`, `return_code`, `return_message`, `successful`, `error_code`, `error_message`, `duration`, `exec_date`) values (?,?,?,?,?,?,?,?);";
				
//				session.createSQLQuery(sql).executeUpdate();
//				session.createSQLQuery(sql2).executeUpdate();
				
				Query query1 = session.createSQLQuery(sql);
				
				query1.setInteger(0, scriptResult.getPigScript().getScriptId());
				
//				query1.setInteger(1, scriptResult.getReturnCode());
				query1.setParameter(1, scriptResult.getReturnCode(), Hibernate.INTEGER);
				query1.setParameter(2, scriptResult.getReturnMessage(), Hibernate.STRING);
				
				query1.setBoolean(3, scriptResult.isSuccessful());
				
//				query1.setInteger(4, scriptResult.getErrorCode());
				query1.setParameter(4, scriptResult.getErrorCode(), Hibernate.INTEGER);
				query1.setParameter(5, scriptResult.getErrorMessage(), Hibernate.TEXT);
				
				query1.setLong(6, scriptResult.getDuration());
				
				query1.setParameter(7, scriptResult.getExecDate(), Hibernate.TIMESTAMP);
				
				saveRet = query1.executeUpdate();
				
//				scriptResult.setScriptResultId(saveRet);
		
				session.close();
				
			}catch(Exception e){
				String msg = "saving PigScriptResult failed: " + e.getMessage();
//				Response response = new Response(null, msg);
		    	logger.info(msg);
				e.printStackTrace();
				
			}
		}
		
		return saveRet;
	}
	
	@SuppressWarnings("unchecked")
	public static PigScriptResult loadPigScriptResult(int scriptResultId, boolean loadPigScript, SessionFactory sessionFactory, Log logger) {
		
//		List<PigScriptResult> pigScriptsResults = new Vector<PigScriptResult>();
		PigScriptResult scriptResult = null;
		List<Object[]> ssScriptsRes = null;
		Session session = null;
		int scriptId = -1;
//		List<PigScript> pigScripts = null;
		
		try{
			
//			if(loadPigScripts) {
//				pigScripts = loadAllPigScript(sessionFactory, logger);
//			}
			
			session = sessionFactory.openSession();
			
//			String sql = "select username, password, enabled from users where username = '" + username + "'";
			String sql = "SELECT `script_id`, `return_code`, `return_message`, `successful`, `error_code`, `error_message`, `duration`, `exec_date` FROM `pig_script_result` WHERE `script_result_id` = " + scriptResultId + "";
			
			
			Query query1 = session.createSQLQuery(sql);	
			
			ssScriptsRes = query1.list();
			
			if(ssScriptsRes != null && ssScriptsRes.size() > 0) {
				
				
				Object[] row = (Object[])ssScriptsRes.get(0);
				
//					PigScriptResult scriptResult = null;
//				PigScript pigScript = null;
				scriptResult = new PigScriptResult();
				
//					int scriptResultId = (Integer)row[0];
				scriptId = (Integer)row[0];
				Integer returnCode = (Integer)row[1];
				String returnMessage = (String)row[2];
				boolean successful = (Boolean)row[3];
				Integer errorCode = (Integer)row[4];
				String errorMessage = (String)row[5];
				long duration = ((Integer)row[6]).longValue();
				Date execDate = (Date)row[7];
				
				scriptResult.setScriptResultId(scriptResultId);
				
				
				
//				scriptResult.setPigScript(pigScript);
				
				scriptResult.setReturnCode(returnCode);
				scriptResult.setReturnMessage(returnMessage);
				scriptResult.setSuccessful(successful);
				scriptResult.setErrorCode(errorCode);
				scriptResult.setErrorMessage(errorMessage);
				scriptResult.setDuration(duration);
				scriptResult.setExecDate(execDate);
				
			}
			
			session.close();
			
			if(scriptResult != null) {
				
				PigScript pigScript = null;
				
				if(loadPigScript) {
					pigScript = loadPigScript(scriptId, sessionFactory, logger);
				}
				else {
					pigScript = new PigScript();
					pigScript.setScriptId(scriptId);
				}
				
				scriptResult.setPigScript(pigScript);
			}
			
		}catch(Exception e){
			String msg = "Loading PigScriptsResult id: " + scriptResultId + " failed: " + e.getMessage();
//			Response response = new Response(null, msg);
	    	logger.info(msg);
			e.printStackTrace();
			
//			return null;
		}
		
		return scriptResult;
		
	}
	
	@SuppressWarnings("unchecked")
	public static PigScriptResultList loadPigScriptsResultsList(SessionFactory sessionFactory, Log logger, int limit, int offset, boolean loadPigScripts) {
		
		PigScriptResultList pigScriptResultList = new PigScriptResultList();
		Vector<PigScriptResult> pigScriptsResults = new Vector<PigScriptResult>();
		List<Object[]> ssScriptsRes = null;
		Session session = null;
		List<PigScript> pigScripts = null;
		
		try{
			
			if(loadPigScripts) {
				pigScripts = loadAllPigScript(sessionFactory, logger);
			}
			
			int total = countPigScriptResults(-1, sessionFactory, logger);
			
			pigScriptResultList.setLimit(limit);
			pigScriptResultList.setOffset(offset);
			pigScriptResultList.setTotalresults(total);
			
			session = sessionFactory.openSession();
			
			String sql = null;
			
			if(limit > 0 && offset >= 0) {
				sql = "SELECT `script_result_id`, `script_id`, `return_code`, `return_message`, `successful`, `error_code`, `error_message`, `duration`, `exec_date` FROM `pig_script_result` ORDER BY script_result_id DESC LIMIT " + offset + "," + limit + "";
			}
			else {
				sql = "SELECT `script_result_id`, `script_id`, `return_code`, `return_message`, `successful`, `error_code`, `error_message`, `duration`, `exec_date` FROM `pig_script_result` ORDER BY script_result_id DESC ";
			}

			
			Query query1 = session.createSQLQuery(sql);	
			
			ssScriptsRes = query1.list();
			
			if(ssScriptsRes != null && ssScriptsRes.size() > 0) {
				
				for(int i=0; i<ssScriptsRes.size(); i++) {
					Object[] row = (Object[])ssScriptsRes.get(i);
					
					PigScriptResult scriptResult = null;
					PigScript pigScript = null;
					scriptResult = new PigScriptResult();
					
					int scriptResultId = (Integer)row[0];
					int scriptId = (Integer)row[1];
					Integer returnCode = (Integer)row[2];
					String returnMessage = (String)row[3];
					boolean successful = (Boolean)row[4];
					Integer errorCode = (Integer)row[5];
					String errorMessage = (String)row[6];
					long duration = ((Integer)row[7]).longValue();
					Date execDate = (Date)row[8];
					
					scriptResult.setScriptResultId(scriptResultId);
					
					if(loadPigScripts) {
						pigScript = getPigScript(scriptId, pigScripts);
					}
					else {
						pigScript = new PigScript();
						pigScript.setScriptId(scriptId);
					}
					
					scriptResult.setPigScript(pigScript);
					
					scriptResult.setReturnCode(returnCode);
					scriptResult.setReturnMessage(returnMessage);
					scriptResult.setSuccessful(successful);
					scriptResult.setErrorCode(errorCode);
					scriptResult.setErrorMessage(errorMessage);
					scriptResult.setDuration(duration);
					scriptResult.setExecDate(execDate);
					
					pigScriptsResults.add(scriptResult);
				}
				
			}
			
			pigScriptResultList.setPigScriptResults(pigScriptsResults);
			
			session.close();
			
		}catch(Exception e){
			String msg = "Loading PigScriptsResultsList failed: " + e.getMessage();
//			Response response = new Response(null, msg);
	    	logger.info(msg);
			e.printStackTrace();
			
//			return null;
		}
		
		return pigScriptResultList;
	}
	
	@SuppressWarnings("unchecked")
	public static List<PigScriptResult> loadAllPigScriptResults(boolean loadPigScripts, SessionFactory sessionFactory, Log logger) {
		
		List<PigScriptResult> pigScriptsResults = new Vector<PigScriptResult>();
		List<Object[]> ssScriptsRes = null;
		Session session = null;
		List<PigScript> pigScripts = null;
		
		try{
			
			if(loadPigScripts) {
				pigScripts = loadAllPigScript(sessionFactory, logger);
			}
			
			session = sessionFactory.openSession();
			
//			String sql = "select username, password, enabled from users where username = '" + username + "'";
			String sql = "SELECT `script_result_id`, `script_id`, `return_code`, `return_message`, `successful`, `error_code`, `error_message`, `duration`, `exec_date` FROM `pig_script_result` ";
			
			
			Query query1 = session.createSQLQuery(sql);	
			
			ssScriptsRes = query1.list();
			
			if(ssScriptsRes != null && ssScriptsRes.size() > 0) {
				
				for(int i=0; i<ssScriptsRes.size(); i++) {
					Object[] row = (Object[])ssScriptsRes.get(i);
					
					PigScriptResult scriptResult = null;
					PigScript pigScript = null;
					scriptResult = new PigScriptResult();
					
					int scriptResultId = (Integer)row[0];
					int scriptId = (Integer)row[1];
					Integer returnCode = (Integer)row[2];
					String returnMessage = (String)row[3];
					boolean successful = (Boolean)row[4];
					Integer errorCode = (Integer)row[5];
					String errorMessage = (String)row[6];
					long duration = ((Integer)row[7]).longValue();
					Date execDate = (Date)row[8];
					
					scriptResult.setScriptResultId(scriptResultId);
					
					if(loadPigScripts) {
						pigScript = getPigScript(scriptId, pigScripts);
					}
					else {
						pigScript = new PigScript();
						pigScript.setScriptId(scriptId);
					}
					
					scriptResult.setPigScript(pigScript);
					
					scriptResult.setReturnCode(returnCode);
					scriptResult.setReturnMessage(returnMessage);
					scriptResult.setSuccessful(successful);
					scriptResult.setErrorCode(errorCode);
					scriptResult.setErrorMessage(errorMessage);
					scriptResult.setDuration(duration);
					scriptResult.setExecDate(execDate);
					
					pigScriptsResults.add(scriptResult);
				}
				
			}
			
			session.close();
			
		}catch(Exception e){
			String msg = "Loading all PigScriptsResults failed: " + e.getMessage();
//			Response response = new Response(null, msg);
	    	logger.info(msg);
			e.printStackTrace();
			
//			return null;
		}
		
		return pigScriptsResults;
		
	}
	
	@SuppressWarnings("unchecked")
	public static List<PigScriptResult> loadPigScriptResultsByScriptId(int scriptId, boolean loadPigScripts, SessionFactory sessionFactory, Log logger) {
		
		List<PigScriptResult> pigScriptsResults = new Vector<PigScriptResult>();
		List<Object[]> ssScriptsRes = null;
		Session session = null;
//		List<PigScript> pigScripts = null;
		PigScript pigScript = null;
		
		try{
			
			if(loadPigScripts) {
				pigScript = loadPigScript(scriptId, sessionFactory, logger);
			}
			else {
				pigScript = new PigScript();
				pigScript.setScriptId(scriptId);
			}
			
			session = sessionFactory.openSession();
			
//			String sql = "select username, password, enabled from users where username = '" + username + "'";
			String sql = "SELECT `script_result_id`, `return_code`, `return_message`, `successful`, `error_code`, `error_message`, `duration`, `exec_date` FROM `pig_script_result` WHERE `script_id` = " + scriptId + "";
			
			
			Query query1 = session.createSQLQuery(sql);	
			
			ssScriptsRes = query1.list();
			
			if(ssScriptsRes != null && ssScriptsRes.size() > 0) {
				
				for(int i=0; i<ssScriptsRes.size(); i++) {
					Object[] row = (Object[])ssScriptsRes.get(i);
					
					PigScriptResult scriptResult = null;
//					PigScript pigScript = null;
					scriptResult = new PigScriptResult();
					
					int scriptResultId = (Integer)row[0];
//					int scriptId = (Integer)row[1];
					Integer returnCode = (Integer)row[1];
					String returnMessage = (String)row[2];
					boolean successful = (Boolean)row[3];
					Integer errorCode = (Integer)row[4];
					String errorMessage = (String)row[5];
					long duration = ((Integer)row[6]).longValue();
					Date execDate = (Date)row[7];
					
					scriptResult.setScriptResultId(scriptResultId);
					
					scriptResult.setPigScript(pigScript);
					
					scriptResult.setReturnCode(returnCode);
					scriptResult.setReturnMessage(returnMessage);
					scriptResult.setSuccessful(successful);
					scriptResult.setErrorCode(errorCode);
					scriptResult.setErrorMessage(errorMessage);
					scriptResult.setDuration(duration);
					scriptResult.setExecDate(execDate);
					
					pigScriptsResults.add(scriptResult);
				}
				
			}
			
			session.close();
			
		}catch(Exception e){
			String msg = "Loading PigScriptResultsByScriptId id: " + scriptId + " failed: " + e.getMessage();
//			Response response = new Response(null, msg);
	    	logger.info(msg);
			e.printStackTrace();
			
//			return null;
		}
		
		return pigScriptsResults;
		
	}
	
	public static PigScript getPigScript(int scriptId, List<PigScript> pigScripts) {
		
		PigScript pigScript = null;
		
		if(pigScripts != null) {
			
			for(PigScript auxPigScript: pigScripts) {
				
				if(auxPigScript.getScriptId() == scriptId) {
					
					pigScript = auxPigScript;
					break;
				}
			}
		}
		
		
		return pigScript;
		
	}
	
	public static int countPigScripts(SessionFactory sessionFactory, Log logger) {
		
		int total = 0;
		Session session = null;
		
		
		try{
			session = sessionFactory.openSession();
			
//			String sql = "select username, password, enabled from users where username = '" + username + "'";
			String sql = "SELECT COUNT(*) FROM `pig_script` ";
			
			
			Query query1 = session.createSQLQuery(sql);	
			
//			ssScripts = query1.list();
			total = ((BigInteger) query1.uniqueResult()).intValue();
			
			session.close();
			
		}catch(Exception e){
			String msg = "Count PigScripts failed: " + e.getMessage();
//			Response response = new Response(null, msg);
	    	logger.info(msg);
			e.printStackTrace();
			
//			return null;
		}
		
		return total;
		
	}
	
	public static int countPigScriptResults(int scriptId, SessionFactory sessionFactory, Log logger) {
		
		int total = 0;
		Session session = null;
		
		
		try{
			session = sessionFactory.openSession();
			
			String sql = null;
			
			if(scriptId > 0) {
				sql = "SELECT COUNT(*) FROM `pig_script_result` WHERE `script_id` = " + scriptId + "";
			}
			else {
				sql = "SELECT COUNT(*) FROM `pig_script_result` ";
			}
			
			
			Query query1 = session.createSQLQuery(sql);	
			
//			ssScripts = query1.list();
			total = ((BigInteger) query1.uniqueResult()).intValue();
			
			session.close();
			
		}catch(Exception e){
			String msg = "Count PigScriptResults failed: " + e.getMessage();
//			Response response = new Response(null, msg);
	    	logger.info(msg);
			e.printStackTrace();
			
//			return null;
		}
		
		return total;
		
	}

}
