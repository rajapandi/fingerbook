package com.fingerbook.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.fingerbook.models.Fingerbook;



@RequestMapping("/listfingerbooks/**")
@Controller
public class ListFingerbooksController {
	
	public static int LIMIT_PAG = 5;

	@RequestMapping(value="/ticketform", method = RequestMethod.GET)
	public String searchByTicket() {
        return "listfingerbooks/ticketform";
    }
	
	@RequestMapping(value="/userform", method = RequestMethod.GET)
	public String searchByUser() {
        return "listfingerbooks/userform";
    }
	
	@RequestMapping(value="/hashform", method = RequestMethod.GET)
	public String searchByHash() {
        return "listfingerbooks/hashform";
    }
	
	@RequestMapping(value="/ticket", method = RequestMethod.GET)
//	public String listByTicket(@RequestParam("ticket_input") String ticket, @RequestParam("page_number") int page, ModelMap modelMap) {
	public String listByTicket(@RequestParam("ticket_input") String ticket, ModelMap modelMap) {
		
		List<Fingerbook> fbs = null;
		String result = "";
    	try {
    		
    		String urlStr = "http://localhost:8080/fingerbookRESTM/fingerbooks/ticket/";
    		urlStr = urlStr + ticket;
//    		urlStr = urlStr + "/limit/" + LIMIT_PAG + "/offset/" + page * LIMIT_PAG;
    		
//    		URL url = new URL("http://localhost:8080/fingerbookRESTM/fingerbooks/132143");
//    		URL url = new URL(urlStr);
//    		
//    		URLConnection conn = url.openConnection();
//    		
//    		InputStream is = conn.getInputStream();

    		// Get the response
//    		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//    		StringBuffer sb = new StringBuffer();
//    		String line;
//    		while ((line = rd.readLine()) != null) {
//    			sb.append(line);
//    		}
//    		rd.close();
////    		sb.append("\"");
//    		result = sb.toString();
    		
    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
    		
    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
    		fbs = (List<Fingerbook>) restTemplate.getForObject(urlStr, List.class);
    		
//    		XStreamMarshaller unmarshaller = (XStreamMarshaller)wap.getBean("configuredMarshaller");
//    		Unmarshaller unmarshaller;
    		
//    		fbs = (Vector<Fingerbook>) unmarshaller.unmarshal(new StreamSource(is));
    		
    		for(Fingerbook fb: fbs) {
    			result = result + "<br />" + fb.toString();
    			System.out.println(fb.toString());
    		}
    		
//    		result = StringEscapeUtils.escapeHtml(result);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	modelMap.put("result", result);
    	modelMap.put("fbs", fbs);
		
        return "listfingerbooks/list";
    }
	
	@RequestMapping(value="/user", method = RequestMethod.GET)
	public String listByUser(@RequestParam("user_input") String user, ModelMap modelMap) {
		
		List<Fingerbook> fbs = null;
		String result = "";
    	try {
    		
    		String urlStr = "http://localhost:8080/fingerbookRESTM/fingerbooks/user/";
    		urlStr = urlStr + user;

    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
    		
    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
    		fbs = (List<Fingerbook>) restTemplate.getForObject(urlStr, List.class);
    		    		
    		for(Fingerbook fb: fbs) {
    			result = result + "<br />" + fb.toString();
    			System.out.println(fb.toString());
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	modelMap.put("result", result);
    	modelMap.put("fbs", fbs);
		
        return "listfingerbooks/list";
    }
	
	@RequestMapping(value="/hash", method = RequestMethod.GET)
	public String listByHash(@RequestParam("hash_input") String hash, ModelMap modelMap) {
		
		List<Fingerbook> fbs = null;
		String result = "";
    	try {
    		
    		String urlStr = "http://localhost:8080/fingerbookRESTM/fingerbooks/hash/";
    		urlStr = urlStr + hash;

    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
    		
    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
    		fbs = (List<Fingerbook>) restTemplate.getForObject(urlStr, List.class);
    		    		
    		for(Fingerbook fb: fbs) {
    			result = result + "<br />" + fb.toString();
    			System.out.println(fb.toString());
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	modelMap.put("result", result);
    	modelMap.put("fbs", fbs);
		
        return "listfingerbooks/list";
    }
	
    @RequestMapping
    public void get(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }

    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }

    @RequestMapping
    public String index() {
        return "listfingerbooks/index";
    }
}
