package com.fingerbook.rest.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fingerbook.models.Response;

@Controller
@RequestMapping("/admin")
public class Admincontroller {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
    @RequestMapping(value="/createUser/{username}/{password}", method=RequestMethod.GET)
    @ResponseBody
	public Response createUser(@PathVariable("username") String username, 
			@PathVariable("password") String password,
			Model model) {
    	
    	String msg = "Creating user: " +  username + ", password: " + password;
    	Response response = new Response(null, msg);
    	logger.info(msg);
    	
		return response;
	}

}
