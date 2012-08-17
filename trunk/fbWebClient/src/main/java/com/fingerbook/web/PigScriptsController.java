package com.fingerbook.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Response;
import com.fingerbook.models.UserInfo;
import com.fingerbook.models.pig.PigScript;
import com.fingerbook.models.pig.PigScriptResult;
import com.fingerbook.models.transfer.PigScriptFeed;
import com.fingerbook.models.transfer.PigScriptList;
import com.fingerbook.models.transfer.PigScriptResultFeed;
import com.fingerbook.models.transfer.PigScriptResultList;

@RequestMapping("/pigscripts/**")
@Controller
public class PigScriptsController {
	
	public static final String URL_PIG_SCRIPTS_ALL = "http://localhost:8080/fingerbookRESTM/admin/script/scriptslist/";
	public static final String URL_PIG_SCRIPTS_RESULTS_ALL = "http://localhost:8080/fingerbookRESTM/admin/script/scriptsreslist/";
	public static final String URL_PIG_EXEC_SCRIPT = "http://localhost:8080/fingerbookRESTM/admin/script/execscripttask?id={id}";
	
	public static final String URL_PIG_SCRIPT = "http://localhost:8080/fingerbookRESTM/admin/script/scriptfeed?id={id}";
	public static final String URL_PIG_SCRIPT_RESULT = "http://localhost:8080/fingerbookRESTM/admin/script/scriptresfeed?id={id}";
	
	public static final String URL_PIG_SCRIPT_UPDATE = "http://localhost:8080/fingerbookRESTM/admin/script/updatescript/";
	
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_SEMIAUTH = "ROLE_SEMIAUTH";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
	
