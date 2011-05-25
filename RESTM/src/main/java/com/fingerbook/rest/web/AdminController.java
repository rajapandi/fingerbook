package com.fingerbook.rest.web;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fingerbook.models.Response;
import com.fingerbook.rest.domain.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
    private SessionFactory sessionFactory;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
    @RequestMapping(value="/createUser/{username}/{password}", method=RequestMethod.GET)
    @ResponseBody
	public Response createUser(@PathVariable("username") String username, 
			@PathVariable("password") String password,
			Model model) {
    	
    	String msg = "Creating user: " +  username + ", password: " + password;
    	
    	// --------------------------------------------------------------------------------
    	Session session = null;
		
		try{
			// This step will read data.xml and prepare hibernate for use
			//File configFile = new File("classpath:data.xml");
			session = sessionFactory.openSession();
			
			/* TODO: Password is always admin*/
			String sql = "insert into users(username, password, enabled) values ('" + 
						username + "', 'd033e22ae348aeb5660fc2140aec35850c4da997', true);";
			String sql2 = "insert into authorities(username, authority) values ('" + 
						username + "', 'ROLE_USER');";
			
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
    	
		// --------------------------------------------------------------------------------
    	
		msg = "Creating user " + username + " success!";
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
