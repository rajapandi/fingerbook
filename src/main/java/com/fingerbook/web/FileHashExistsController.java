package com.fingerbook.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/filehashexists/**")
@Controller
public class FileHashExistsController {

    @RequestMapping(method = RequestMethod.POST)
    public String post(@RequestParam("hash") String hash, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    	String result = "\"";
    	try {
    		
    		URL url = new URL("http://localhost:8080/fingerbookRESTM/fingerbooks/hash/" + hash);
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
    	return "fileHashExists";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    	return "fileHashExists";
    }
    
    @RequestMapping
    public String index() {
        return "fileHashExists";
    }
}
