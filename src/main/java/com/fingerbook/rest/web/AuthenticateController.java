package com.fingerbook.rest.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fingerbook.models.Response;

@Controller
@RequestMapping("/authenticate")
public class AuthenticateController {
	
    protected final Log logger = LogFactory.getLog(getClass());

    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody
	public Response authenticate(@RequestParam("username") String username, 
			@RequestParam("password") String password,
			Model model) {
    	
    	// If Spring security allowed to access this resource, then authentication
    	// is already succesful
    	String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    	String msg = "Authentication succesful for user: " +  username + ", password: " + password + ", role: "
    		+ role;
    	Response response = new Response(null, msg);
    	logger.info(msg);
    	
		return response;
	}
}
