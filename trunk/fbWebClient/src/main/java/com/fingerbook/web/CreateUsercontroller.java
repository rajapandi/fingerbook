package com.fingerbook.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/createUser/**")
@Controller
public class CreateUsercontroller {

    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    	String result = "\"";
    	try {
    		
    		//TODO: Wired URL
    		URL url = new URL("http://localhost:8080/fingerbookRESTM/admin/createUser/newuser/newuserpassword");
    		URLConnection conn = url.openConnection ();

    		// Get the response
    		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    		StringBuffer sb = new StringBuffer();
    		String line;
    		while ((line = rd.readLine()) != null) {
    			sb.append(line);
    		}
    		rd.close();
    		sb.append("\"");
    		result = sb.toString();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	modelMap.put("result", result);
    	return "createUser";
    }

    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }

    @RequestMapping
    public String index() {
        return "createUser/index";
    }
}