	public static int LIMIT_PAG = 5;
	
	
	@RequestMapping(value="/script/scriptslist", method = RequestMethod.GET)
	public String listScripts(ModelMap modelMap, HttpServletRequest request) {
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
//		FingerbookList fingerbookList = null;
//		Vector<Fingerbook> fbs = null;
		PigScriptList pigScriptList = null;
		Vector<PigScript> scripts = null;
		Response response = null;
		
    	try {
    		
    		if(request.getParameter("page") != null) {
    			try {
					page = Integer.parseInt(request.getParameter("page"));
					if(page <= 0) {
						page = 1;
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		if(request.getParameter("size") != null) {
    			try {
					size = Integer.parseInt(request.getParameter("size"));
					if(size <= 0) {
						size = LIMIT_PAG;
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
//	    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
//	    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
//	    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
//	    		fingerbookList = (FingerbookList) restTemplate.getForObject(urlStr, FingerbookList.class);
    		
//    		fingerbookList = getFingerbookListRESTTicket(request, ticket, size, page);
    		pigScriptList = getPigScriptListREST(request, size, page);
    		
    		

    		if(pigScriptList != null) {
    			
    			response = pigScriptList.getResponse();
    			if(response != null) {
        			//TODO: Nahuel: accion de error
        			modelMap.put("response", response);
        			return "listfingerbooks/error_response";
        		}
    			
    			maxPages = (int) Math.ceil((double)pigScriptList.getTotalresults() / (double)size);
    			
    			scripts = pigScriptList.getPigScripts();
	    		for(PigScript script: scripts) {
//	    			result = result + "<br />" + fb.toString();
	    			System.out.println(script.toString());
	    		}
    		}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	
//    	modelMap.put("fbs", fbs);
    	modelMap.put("scripts", scripts);
    	modelMap.put("page", page);
		modelMap.put("size", size);
    	modelMap.put("maxPages", maxPages);
//    	modelMap.put("requiredParams", "ticket_input");
		
    	if(request.getParameter("table") != null && request.getParameter("table").equals("scriptsfulllist")) {
			return "pigscripts/scriptsfulllist";
		}
    	
    	return "pigscripts/list_scripts";
    	
    }
	
	@RequestMapping(value="/scriptsres/scriptsreslist", method = RequestMethod.GET)
	public String listScriptsResults(ModelMap modelMap, HttpServletRequest request) {
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		

		PigScriptResultList pigScriptResultList = null;
		Vector<PigScriptResult> scriptsResults = null;
		Response response = null;
		
    	try {
    		
    		if(request.getParameter("page") != null) {
    			try {
					page = Integer.parseInt(request.getParameter("page"));
					if(page <= 0) {
						page = 1;
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		if(request.getParameter("size") != null) {
    			try {
					size = Integer.parseInt(request.getParameter("size"));
					if(size <= 0) {
						size = LIMIT_PAG;
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
//	    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
//	    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
//	    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
//	    		fingerbookList = (FingerbookList) restTemplate.getForObject(urlStr, FingerbookList.class);
    		
//    		fingerbookList = getFingerbookListRESTTicket(request, ticket, size, page);
    		pigScriptResultList = getPigScriptResultListREST(request, size, page);
    		
    		

    		if(pigScriptResultList != null) {
    			
    			response = pigScriptResultList.getResponse();
    			if(response != null) {
        			//TODO: Nahuel: accion de error
        			modelMap.put("response", response);
        			return "listfingerbooks/error_response";
        		}
    			
    			maxPages = (int) Math.ceil((double)pigScriptResultList.getTotalresults() / (double)size);
    			
    			scriptsResults = pigScriptResultList.getPigScriptResults();
	    		for(PigScriptResult scriptsResult: scriptsResults) {
//	    			result = result + "<br />" + fb.toString();
	    			System.out.println(scriptsResult.toString());
	    		}
    		}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	
//    	modelMap.put("fbs", fbs);
    	modelMap.put("scriptsResults", scriptsResults);
    	modelMap.put("page", page);
		modelMap.put("size", size);
    	modelMap.put("maxPages", maxPages);
//    	modelMap.put("requiredParams", "ticket_input");
		
    	if(request.getParameter("table") != null && request.getParameter("table").equals("scriptsresfulllist")) {
			return "pigscripts/scriptsresfulllist";
		}
    	
    	return "pigscripts/list_scripts_res";
    	
    }
	
	@RequestMapping(value="/script/{id}", params="action=exec", method = RequestMethod.GET)
	@ResponseBody
	public String scriptActionExec(@PathVariable("id") int scriptId, ModelMap modelMap, HttpServletRequest request) {
		
		String ret = "";
		
		Response response = null;
		String urlStr = null;
		
		System.out.println("scriptActionExec - doExec ID: " + scriptId);
		
		urlStr = URL_PIG_EXEC_SCRIPT;
//		urlStr = urlStr + scriptId;
		
//		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
//		map.add("id", String.valueOf(scriptId));
		Map<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(scriptId));
		
		RestTemplate restTemplate = buildRestTemplate(request);
//		response = restTemplate.postForObject(urlStr, loadfb, Response.class);
//		response = restTemplate.postForObject(urlStr, map, Response.class);
		response = restTemplate.getForObject(urlStr, Response.class, map);

		if(response != null) {
//			modelMap.put("response", response);
			
			String respDesc = response.getDesc();
			
			if(respDesc == null) {
				respDesc = "";
			}
			
			if(response.getErrorCode() != null) {
    			
				ret = "Error: " + respDesc;
			}
			else {
				ret = "Success: " + respDesc;
			}
		}
		else {
			ret = "Empty response for Execution of pig script: " + scriptId;
		}
		
		
//		return "scriptActionExec - ID: " + scriptId;
		return ret;

	}
	
	@RequestMapping(value="/script/{id}", params="action=show", method = RequestMethod.GET)
	public String scriptActionShow(@PathVariable("id") int scriptId, ModelMap modelMap, HttpServletRequest request) {
		
		PigScriptFeed pigScriptFeed = null;
		PigScript script = null;
		Response response = null;
//		String urlStr = null;
		
    	try {
    		
//            pigScriptFeed = restTemplate.getForObject(urlStr, PigScriptFeed.class, map);
    		pigScriptFeed = loadPigScriptFeed(scriptId, modelMap, request);
    		
    		if(pigScriptFeed != null) {
    			
    			response = pigScriptFeed.getResponse();
    			if(response != null) {
        			//TODO: Nahuel: accion de error
        			modelMap.put("response", response);
        			return "listfingerbooks/error_response";
        		}
    			
    			script = pigScriptFeed.getPigScript();
    			if(script != null) {
    				System.out.println(script.toString());
    			}
    		}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	
    	modelMap.put("script", script);
//    	modelMap.put("requiredParams", "ticket_input");
		
    	
    	return "pigscripts/show_script";
		
	}
	
	@RequestMapping(value="/script/{id}", params="action=update", method = RequestMethod.GET)
	public String scriptActionUpdate(@PathVariable("id") int scriptId, ModelMap modelMap, HttpServletRequest request) {
		
		PigScriptFeed pigScriptFeed = null;
		PigScript script = null;
		Response response = null;
//		String urlStr = null;
		
    	try {
    		
//            pigScriptFeed = restTemplate.getForObject(urlStr, PigScriptFeed.class, map);
    		pigScriptFeed = loadPigScriptFeed(scriptId, modelMap, request);
    		
    		if(pigScriptFeed != null) {
    			
    			response = pigScriptFeed.getResponse();
    			if(response != null) {
        			//TODO: Nahuel: accion de error
        			modelMap.put("response", response);
        			return "listfingerbooks/error_response";
        		}
    			
    			script = pigScriptFeed.getPigScript();
    			if(script != null) {
    				System.out.println(script.toString());
    			}
    		}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	
    	modelMap.put("script", script);
//    	modelMap.put("requiredParams", "ticket_input");
		
    	String referer = request.getHeader("Referer");
    	modelMap.put("referer", referer);
    	
    	return "pigscripts/update_script";
		
	}
	
//	@RequestMapping(value="/script/{id}", params="action=show", method = RequestMethod.GET)
//	public String scriptActionShow(@PathVariable("id") int scriptId, ModelMap modelMap, HttpServletRequest request) {
//		
//		PigScriptFeed pigScriptFeed = null;
//		PigScript script = null;
//		Response response = null;
//		String urlStr = null;
//		
//    	try {
//    		
//    		urlStr = URL_PIG_SCRIPT;
//    		
//    		Map<String, String> map = new HashMap<String, String>();
//            map.put("id", String.valueOf(scriptId));
//            
//            RestTemplate restTemplate = buildRestTemplate(request);
//            pigScriptFeed = restTemplate.getForObject(urlStr, PigScriptFeed.class, map);
//            
//    		
//    		if(pigScriptFeed != null) {
//    			
//    			response = pigScriptFeed.getResponse();
//    			if(response != null) {
//        			//TODO: Nahuel: accion de error
//        			modelMap.put("response", response);
//        			return "listfingerbooks/error_response";
//        		}
//    			
//    			script = pigScriptFeed.getPigScript();
//    			if(script != null) {
//    				System.out.println(script.toString());
//    			}
//    		}
//
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}
//    	
//    	
//    	modelMap.put("script", script);
////    	modelMap.put("requiredParams", "ticket_input");
//		
//    	
//    	return "pigscripts/show_script";
//		
//	}
	
	@RequestMapping(value="/scriptsres/{id}", method = RequestMethod.GET)
	public String scriptResultActionShow(@PathVariable("id") int scriptResultId, ModelMap modelMap, HttpServletRequest request) {
		
		PigScriptResultFeed pigScriptResultFeed = null;
		PigScriptResult scriptsResult = null;
		Response response = null;
		String urlStr = null;
		
    	try {
    		
    		urlStr = URL_PIG_SCRIPT_RESULT;
    		
    		Map<String, String> map = new HashMap<String, String>();
            map.put("id", String.valueOf(scriptResultId));
            
            RestTemplate restTemplate = buildRestTemplate(request);
            pigScriptResultFeed = restTemplate.getForObject(urlStr, PigScriptResultFeed.class, map);
            
    		
    		if(pigScriptResultFeed != null) {
    			
    			response = pigScriptResultFeed.getResponse();
    			if(response != null) {
        			//TODO: Nahuel: accion de error
        			modelMap.put("response", response);
        			return "listfingerbooks/error_response";
        		}
    			
    			scriptsResult = pigScriptResultFeed.getScriptResult();
    			if(scriptsResult != null) {
    				System.out.println(scriptsResult.toString());
    			}
    		}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	
    	modelMap.put("scriptsResult", scriptsResult);
//    	modelMap.put("requiredParams", "ticket_input");
		
    	
    	return "pigscripts/show_script_res";
		
	}
	
	@RequestMapping(value="/script/update", method = RequestMethod.GET)
	public String updateScript(ModelMap modelMap, HttpServletRequest request) {
		
		Response response = null;
//		Fingerbook loadfb = null;
		PigScript script = null;
		
//		Long id = null;
//		String comment = "";
//		Set<String> tags = new HashSet<String>();
//		String[] tagsInput = null;
		
		int scriptId = -1;
		String scriptName = "";
		String scriptDesc = "";
		String scriptFilePath = "";
		
		
		String urlStr = null;
		
    	try {
    		if(request.getParameter("scriptId") != null) {
//    			id = Long.parseLong(request.getParameter("fbId"));
    			scriptId = Integer.parseInt(request.getParameter("scriptId"));
    		}
    		if(request.getParameter("name_input") != null) {
    			scriptName = request.getParameter("name_input");
    		}
    		if(request.getParameter("desc_input") != null) {
    			scriptDesc = request.getParameter("desc_input");
    		}
    		if(request.getParameter("path_input") != null) {
    			scriptFilePath = request.getParameter("path_input");
    		}
    		
    		
//    		loadfb = new Fingerbook();
    		script = new PigScript();
    		
//    		loadfb.setFingerbookId(id);
//    		loadfb.setComment(comment);
//    		loadfb.setTags(tags);
    		
    		script.setScriptId(scriptId);
    		script.setScriptName(scriptName);
    		script.setScriptDesc(scriptDesc);
    		script.setScriptFilePath(scriptFilePath);
    		
    		urlStr = URL_PIG_SCRIPT_UPDATE;
    		
    		RestTemplate restTemplate = buildRestTemplate(request);
    		response = restTemplate.postForObject(urlStr, script, Response.class);
    		
//    		String authMethod = getAuthMethod();
//    		
//    		if(authMethod.equalsIgnoreCase(ROLE_ADMIN)) {
//    			urlStr = URL_UPDATE_ADMIN;
//    		}
//    		else if(authMethod.equalsIgnoreCase(ROLE_USER)) {
//    			urlStr = URL_UPDATE_USER;
//    		}
//    		else if(authMethod.equalsIgnoreCase(ROLE_SEMIAUTH)) {
//    			urlStr = URL_UPDATE_TICKET;
//    		}
//    		else {
//    			urlStr = URL_UPDATE_TICKET;
//    		}
//    		
//    		RestTemplate restTemplate = buildRestTemplate(request, loadfb);
//    		response = restTemplate.postForObject(urlStr, loadfb, Response.class);

    		if(response != null) {
    			modelMap.put("response", response);
    			
    			if(response.getErrorCode() != null) {
        			return "listfingerbooks/error_response";
    			}
    		}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
//    	return "redirect:" + id;
    	return "redirect:" + scriptId + "?action=show";
    }
	
	public PigScriptFeed loadPigScriptFeed(int scriptId, ModelMap modelMap, HttpServletRequest request) {
		
		PigScriptFeed pigScriptFeed = null;
		PigScript script = null;
		Response response = null;
		String urlStr = null;
		
    	try {
    		
    		urlStr = URL_PIG_SCRIPT;
    		
    		Map<String, String> map = new HashMap<String, String>();
            map.put("id", String.valueOf(scriptId));
            
            RestTemplate restTemplate = buildRestTemplate(request);
            pigScriptFeed = restTemplate.getForObject(urlStr, PigScriptFeed.class, map);
            

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	
    	modelMap.put("script", script);
//    	modelMap.put("requiredParams", "ticket_input");
		
    	
//    	return "pigscripts/show_script";
    	return pigScriptFeed;
		
	}

	public PigScriptList getPigScriptListREST(HttpServletRequest request, int size, int page) {
		
//		FingerbookList fingerbookList = null;
		PigScriptList pigScriptList = null;
		String urlStr = null;
		
    	try {
    		
    		String authMethod = getAuthMethod();
    		
    		if(authMethod.equalsIgnoreCase(ROLE_ADMIN)) {
    			
    			urlStr = URL_PIG_SCRIPTS_ALL;
//    			urlStr = urlStr + ticket;
    		}
    		else {
    			return pigScriptList;
    		}
    		
    		urlStr = urlStr + "limit/" + size + "/offset/" + (page-1)  * size;
    		RestTemplate restTemplate = buildRestTemplate(request);
//    		fingerbookList = (FingerbookList) restTemplate.getForObject(urlStr, FingerbookList.class);
    		pigScriptList = (PigScriptList) restTemplate.getForObject(urlStr, PigScriptList.class);
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
		return pigScriptList;
	}
	
	public PigScriptResultList getPigScriptResultListREST(HttpServletRequest request, int size, int page) {
		
		PigScriptResultList pigScriptResultList = null;
		String urlStr = null;
		
    	try {
    		
    		String authMethod = getAuthMethod();
    		
    		if(authMethod.equalsIgnoreCase(ROLE_ADMIN)) {
    			
    			urlStr = URL_PIG_SCRIPTS_RESULTS_ALL;
//    			urlStr = urlStr + ticket;
    		}
    		else {
    			return pigScriptResultList;
    		}
    		
    		urlStr = urlStr + "limit/" + size + "/offset/" + (page-1)  * size;
    		RestTemplate restTemplate = buildRestTemplate(request);
//    		fingerbookList = (FingerbookList) restTemplate.getForObject(urlStr, FingerbookList.class);
    		pigScriptResultList = (PigScriptResultList) restTemplate.getForObject(urlStr, PigScriptResultList.class);
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
		return pigScriptResultList;
	}
	
	public String getAuthMethod() {
    	String role = null;
    	GrantedAuthorityImpl authRole = null;
		Collection<GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		
		for(GrantedAuthority authority: authorities) {
			if(authority instanceof GrantedAuthorityImpl) {
				authRole = ((GrantedAuthorityImpl)authority);
			}
		}
		
		if(authRole != null) {
			role = authRole.getAuthority();
			System.out.println("ROLE: " + role);
		}
		
//		if(authorities != null && authorities.size() > 0) {
//			role = authorities.toString();
//		}
		
		if(role == null) {
			role = ROLE_ANONYMOUS;
		}
		
		return role;
		
    }
	
	public RestTemplate buildRestTemplate(HttpServletRequest request) {
		
		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
		UserInfo userInfo = new UserInfo();
		
		String role = getAuthMethod();
		
		System.out.println("ROLE AUTH METHOD!! " + role);
		
		if(role.equalsIgnoreCase(ROLE_ADMIN)) {
			String authenticatedUser = request.getUserPrincipal().getName();
			String authenticatedUserPasswword = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
			
			HttpClient client = new HttpClient();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(authenticatedUser,authenticatedUserPasswword);
			client.getState().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM), credentials);
			CommonsClientHttpRequestFactory commons = new CommonsClientHttpRequestFactory(client);
			
			restTemplate.setRequestFactory(commons);

		}
		else if(role.equalsIgnoreCase(ROLE_USER)) {
			String authenticatedUser = request.getUserPrincipal().getName();
			String authenticatedUserPasswword = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
			
			HttpClient client = new HttpClient();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(authenticatedUser,authenticatedUserPasswword);
			client.getState().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM), credentials);
			CommonsClientHttpRequestFactory commons = new CommonsClientHttpRequestFactory(client);
			
			restTemplate.setRequestFactory(commons);

			userInfo.setUser(authenticatedUser);
		}
		else if(role.equalsIgnoreCase(ROLE_SEMIAUTH)) {
			String authenticatedTicket = request.getUserPrincipal().getName();
			userInfo.setTicket(authenticatedTicket);
		}
		
//		if(loadfb != null) {
//			loadfb.setUserInfo(userInfo);
//		}
		
		return restTemplate;
		
	}
}
