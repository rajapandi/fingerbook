package com.fingerbook.rest.web;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Response;
import com.fingerbook.models.SpringSecurityAuthority;
import com.fingerbook.models.SpringSecurityUser;
import com.fingerbook.models.SpringSecurityUserMerge;
import com.fingerbook.models.UserInfo;
import com.fingerbook.rest.back.FingerbookUtils;
import com.fingerbook.rest.service.AdminServices;
import com.fingerbook.rest.service.FingerbookServices;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	// DI
    private SessionFactory sessionFactory;
    // DI
    private AdminServices adminService;
	
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
 
    @RequestMapping(value="/listUsers/{username}", method=RequestMethod.GET)
    @ResponseBody
    public SpringSecurityUser listUserById(@PathVariable("username") String username, Model model) {    
    	
    	SpringSecurityUser user = new SpringSecurityUser();
    	List<Object[]> ssUsers = null;
    	List<SpringSecurityAuthority> ssAuthority = null;
	
    	String msg;
    	Session session = null;
	
		try{
			session = sessionFactory.openSession();		
			String sql = "select username, password, enabled from users where username = '" + username + "'";
			
			Query query1 = session.createSQLQuery(sql);	
			
			ssUsers = query1.list();
					
			for(int i=0; i<ssUsers.size(); i++) {
				
				Object[] row = (Object[])ssUsers.get(i);  
				user.setId(new Integer(i).toString());
				user.setUsername((String)row[0]);
				user.setPassword((String)row[1]);
				//TODO Hardwired
				user.setEnabled(true);
			}
			
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
}
