package com.fingerbook.web;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/deleteUser/**")
@Controller
public class DeleteUserController {

	@RequestMapping(method = RequestMethod.POST)
    public String post(@RequestParam("username") String username,
			ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    	
    	String result = "\"";
    	try {
    		
    		String authenticatedUser = request.getUserPrincipal().getName();
    		String authenticatedUserPasswword = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
    		
    		// Construct parameters
    	    String parameters = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
    	    
    		//TODO: HardWired URL
    		String urlString = "http://localhost:8080/fingerbookRESTM/admin/deleteUser";
    		
    		StringBuffer sb = FingerbookWebClientUtils.makeBasicPostRequest(urlString, parameters, 
    				authenticatedUser, authenticatedUserPasswword);	
    		
    		sb.append("\"");
    		result = sb.toString();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	modelMap.put("result", result);
    	return "deleteUser";
    }


    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    	return "deleteUser";
    }
    
    @RequestMapping
    public String index() {
        return "deleteUser";
    }
}
