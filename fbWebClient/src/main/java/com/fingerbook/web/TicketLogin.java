package com.fingerbook.web;

import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/ticketLogin/**")
@Controller
public class TicketLogin {

	@RequestMapping(method = RequestMethod.POST)
    public String post(@RequestParam("ticket") String ticket, 
			ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    	
    	String result = "\"";
    	try {
    		
    		// Construct parameters
    	    String parameters = URLEncoder.encode("ticket", "UTF-8") + "=" + URLEncoder.encode(ticket, "UTF-8");
    	    
    		//TODO: HardWired URL
    		String urlString = "http://localhost:8080/fingerbookRESTM/ticketAuthenticate";
    		
    		StringBuffer sb = FingerbookWebClientUtils.makeBasicPostRequest(urlString, parameters);	
    		
    		sb.append("\"");
    		result = sb.toString();
    		
    		if(sb.toString().contains("Authentication succesful for ticket")) {        		
        		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        		authentication.setAuthenticated(true);
        		SecurityContextHolder.getContext().setAuthentication(authentication);	
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	modelMap.put("result", result);
    	return "ticketLogin";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    	return "ticketLogin";
    }

    @RequestMapping
    public String index() {
        return "ticketLogin";
    }
}
