package com.fingerbook.rest.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Response;
import com.fingerbook.models.SpringSecurityAuthority;
import com.fingerbook.models.SpringSecurityUser;
import com.fingerbook.models.pig.PigScript;
import com.fingerbook.models.pig.PigScriptResult;
import com.fingerbook.models.transfer.PigScriptFeed;
import com.fingerbook.models.transfer.PigScriptList;
import com.fingerbook.models.transfer.PigScriptResultFeed;
import com.fingerbook.models.transfer.PigScriptResultList;
import com.fingerbook.rest.back.FingerbookUtils;
import com.fingerbook.rest.service.AdminServices;
import com.fingerbook.rest.service.PigScriptServices;
import com.fingerbook.rest.task.PigScriptExecutorTask;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	// DI
    private SessionFactory sessionFactory;
    // DI
    private AdminServices adminService;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private TaskExecutor taskExecutor;
	
    @RequestMapping(value="/createUser", method=RequestMethod.POST)
    @ResponseBody
	public Response createUser(@RequestParam("username") String username, 
			@RequestParam("password") String password, @RequestParam("role") String role,
			Model model) {
    	
    	String msg = "Creating user: " +  username + " with role: " + role;
    	
    	if(!FingerbookUtils.isValidRole(role)) {
    		msg = "Creating user " + username + " failed: Invalid role: " + role;
			Response response = new Response(null, msg);
	    	logger.info(msg);
			return response;
    	}
    	
    	Session session = null;
		
		try{
			session = sessionFactory.openSession();
			
			String hashedPassword = FingerbookUtils.toSHA1(password);
			
			String sql = "insert into users(username, password, enabled) values (?, ?, true);";
			String sql2 = "insert into authorities(username, authority) values (?, ?);";
			
			// Create queries
			Query query1 = session.createSQLQuery(sql);
			Query query2 = session.createSQLQuery(sql2);
						
			// Set parameters
			query1.setParameter(0, username, Hibernate.STRING);
			query1.setParameter(1, hashedPassword, Hibernate.STRING);
			
			query2.setParameter(0, username, Hibernate.STRING);
			query2.setParameter(1, "ROLE_" + role, Hibernate.STRING);	
	
			// Execute queries
			query1.executeUpdate();
			query2.executeUpdate();
			
			session.close();
			
		}catch(Exception e){
			msg = "Creating user " + username + " failed: " + e.getMessage();
			Response response = new Response(null, msg);
	    	logger.info(msg);
			e.printStackTrace();
			
			return response;
		}
    	
		msg = "Creating user " + username + " success!";
    	Response response = new Response(null, msg);
    	logger.info(msg);
    	
		return response;
	}
    
    // TODO: Deleting an unexistant user throws a succesfull operation message
    @RequestMapping(value="/deleteUser", method=RequestMethod.POST)
    @ResponseBody
	public Response deleteUser(@RequestParam("username") String username,
			Model model) {
    	
    	String msg = "Deleting user: " +  username;
    	
    	Session session = null;
		
		try{
			session = sessionFactory.openSession();
			
			String sql = "delete from users where username = '" + username + "';";
			String sql2 = "delete from authorities where username = '" + username + "';";
			
			session.createSQLQuery(sql2).executeUpdate();
			session.createSQLQuery(sql).executeUpdate();	
	
			session.close();
			
		}catch(Exception e){
			msg = "Deleting user " + username + " failed: " + e.getMessage();
			Response response = new Response(null, msg);
	    	logger.info(msg);
			e.printStackTrace();
			
			return response;
		}
    	
		msg = "Deleting user " + username + " success!";
    	Response response = new Response(null, msg);
    	logger.info(msg);
    	
		return response;
	}    

    @RequestMapping(value="/modifyUser", method=RequestMethod.POST)
    @ResponseBody
	public Response modifyUser(@RequestParam("username") String username, 
			@RequestParam("password") String password, @RequestParam("role") String role,
			@RequestParam("enabled") String enabled,
			Model model) {
    	
    	// Don-t allow empty password
    	if(password.isEmpty()) {
        	String msg = "Empty passwords are not allowed. Please choose a password.";
    		Response response = new Response(null, msg);
        	logger.info(msg);	
    		return response;
    	}
    	
    	String msg = "Modifying user: " +  username + " with values: (role, " + role + 
    		") and (enabled, " + enabled + ")";
    	
    	
    	Session session = null;
		
		try{
			session = sessionFactory.openSession();
			
			String hashedPassword = FingerbookUtils.toSHA1(password);
			
			String sql = "update users set password='" + hashedPassword + "', enabled=" +
				enabled + " where username = '" + username + "';";
			String sql2 = "update authorities set authority='" +
				 role + "' where username = '" + username + "';";
			
			session.createSQLQuery(sql).executeUpdate();
			session.createSQLQuery(sql2).executeUpdate();	
	
			session.close();
			
		}catch(Exception e){
			msg = "Modifying user " + username + " failed: " + e.getMessage();
			Response response = new Response(null, msg);
	    	logger.info(msg);
			e.printStackTrace();
			
			return response;
		}
    	
		msg = "Modifying user " + username + " success!";
    	Response response = new Response(null, msg);
    	logger.info(msg);
    	
		return response;
	}    
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value="/listUsers", method=RequestMethod.GET)
    @ResponseBody
    public List<SpringSecurityUser> fingerbooksByTicket(Model model) {    
    	
    	List<SpringSecurityUser> users = new Vector<SpringSecurityUser>();
    	List<Object[]> ssUsers = null;
    	List<SpringSecurityAuthority> ssAuthority = null;
	
    	String msg;
    	Session session = null;
	
		try{
			session = sessionFactory.openSession();
			
			String sql = "select username, password, enabled from users";
			//String sql = "from users" ;
			String sql2 = "select  username, authority from authorities";
			
			Query query1 = session.createSQLQuery(sql);
			//Query query1 = session.createQuery(sql);
			Query query2 = session.createSQLQuery(sql2);	
			
			ssUsers = query1.list();
			ssAuthority = query2.list();
					
			for(int i=0; i<ssUsers.size(); i++) {
				SpringSecurityUser user = new SpringSecurityUser();
				
				Object[] row = (Object[])ssUsers.get(i);  
				user.setId(new Integer(i).toString());
				user.setUsername((String)row[0]);
				user.setPassword((String)row[1]);
				//TODO Hardwired
				user.setEnabled(true);
				
				users.add(user);
			}
			
			session.close();
			
		}catch(Exception e){
			msg = "Listing users failed: " + e.getMessage();
			Response response = new Response(null, msg);
	    	logger.info(msg);
			e.printStackTrace();
			
			return null;
		}
    	
    	model.addAttribute("users", users);
    	logger.info("Returning users");
    	
    	return users;
    }
 
    @SuppressWarnings("unchecked")
	@RequestMapping(value="/listUsers/{username}", method=RequestMethod.GET)
    @ResponseBody
    public SpringSecurityUser listUserById(@PathVariable("username") String username, Model model) {    
    	
    	SpringSecurityUser user = new SpringSecurityUser();
    	List<Object[]> ssUsers = null;
    	//List<SpringSecurityAuthority> ssAuthority = null;
	
    	String msg;
    	Session session = null;
	
		try{
			session = sessionFactory.openSession();		
			String sql = "select username, password, enabled from users where username = '" + username + "'";
			
			// Get password
			Query query1 = session.createSQLQuery(sql);	
			ssUsers = query1.list();	
			for(int i=0; i<ssUsers.size(); i++) {
				Object[] row = (Object[])ssUsers.get(i);  
				user.setId(new Integer(i).toString());
				user.setUsername((String)row[0]);
				user.setPassword((String)row[1]);
				System.out.println(String.valueOf(row[2]));
				boolean enabled = String.valueOf(row[2]).equals("true") ? true : false;
				user.setEnabled(enabled);
			}
			
			// Get role
			sql = "select username, authority from authorities where username = '" + username + "'";
			Query query2 = session.createSQLQuery(sql);	
			ssUsers = query2.list();
			Object[] row = (Object[])ssUsers.get(0);
			user.setRole((String)row[1]);
	
			
			session.close();
			
		}catch(Exception e){
			msg = "Listing users failed: " + e.getMessage();
			Response response = new Response(null, msg);
	    	logger.info(msg);
			e.printStackTrace();
			
			return null;
		}
    	
    	model.addAttribute("user", user);
    	logger.info("Returning user " + username);
    	
    	return user;
    }
    
	@RequestMapping(value="/script/showscript/{id}", method=RequestMethod.GET)
    @ResponseBody
    public PigScript showPigScriptById(@PathVariable("id") int scriptId, Model model) {
    	
    	PigScript pigScript = null;
    	
    	pigScript = PigScriptServices.loadPigScript(scriptId, sessionFactory, logger);
    	
    	model.addAttribute("pigScript", pigScript);
//    	logger.info("Returning user " + username);
    	
    	return pigScript;
    }
	
