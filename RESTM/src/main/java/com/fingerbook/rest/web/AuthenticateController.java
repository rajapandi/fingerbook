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
@RequestMapping("/authenticate")
public class AuthenticateController {
	
    protected final Log logger = LogFactory.getLog(getClass());

    // TODO: username and password should be sent with POST
    @RequestMapping(value="/{username}/{password}", method=RequestMethod.GET)
    @ResponseBody
	public Response authenticate(@PathVariable("username") String username, 
			@PathVariable("password") String password,
			Model model) {
    	
    	String msg = "Authentication succesful for user: " +  username + ", password: " + password;
    	Response response = new Response(null, msg);
    	logger.info(msg);
    	
		return response;
	}
}
