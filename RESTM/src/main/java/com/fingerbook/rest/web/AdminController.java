package com.fingerbook.rest.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fingerbook.models.Response;
import com.fingerbook.rest.back.FingerbookUtils;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	// DI
    private SessionFactory sessionFactory;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
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
			
			String sql = "insert into users(username, password, enabled) values ('" + 
						username + "', '" + hashedPassword + "', true);";
			String sql2 = "insert into authorities(username, authority) values ('" + 
						username + "', 'ROLE_" + role + "');";
			
			session.createSQLQuery(sql).executeUpdate();
			session.createSQLQuery(sql2).executeUpdate();	
	
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
    
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

    
}