//	@RequestMapping(value="/script/scriptfeed/{id}", method=RequestMethod.GET)
//	public PigScriptFeed getPigScriptFeedById(@PathVariable("id") int scriptId, Model model) {
	@RequestMapping(value="/script/scriptfeed", method=RequestMethod.GET)
    @ResponseBody
    public PigScriptFeed getPigScriptFeedById(@RequestParam("id") int scriptId, Model model) {
    	
		PigScriptFeed pigScriptFeed = null;
    	PigScript pigScript = null;
    	
    	pigScriptFeed = new PigScriptFeed();
    	
    	pigScript = PigScriptServices.loadPigScript(scriptId, sessionFactory, logger);
    	
    	pigScriptFeed.setPigScript(pigScript);
    	
    	model.addAttribute("pigScriptFeed", pigScriptFeed);
    	
    	return pigScriptFeed;
    }
	
	
	@RequestMapping(value="/script/showallscripts", method=RequestMethod.GET)
    @ResponseBody
    public List<PigScript> showAllPigScripts(Model model) {
    	
    	List<PigScript> pigScripts = null;
    	
    	pigScripts = PigScriptServices.loadAllPigScript(sessionFactory, logger);
    	
    	model.addAttribute("pigScripts", pigScripts);
//    	logger.info("Returning user " + username);
    	
    	return pigScripts;
    }
	
	@RequestMapping(value="/script/scriptslist/limit/{limit}/offset/{offset}", method=RequestMethod.GET)
    @ResponseBody
    public PigScriptList getPigScriptsList(@PathVariable("limit") int limit, @PathVariable("offset") int offset, Model model) {
    	
    	PigScriptList pigScriptList = null;
    	
//    	pigScripts = PigScriptServices.loadAllPigScript(sessionFactory, logger);
    	pigScriptList = PigScriptServices.loadPigScriptsList(sessionFactory, logger, limit, offset);
    	
    	model.addAttribute("pigScriptList", pigScriptList);
//    	logger.info("Returning user " + username);
    	
    	return pigScriptList;
    }
	
	@RequestMapping(value="/script/execscript/{id}", method=RequestMethod.GET)
    @ResponseBody
    public PigScriptResult execPigScriptById(@PathVariable("id") int scriptId, Model model) {
    	
		PigScriptResult scriptResult = null;
    	
		scriptResult = PigScriptServices.execPigScript(scriptId, sessionFactory, logger);
    	
    	model.addAttribute("scriptResult", scriptResult);
//    	logger.info("Returning user " + username);
    	
    	return scriptResult;
    }
	
//	@RequestMapping(value="/script/execscripttask/{id}", method=RequestMethod.GET)
//	@ResponseBody
//    public Response execPigScriptByIdTask(@PathVariable("id") int scriptId, Model model) {
    @RequestMapping(value="/script/execscripttask", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Response execPigScriptByIdTask(@RequestParam("id") int scriptId, Model model) {
		
		String msg = null;
		Response response = null;
		
		System.out.println("execPigScriptByIdTask - ID: " + scriptId);
    			
		System.out.println("execPigScriptByIdTask - 1 - " + (new Date()));
				
		try {
			taskExecutor.execute(new PigScriptExecutorTask(scriptId, sessionFactory, logger));
			
			msg = "Starting pig script id: " + scriptId + " execution success!";
	    	response = new Response(null, msg);
	    	
	    	System.out.println("execPigScriptByIdTask - 2 - " + (new Date()));
			
		} catch (Exception e) {
			e.printStackTrace();
			
			msg = "Starting pig script id: " + scriptId + " execution failed";
	    	response = new Response(15, msg);
	    	
	    	System.out.println("execPigScriptByIdTask - 2 FAIL - " + (new Date()));
		}
		
		
    	return response;
    }
    
    /* URL_PIG_SCRIPT_UPDATE */
    @RequestMapping(value="/script/updatescript", method=RequestMethod.POST)
    @ResponseBody
    public Response updatePigScript(@RequestBody PigScript pigScript) {    
    	
    	Response response = null;
    	String msg = null;
    	int ret = -1;
    	int scriptId = -1;

    	if(pigScript == null) {
    		
    		msg = "Updating pig script failed. Reason: Empty script";
	    	response = new Response(16, msg);
	    	
	    	return response;
    	}
    	
    	scriptId = pigScript.getScriptId();
    	
    	if(scriptId < 0) {
    		
    		msg = "Updating pig script failed. Reason: Invalid script id: " + scriptId;
	    	response = new Response(16, msg);
	    	
	    	return response;
    	}
    	
    	if(pigScript.getScriptName() == null) {
    		
    		msg = "Updating pig script id: " + scriptId + " failed. Reason: Empty name";
	    	response = new Response(16, msg);
	    	
	    	return response;
    	}
    	
    	if(pigScript.getScriptFilePath() == null) {
    		
    		msg = "Updating pig script id: " + scriptId + " failed. Reason: Empty file path";
	    	response = new Response(16, msg);
	    	
	    	return response;
    	}
    	
    	ret = PigScriptServices.updatePigScript(pigScript, sessionFactory, logger);
    	
    	if(ret > 0) {
    		
    		msg = "Updating pig script id: " + scriptId + " success!";
	    	response = new Response(null, msg);
	    	
    	}
    	else {
    		
    		msg = "Updating pig script id: " + scriptId + " failed";
	    	response = new Response(16, msg);
    	}
    	
    	return response;
    }
	
	@RequestMapping(value="/script/execscriptasync/{id}", method=RequestMethod.GET)
    @ResponseBody
    public void execPigScriptByIdAsync(@PathVariable("id") int scriptId, Model model) {
    	
//		PigScriptResult scriptResult = null;
//		scriptResult = PigScriptServices.execPigScript(scriptId, sessionFactory, logger);
		
		System.out.println("execPigScriptByIdAsync - 1 - " + (new Date()));
		
//		doExecPigScriptByIdAsync(scriptId);
		PigScriptServices.execPigScriptAsync(scriptId, sessionFactory, logger);
		
		System.out.println("execPigScriptByIdAsync - 2 - " + (new Date()));
    	
    	model.addAttribute("executed", "ok");
    	
    	System.out.println("execPigScriptByIdAsync - 3 - " + (new Date()));
    	
//    	return scriptResult;
    }
	
	@RequestMapping(value="/script/execscriptcall/{id}", method=RequestMethod.GET)
    @ResponseBody
    public Callable<PigScriptResult> execPigScriptByIdCall(@PathVariable("id") final int scriptId, final Model model) {
		
		System.out.println("execPigScriptByIdCall - 1 - " + (new Date()));
    	
		return new Callable<PigScriptResult>() {
			
			public PigScriptResult call() throws Exception {
				
				System.out.println("execPigScriptByIdCall - 2 - " + (new Date()));
			
				PigScriptResult scriptResult = null;
		    	
				scriptResult = PigScriptServices.execPigScript(scriptId, sessionFactory, logger);
		    	
		    	model.addAttribute("scriptResult", scriptResult);
		    	
		    	System.out.println("execPigScriptByIdCall - 3 - " + (new Date()));
		    	
		    	return scriptResult;
			}
		};
    	
		
    }
	
	@Async
	void doExecPigScriptByIdAsync(int scriptId) {
    	
		System.out.println("doExecPigScriptByIdAsync - 1 - " + (new Date()));
		PigScriptResult scriptResult = null;
    	
		scriptResult = PigScriptServices.execPigScript(scriptId, sessionFactory, logger);
		
		System.out.println("doExecPigScriptByIdAsync - 2 - " + (new Date()));
    	
//    	model.addAttribute("scriptResult", scriptResult);
    	
//    	return scriptResult;
    }
	
	@RequestMapping(value="/script/showscriptres/{id}", method=RequestMethod.GET)
    @ResponseBody
    public PigScriptResult showPigScriptResById(@PathVariable("id") int scriptResultId, Model model) {
    	
		PigScriptResult scriptResult = null;
    	
		scriptResult = PigScriptServices.loadPigScriptResult(scriptResultId, true, sessionFactory, logger);
    	
    	model.addAttribute("scriptResult", scriptResult);
//    	logger.info("Returning user " + username);
    	
    	return scriptResult;
    }
	
//	@RequestMapping(value="/script/scriptresfeed/{id}", method=RequestMethod.GET)
//	public PigScriptResultFeed getPigScriptResultFeedById(@PathVariable("id") int scriptResultId, Model model) {
	@RequestMapping(value="/script/scriptresfeed", method=RequestMethod.GET)
    @ResponseBody
    public PigScriptResultFeed getPigScriptResultFeedById(@RequestParam("id") int scriptResultId, Model model) {
    	
		PigScriptResultFeed pigScriptResultFeed = null;
		PigScriptResult scriptResult = null;
    	
		scriptResult = PigScriptServices.loadPigScriptResult(scriptResultId, true, sessionFactory, logger);
    	
		pigScriptResultFeed = new PigScriptResultFeed();
    	
		pigScriptResultFeed.setScriptResult(scriptResult);
    	
    	model.addAttribute("pigScriptResultFeed", pigScriptResultFeed);
    	
    	return pigScriptResultFeed;
    }
	
	@RequestMapping(value="/script/showallscriptsres", method=RequestMethod.GET)
    @ResponseBody
    public List<PigScriptResult> showAllPigScriptsRes(Model model) {
    	
    	List<PigScriptResult> pigScriptResults = null;
    	
    	pigScriptResults = PigScriptServices.loadAllPigScriptResults(true, sessionFactory, logger);
    	
    	model.addAttribute("pigScriptResults", pigScriptResults);
//    	logger.info("Returning user " + username);
    	
    	return pigScriptResults;
    }
	
	@RequestMapping(value="/script/scriptsreslist/limit/{limit}/offset/{offset}", method=RequestMethod.GET)
    @ResponseBody
    public PigScriptResultList getPigScriptsResultsList(@PathVariable("limit") int limit, @PathVariable("offset") int offset, Model model) {
    	
//    	List<PigScriptResult> pigScriptResults = null;
    	PigScriptResultList pigScriptResultList = new PigScriptResultList();
    	
//    	pigScriptResults = PigScriptServices.loadAllPigScriptResults(true, sessionFactory, logger);
    	pigScriptResultList = PigScriptServices.loadPigScriptsResultsList(sessionFactory, logger, limit, offset, true);
    	
    	model.addAttribute("pigScriptResultList", pigScriptResultList);
//    	logger.info("Returning user " + username);
    	
    	return pigScriptResultList;
    }
	
	@RequestMapping(value="/script/showallscriptsres/scrid/{id}", method=RequestMethod.GET)
    @ResponseBody
    public List<PigScriptResult> showAllPigScriptsResByScriptId(@PathVariable("id") int scriptId, Model model) {
    	
    	List<PigScriptResult> pigScriptResults = null;
    	
    	pigScriptResults = PigScriptServices.loadPigScriptResultsByScriptId(scriptId, true, sessionFactory, logger);
    	
    	model.addAttribute("pigScriptResults", pigScriptResults);
//    	logger.info("Returning user " + username);
    	
    	return pigScriptResults;
    }
    
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public AdminServices getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminServices adminService) {
		this.adminService = adminService;
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
		
		System.out.println("setTaskExecutor - " + (new Date()));
	}
	
}
