package com.fingerbook.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.fingerbook.FingerbookMatch;
import com.fingerbook.models.CompositeFingerbook;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.models.Response;
import com.fingerbook.models.UserInfo;
import com.fingerbook.models.transfer.CompositeFingerbookFeed;
import com.fingerbook.models.transfer.CompositeFingerbookList;
import com.fingerbook.models.transfer.FingerbookFeed;
import com.fingerbook.models.transfer.FingerbookList;
import com.fingerbook.models.transfer.FingerprintsFeed;
import com.fingerbook.models.transfer.SimilaritiesFeed;



@RequestMapping("/listfingerbooks/**")
@Controller
public class ListFingerbooksController {
	
//	public static final String URL_FINGERBOOK = "http://localhost:8080/fingerbookRESTM/fingerbooks/fingerbook/";
	public static final String URL_FINGERBOOK_ADMIN = "http://localhost:8080/fingerbookRESTM/fingerbooks/admin/fingerbook/";
	public static final String URL_FINGERBOOK_USER = "http://localhost:8080/fingerbookRESTM/fingerbooks/authenticated/fingerbook/";
	public static final String URL_FINGERBOOK_TICKET = "http://localhost:8080/fingerbookRESTM/fingerbooks/semiauthenticated/fingerbook/";
	public static final String URL_FINGERBOOK_ANON = "http://localhost:8080/fingerbookRESTM/fingerbooks/anonymous/fingerbook/";
	
	public static final String URL_COMPFINGERBOOK_ADMIN = "http://localhost:8080/fingerbookRESTM/fingerbooks/admin/compfingerbook/";
	public static final String URL_COMPFINGERBOOK_USER = "http://localhost:8080/fingerbookRESTM/fingerbooks/authenticated/compfingerbook/";
	public static final String URL_COMPFINGERBOOK_TICKET = "http://localhost:8080/fingerbookRESTM/fingerbooks/semiauthenticated/compfingerbook/";
	public static final String URL_COMPFINGERBOOK_ANON = "http://localhost:8080/fingerbookRESTM/fingerbooks/anonymous/compfingerbook/";
	
	public static final String URL_FINGEPRINTS_ADMIN = "http://localhost:8080/fingerbookRESTM/fingerbooks/admin/fingerprints/";
	public static final String URL_FINGEPRINTS_AUTH = "http://localhost:8080/fingerbookRESTM/fingerbooks/authenticated/fingerprints/";
	public static final String URL_FINGEPRINTS_SEMIAUTH = "http://localhost:8080/fingerbookRESTM/fingerbooks/semiauthenticated/fingerprints/";
	public static final String URL_FINGEPRINTS_ANON = "http://localhost:8080/fingerbookRESTM/fingerbooks/anonymous/fingerprints/";
	
	public static final String URL_COMPFINGEPRINTS_ADMIN = "http://localhost:8080/fingerbookRESTM/fingerbooks/admin/compfingerprints/";
	public static final String URL_COMPFINGEPRINTS_AUTH = "http://localhost:8080/fingerbookRESTM/fingerbooks/authenticated/compfingerprints/";
	public static final String URL_COMPFINGEPRINTS_SEMIAUTH = "http://localhost:8080/fingerbookRESTM/fingerbooks/semiauthenticated/compfingerprints/";
	public static final String URL_COMPFINGEPRINTS_ANON = "http://localhost:8080/fingerbookRESTM/fingerbooks/anonymous/compfingerprints/";
	
	public static final String URL_MATCHES_ADMIN = "http://localhost:8080/fingerbookRESTM/fingerbooks/admin/matches/";
	public static final String URL_MATCHES_AUTH = "http://localhost:8080/fingerbookRESTM/fingerbooks/authenticated/matches/";
	public static final String URL_MATCHES_SEMIAUTH = "http://localhost:8080/fingerbookRESTM/fingerbooks/semiauthenticated/matches/";
	public static final String URL_MATCHES_ANON = "http://localhost:8080/fingerbookRESTM/fingerbooks/anonymous/matches/";
	
	public static final String URL_COMPFBLIST_ADMIN = "http://localhost:8080/fingerbookRESTM/fingerbooks/admin/compfblist/";
	public static final String URL_COMPFBLIST_AUTH = "http://localhost:8080/fingerbookRESTM/fingerbooks/authenticated/compfblist/";
	public static final String URL_COMPFBLIST_SEMIAUTH = "http://localhost:8080/fingerbookRESTM/fingerbooks/semiauthenticated/compfblist/";
	public static final String URL_COMPFBLIST_ANON = "http://localhost:8080/fingerbookRESTM/fingerbooks/anonymous/compfblist/";
	
	public static final String URL_FINGERBOOKS_ADMIN_USER = "http://localhost:8080/fingerbookRESTM/fingerbooks/admin/user/";
	public static final String URL_FINGERBOOKS_ADMIN_TICKET = "http://localhost:8080/fingerbookRESTM/fingerbooks/admin/ticket/";
	public static final String URL_FINGERBOOKS_USER = "http://localhost:8080/fingerbookRESTM/fingerbooks/authenticated/user/";
	public static final String URL_FINGERBOOKS_TICKET = "http://localhost:8080/fingerbookRESTM/fingerbooks/semiauthenticated/ticket/";
	public static final String URL_FINGERBOOKS_HASH = "http://localhost:8080/fingerbookRESTM/fingerbooks/hash/";
	
	public static final String URL_UPDATE_ADMIN = "http://localhost:8080/fingerbookRESTM/fingerbooks/admin/update/";
	public static final String URL_UPDATE_USER = "http://localhost:8080/fingerbookRESTM/fingerbooks/authenticated/update/";
	public static final String URL_UPDATE_TICKET = "http://localhost:8080/fingerbookRESTM/fingerbooks/semiauthenticated/update/";
	public static final String URL_UPDATE_ANON = "http://localhost:8080/fingerbookRESTM/fingerbooks/anonymous/update/";
	
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_SEMIAUTH = "ROLE_SEMIAUTH";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
	
	public static final String FINGERPRINTS_TABLE_PATH = "/listfingerbooks";
	public static final String COMP_FINGERPRINTS_TABLE_PATH = "/listfingerbooks/compfb";
	
	public static int LIMIT_PAG = 5;

	@RequestMapping(value="/ticketform", method = RequestMethod.GET)
	public String searchByTicket(ModelMap modelMap) {
		
		modelMap.put("page", 1);
		modelMap.put("size", LIMIT_PAG);
		
        return "listfingerbooks/ticketform";
    }
	
	@RequestMapping(value="/userform", method = RequestMethod.GET)
	public String searchByUser(ModelMap modelMap) {
		
		modelMap.put("page", 1);
		modelMap.put("size", LIMIT_PAG);
		
        return "listfingerbooks/userform";
    }
	
	@RequestMapping(value="/hashform", method = RequestMethod.GET)
	public String searchByHash(ModelMap modelMap) {
		
		modelMap.put("page", 1);
		modelMap.put("size", LIMIT_PAG);
		
        return "listfingerbooks/hashform";
    }
	
//	@RequestMapping(value="/ticket", method = RequestMethod.GET)
////	public String listByTicket(@RequestParam("ticket_input") String ticket, @RequestParam("page_number") int page, ModelMap modelMap) {
//	public String listByTicket(@RequestParam("ticket_input") String ticket, ModelMap modelMap, HttpServletRequest request) {
//		
//		int size = LIMIT_PAG;
//		int page = 1;
//		int maxPages = 1;
//		
//		FingerbookList fingerbookList = null;
//		Vector<Fingerbook> fbs = null;
////		String result = "";
//		Response response = null;
//		
//		if(ticket != null && !ticket.isEmpty()) {
//	    	try {
//	    		
//	//    		String urlStr = "http://localhost:8080/fingerbookRESTM/fingerbooks/ticket/";
//	    		String urlStr = URL_FINGERBOOKS_TICKET;
//	    		urlStr = urlStr + ticket;
//	    		
//	    		if(request.getParameter("page") != null) {
//	    			try {
//						page = Integer.parseInt(request.getParameter("page"));
//						if(page <= 0) {
//							page = 1;
//						}
//					} catch (NumberFormatException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//	    		}
//	    		if(request.getParameter("size") != null) {
//	    			try {
//						size = Integer.parseInt(request.getParameter("size"));
//						if(size <= 0) {
//							size = LIMIT_PAG;
//						}
//					} catch (NumberFormatException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//	    		}
//	    		
//	    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
//	    		
//	    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
//	    		
//	    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
//	    		
//	//    		fbs = (List<Fingerbook>) restTemplate.getForObject(urlStr, List.class);
//	    		fingerbookList = (FingerbookList) restTemplate.getForObject(urlStr, FingerbookList.class);
//	    		
//	
//	    		if(fingerbookList != null) {
//	    			
//	    			response = fingerbookList.getResponse();
//	    			if(response != null) {
//	        			//TODO: Nahuel: accion de error
//	        			modelMap.put("response", response);
//	        			return "listfingerbooks/error_response";
//	        		}
//	    			
//	    			maxPages = (int) Math.ceil((double)fingerbookList.getTotalresults() / (double)size);
//	    			
//	    			fbs = fingerbookList.getFbs();
//		    		for(Fingerbook fb: fbs) {
//	//	    			result = result + "<br />" + fb.toString();
//		    			System.out.println(fb.toString());
//		    		}
//	    		}
//	
//	    	} catch (Exception e) {
//	    		e.printStackTrace();
//	    	}
//		}
//    	
////    	modelMap.put("result", result);
//    	
//    	modelMap.put("fbs", fbs);
//    	modelMap.put("page", page);
//		modelMap.put("size", size);
//    	modelMap.put("maxPages", maxPages);
//    	modelMap.put("requiredParams", "ticket_input");
//		
//    	if(request.getParameter("table") != null && request.getParameter("table").equals("fbsfulllist")) {
//			return "listfingerbooks/fbsfulllist";
//		}
//    	
//    	return "listfingerbooks/list_by_user";
//    	
//    }
	
	@RequestMapping(value="/ticket", method = RequestMethod.GET)
//	public String listByTicket(@RequestParam("ticket_input") String ticket, @RequestParam("page_number") int page, ModelMap modelMap) {
	public String listByTicket(@RequestParam("ticket_input") String ticket, ModelMap modelMap, HttpServletRequest request) {
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
		FingerbookList fingerbookList = null;
		Vector<Fingerbook> fbs = null;
		Response response = null;
		
		if(ticket != null && !ticket.isEmpty()) {
	    	try {
	    		
//	    		String urlStr = URL_FINGERBOOKS_TICKET;
//	    		urlStr = urlStr + ticket;
	    		
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
	    		
	    		fingerbookList = getFingerbookListRESTTicket(request, ticket, size, page);
	    		
	    		
	    		
	
	    		if(fingerbookList != null) {
	    			
	    			response = fingerbookList.getResponse();
	    			if(response != null) {
	        			//TODO: Nahuel: accion de error
	        			modelMap.put("response", response);
	        			return "listfingerbooks/error_response";
	        		}
	    			
	    			maxPages = (int) Math.ceil((double)fingerbookList.getTotalresults() / (double)size);
	    			
	    			fbs = fingerbookList.getFbs();
		    		for(Fingerbook fb: fbs) {
	//	    			result = result + "<br />" + fb.toString();
		    			System.out.println(fb.toString());
		    		}
	    		}
	
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
		}
    	
    	
    	modelMap.put("fbs", fbs);
    	modelMap.put("page", page);
		modelMap.put("size", size);
    	modelMap.put("maxPages", maxPages);
    	modelMap.put("requiredParams", "ticket_input");
		
    	if(request.getParameter("table") != null && request.getParameter("table").equals("fbsfulllist")) {
			return "listfingerbooks/fbsfulllist";
		}
    	
    	return "listfingerbooks/list_by_user";
    	
    }

	
	@RequestMapping(value="/user", method = RequestMethod.GET)
//	public String listByUser(ModelMap modelMap, HttpServletRequest request) {
	public String listByUser(@RequestParam("user_input") String user, ModelMap modelMap, HttpServletRequest request) {
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
		FingerbookList fingerbookList = null;
		Vector<Fingerbook> fbs = null;
		Response response = null;
		
		if(user != null && !user.isEmpty()) {
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
	    		
	    		fingerbookList = getFingerbookListRESTUser(request, user, size, page);
	    		
	    		
	    		if(fingerbookList != null) {
	    			
	    			response = fingerbookList.getResponse();
	    			if(response != null) {
	        			//TODO: Nahuel: accion de error
	        			modelMap.put("response", response);
	        			return "listfingerbooks/error_response";
	        		}
	    			
	    			maxPages = (int) Math.ceil((double)fingerbookList.getTotalresults() / (double)size);
	    			
	    			fbs = fingerbookList.getFbs();
		    		for(Fingerbook fb: fbs) {
	//	    			result = result + "<br />" + fb.toString();
		    			System.out.println(fb.toString());
		    		}
	    		}
	    		
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
		}
    	
    	
    	modelMap.put("fbs", fbs);
    	modelMap.put("page", page);
		modelMap.put("size", size);
    	modelMap.put("maxPages", maxPages);
    	
    	modelMap.put("requiredParams", "user_input");
    	
    	if(request.getParameter("table") != null && request.getParameter("table").equals("fbsfulllist")) {
			return "listfingerbooks/fbsfulllist";
		}
		
        return "listfingerbooks/list_by_user";
    }
	
	@RequestMapping(value="/hash", method = RequestMethod.GET)
	public String listByHash(@RequestParam("hash_input") String hash, ModelMap modelMap, HttpServletRequest request) {
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
		FingerbookList fingerbookList = null;
		Vector<Fingerbook> fbs = null;
		String result = "";
		
		Response response = null;
		
		if(hash != null && !hash.isEmpty()) {
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
	    		
	//    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
	//    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
	//    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
	//    		fingerbookList = (FingerbookList) restTemplate.getForObject(urlStr, FingerbookList.class);
	
	    		fingerbookList = getFingerbookListRESTHash(request, hash, size, page);
	    		
	    		if(fingerbookList != null) {
	    			
	    			response = fingerbookList.getResponse();
	    			if(response != null) {
	        			//TODO: Nahuel: accion de error
	        			modelMap.put("response", response);
	        			return "listfingerbooks/error_response";
	        		}
	    			
	    			maxPages = (int) Math.ceil((double)fingerbookList.getTotalresults() / (double)size);
	    			
	    			fbs = fingerbookList.getFbs();
		    		for(Fingerbook fb: fbs) {
		    			result = result + "<br />" + fb.toString();
		    			System.out.println(fb.toString());
		    		}
	    		}
	    		
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
		}
		
    	modelMap.put("result", result);
    	modelMap.put("fbs", fbs);
    	
    	modelMap.put("page", page);
		modelMap.put("size", size);
		
    	modelMap.put("maxPages", maxPages);
    	
    	modelMap.put("requiredParams", "hash_input");
    	
    	if(request.getParameter("table") != null && request.getParameter("table").equals("fbsfulllist")) {
			return "listfingerbooks/fbsfulllist";
		}
		
//        return "listfingerbooks/list_by_user";
		
        return "listfingerbooks/list_by_hash";
    	
    }
	
	@RequestMapping(value="/userlogged", method = RequestMethod.GET)
//	public String listByUser(@RequestParam("user_input") String user, ModelMap modelMap, HttpServletRequest request) {
	public String listByUserLogged(ModelMap modelMap, HttpServletRequest request) {
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
		FingerbookList fingerbookList = null;
		Vector<Fingerbook> fbs = null;
//		String result = "";
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
    		
    		fingerbookList = getFingerbookListRESTUserLogged(request, size, page);
    		
    		
    		if(fingerbookList != null) {
    			
    			response = fingerbookList.getResponse();
    			if(response != null) {
        			//TODO: Nahuel: accion de error
        			modelMap.put("response", response);
        			return "listfingerbooks/error_response";
        		}
    			
    			maxPages = (int) Math.ceil((double)fingerbookList.getTotalresults() / (double)size);
    			
    			fbs = fingerbookList.getFbs();
	    		for(Fingerbook fb: fbs) {
//	    			result = result + "<br />" + fb.toString();
	    			System.out.println(fb.toString());
	    		}
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
//    	modelMap.put("result", result);
    	
    	modelMap.put("fbs", fbs);
    	modelMap.put("page", page);
		modelMap.put("size", size);
    	modelMap.put("maxPages", maxPages);
    	
//    	modelMap.put("requiredParams", "user_input");
    	
    	if(request.getParameter("table") != null && request.getParameter("table").equals("fbsfulllist")) {
			return "listfingerbooks/fbsfulllist";
		}
		
        return "listfingerbooks/list_by_user";
    }
	
	public RestTemplate buildRestTemplate(HttpServletRequest request, Fingerbook loadfb) {
		
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
		
		if(loadfb != null) {
			loadfb.setUserInfo(userInfo);
		}
		
		return restTemplate;
		
	}
	
	@RequestMapping(value="/update", method = RequestMethod.GET)
	public String updateFingerbook(ModelMap modelMap, HttpServletRequest request) {
		
		Response response = null;
		Fingerbook loadfb = null;
		
		Long id = null;
		String comment = "";
		Set<String> tags = new HashSet<String>();
		String[] tagsInput = null;
		
		
		String urlStr = null;
		
    	try {
    		if(request.getParameter("fbId") != null) {
    			id = Long.parseLong(request.getParameter("fbId"));
    		}
    		if(request.getParameter("comment_input") != null) {
    			comment = request.getParameter("comment_input");
    		}
    		
    		tagsInput = request.getParameterValues("tags_input"); 
    		if(tagsInput != null && tagsInput.length > 0) {
    			for(int i = 0; i < tagsInput.length; i++) {
    				tags.add(tagsInput[i]);
    			}
    		}
    		
    		loadfb = new Fingerbook();
    		
    		loadfb.setFingerbookId(id);
    		loadfb.setComment(comment);
    		loadfb.setTags(tags);
    		
    		String authMethod = getAuthMethod();
    		
    		if(authMethod.equalsIgnoreCase(ROLE_ADMIN)) {
    			urlStr = URL_UPDATE_ADMIN;
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_USER)) {
    			urlStr = URL_UPDATE_USER;
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_SEMIAUTH)) {
    			urlStr = URL_UPDATE_TICKET;
    		}
    		else {
    			urlStr = URL_UPDATE_TICKET;
    		}
    		
    		RestTemplate restTemplate = buildRestTemplate(request, loadfb);
    		response = restTemplate.postForObject(urlStr, loadfb, Response.class);

    		if(response != null) {
    			modelMap.put("response", response);
    			
    			if(response.getErrorCode() != null) {
        			return "listfingerbooks/error_response";
    			}
    		}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
//    	modelMap.put("result", result);
//    	modelMap.put("fb", fb);
//    	
//    	modelMap.put("maxPages", maxPages);
//    	
//    	modelMap.put("page", page);
//		modelMap.put("size", size);
//		return "listfingerbooks/show";
    	
    	return "redirect:" + id;
    }
	

	
	
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public String get(@PathVariable("id") Long id, ModelMap modelMap, HttpServletRequest request) {
		
		Fingerbook fb = null;
		String result = "";
		
		Fingerprints fingerprints = null;
		
		Vector<FingerbookMatch> fbMatches = new Vector<FingerbookMatch>();
		Map<Fingerbook, Double> similsFbs = null;
		
		Vector<CompositeFingerbook> compFbs = new Vector<CompositeFingerbook>();
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
		int sizeS = LIMIT_PAG;
		int pageS = 1;
		int maxPagesS = 1;
		
		int sizeC = LIMIT_PAG;
		int pageC = 1;
		int maxPagesC = 1;
		
		FingerbookFeed fingerbookFeed = null;
		FingerprintsFeed fingerprintsFeed = null;
		
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
    		
//    		fingerbookFeed = getFingerbookRest(request, id);
//    		fingerbookFeed = getFingerbookRest(request, id, size, page);
    		fingerbookFeed = getFingerbookRest(request, id, size, page, sizeS, pageS);
    		
    		if(fingerbookFeed != null) {
    			response = fingerbookFeed.getResponse();
    		}
    		
    		if(response != null) {
    			//TODO: Nahuel: accion de error
    			modelMap.put("response", response);
    			return "listfingerbooks/error_response";
    		}
    		
    		fb = fingerbookFeed.getFingerbook();
    		
    		if(fb != null) {
    		
	    		
//	    		fingerprintsFeed = getFingerprintsFeedREST(request, id, size, page);
    			fingerprintsFeed = fingerbookFeed.getFingerprintsFeed();
	    		
	    		if(fingerprintsFeed != null) {
	    			
	    			response = fingerprintsFeed.getResponse();
	    			if(response != null) {
	        			//TODO: Nahuel: accion de error
	    				modelMap.put("response", response);
	        			return "listfingerbooks/error_response";
	        		}
	    			
	    			fingerprints = fingerprintsFeed.getFingerPrints();
	    			
	    			maxPages = (int) Math.ceil((double)fingerprintsFeed.getTotalresults() / (double)size);
//	    			fb.setFingerPrints(fingerprintsFeed.getFingerPrints());
	    			
	    		}
	
	    		result = result + "<br />" + fb.toString();
	    		System.out.println(fb.toString());
	    		
	    		SimilaritiesFeed similaritiesFeed = fingerbookFeed.getSimilaritiesFeed();
	    		
	    		if(similaritiesFeed != null) {
	    			similsFbs = similaritiesFeed.getSimilsFbs();
	    			
	    			fbMatches = getFingerbookMatches(similsFbs);
	    			
	    			maxPagesS = (int) Math.ceil((double)similaritiesFeed.getTotalresults() / (double)sizeS);
	    			
	    			if(similsFbs != null) {
	    				System.out.println("SIMILARITIES");
	    				for(Entry<Fingerbook, Double> entry: similsFbs.entrySet()) {
	    					
	    					System.out.println(entry.getKey() + " --> " + entry.getValue());
	    				}
	    			}
	    			
	    		}
	    		
	    		CompositeFingerbookList compFbList = getCompositeFingerbookListREST(request, id, sizeC, pageC);
	    		if(compFbList != null) {
	    			
	    			System.out.println("COMPOSITE_FBS_LIST");
	    			
	    			maxPagesC = (int) Math.ceil((double)compFbList.getTotalresults() / (double)sizeC);
	    			
	    			compFbs = compFbList.getCompFbs();
					
					if(compFbs != null) {
						
						System.out.println("COMPOSITE_FBS");
						
						System.out.println("Limit: " + compFbList.getLimit() + "Offset: " + compFbList.getOffset() + "Total: " + compFbList.getTotalresults());
						
						for(CompositeFingerbook auxCompFb: compFbs) {
							
							System.out.println(auxCompFb);
						}
					}
					else {
						System.out.println("NULL COMPOSITE_FBS");
					}
				}
	    		else {
	    			System.out.println("NULL COMPOSITE_FBS_LIST");
	    		}
    		}
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	String referer = request.getHeader("Referer");
    	
    	modelMap.put("referer", referer);
    	
    	modelMap.put("result", result);
    	modelMap.put("fb", fb);
    	
    	modelMap.put("fingerprints", fingerprints);
    	
    	modelMap.put("maxPages", maxPages);
    	
    	modelMap.put("page", page);
		modelMap.put("size", size);
		
		modelMap.put("pages", pageS);
		modelMap.put("sizes", sizeS);
		modelMap.put("maxPagess", maxPagesS);
		
		modelMap.put("fbMatches", fbMatches);
		
		modelMap.put("pagec", pageC);
		modelMap.put("sizec", sizeC);
		modelMap.put("maxPagesc", maxPagesC);
		
		modelMap.put("compFbs", compFbs);
		
		modelMap.put("fpsTablePath", FINGERPRINTS_TABLE_PATH);
		
		
		if(request.getParameter("form") != null) {
			return "listfingerbooks/update";
		}
		
		return "listfingerbooks/show";
	}
	
	@RequestMapping(value="/compfb/{id}", method = RequestMethod.GET)
	public String getCompFb(@PathVariable("id") String id, ModelMap modelMap, HttpServletRequest request) {
		
//		Fingerbook fb = null;
		CompositeFingerbook compFb = null;
		String result = "";
		
		Fingerprints fingerprints = null;
		
//		Vector<FingerbookMatch> fbMatches = new Vector<FingerbookMatch>();
//		Map<Fingerbook, Double> similsFbs = null;
//		Vector<CompositeFingerbook> compFbs = new Vector<CompositeFingerbook>();
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
//		int sizeS = LIMIT_PAG;
//		int pageS = 1;
//		int maxPagesS = 1;
//		
//		int sizeC = LIMIT_PAG;
//		int pageC = 1;
//		int maxPagesC = 1;
		
//		FingerbookFeed fingerbookFeed = null;
		CompositeFingerbookFeed compositeFingerbookFeed = null;
		FingerprintsFeed fingerprintsFeed = null;
		
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
    		
//    		fingerbookFeed = getFingerbookRest(request, id, size, page, sizeS, pageS);
    		compositeFingerbookFeed = getCompositeFingerbookFeedRest(request, id, size, page);
    		
    		if(compositeFingerbookFeed != null) {
    			response = compositeFingerbookFeed.getResponse();
    		}
    		
    		if(response != null) {
    			//TODO: Nahuel: accion de error
    			modelMap.put("response", response);
    			return "listfingerbooks/error_response";
    		}
    		
//    		fb = fingerbookFeed.getFingerbook();
    		compFb = compositeFingerbookFeed.getCompositeFingerbook();
    		
    		if(compFb != null) {
    		
    			fingerprintsFeed = compositeFingerbookFeed.getFingerprintsFeed();
	    		
	    		if(fingerprintsFeed != null) {
	    			
	    			response = fingerprintsFeed.getResponse();
	    			if(response != null) {
	        			//TODO: Nahuel: accion de error
	    				modelMap.put("response", response);
	        			return "listfingerbooks/error_response";
	        		}
	    			
	    			fingerprints = fingerprintsFeed.getFingerPrints();
	    			
	    			maxPages = (int) Math.ceil((double)fingerprintsFeed.getTotalresults() / (double)size);
	    			
	    		}
	
	    		result = result + "<br />" + compFb.toString();
	    		System.out.println(compFb.toString());
    		}
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	String referer = request.getHeader("Referer");
    	
    	modelMap.put("referer", referer);
    	
    	modelMap.put("result", result);
//    	modelMap.put("fb", fb);
    	modelMap.put("compFb", compFb);
    	
    	modelMap.put("fingerprints", fingerprints);
    	
    	modelMap.put("maxPages", maxPages);
    	
    	modelMap.put("page", page);
		modelMap.put("size", size);
		
		modelMap.put("fpsTablePath", COMP_FINGERPRINTS_TABLE_PATH);
		
		
//		if(request.getParameter("form") != null) {
//			return "listfingerbooks/update";
//		}
		
		return "listfingerbooks/showcompfb";
	}
	
	@RequestMapping(value="/{id}", params="table=fingerprintslist", method = RequestMethod.GET)
	public String getFingerprints(@PathVariable("id") Long id, ModelMap modelMap, HttpServletRequest request) {
		
//		Fingerbook fb = null;
//		String result = "";
		
//		Vector<FingerbookMatch> fbMatches = new Vector<FingerbookMatch>();
//		Map<Fingerbook, Double> similsFbs = null;
		
		Fingerprints fingerprints = null;
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
//		FingerbookFeed fingerbookFeed = null;
		FingerprintsFeed fingerprintsFeed = null;
		
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
    		
//    		fingerbookFeed = getFingerbookRest(request, id, size, page, 5, 1);
//    		fingerbookFeed = getFingerbookRest(request, id, size, page);
    		
//    		if(fingerbookFeed != null) {
//    			response = fingerbookFeed.getResponse();
//    		}
//    		
//    		if(response != null) {
//    			//TODO: Nahuel: accion de error
//    			modelMap.put("response", response);
//    			return "listfingerbooks/error_response";
//    		}
//    		
//    		fb = fingerbookFeed.getFingerbook();
    		
    		
//			fingerprintsFeed = fingerbookFeed.getFingerprintsFeed();
    		
    		fingerprintsFeed = getFingerprintsFeedREST(request, id, size, page);
    		
    		if(fingerprintsFeed != null) {
    			
    			response = fingerprintsFeed.getResponse();
    			if(response != null) {
    				modelMap.put("response", response);
        			return "listfingerbooks/error_response";
        		}
    			
    			fingerprints = fingerprintsFeed.getFingerPrints();
    			
    			maxPages = (int) Math.ceil((double)fingerprintsFeed.getTotalresults() / (double)size);
    		}

    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
//    	String referer = request.getHeader("Referer");
//    	modelMap.put("referer", referer);
//    	modelMap.put("result", result);
    	
//    	modelMap.put("fb", fb);
    	
    	modelMap.put("fingerprints", fingerprints);
    	
    	modelMap.put("maxPages", maxPages);
    	modelMap.put("page", page);
		modelMap.put("size", size);
		
		modelMap.put("fpsTablePath", FINGERPRINTS_TABLE_PATH);
		
		
		return "listfingerbooks/fingerprintslist";
	}
	
	@RequestMapping(value="/compfb/{id}", params="table=fingerprintslist", method = RequestMethod.GET)
	public String getCompFbFingerprints(@PathVariable("id") String id, ModelMap modelMap, HttpServletRequest request) {
		
		Fingerprints fingerprints = null;
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
		FingerprintsFeed fingerprintsFeed = null;
		
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
    		
//    		fingerprintsFeed = getFingerprintsFeedREST(request, id, size, page);
    		fingerprintsFeed = getCompFingerprintsFeedREST(request, id, size, page);
    		
    		if(fingerprintsFeed != null) {
    			
    			response = fingerprintsFeed.getResponse();
    			if(response != null) {
    				modelMap.put("response", response);
        			return "listfingerbooks/error_response";
        		}
    			
    			fingerprints = fingerprintsFeed.getFingerPrints();
    			
    			maxPages = (int) Math.ceil((double)fingerprintsFeed.getTotalresults() / (double)size);
    		}

    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	modelMap.put("fingerprints", fingerprints);
    	
    	modelMap.put("maxPages", maxPages);
    	modelMap.put("page", page);
		modelMap.put("size", size);
		
		modelMap.put("fpsTablePath", COMP_FINGERPRINTS_TABLE_PATH);
		
		
		return "listfingerbooks/fingerprintslist";
	}
	
	@RequestMapping(value="/{id}", params="table=fbmatches", method = RequestMethod.GET)
	public String getFbMatches(@PathVariable("id") Long id, ModelMap modelMap, HttpServletRequest request) {
		
//		Fingerbook fb = null;
//		String result = "";
		
		Vector<FingerbookMatch> fbMatches = new Vector<FingerbookMatch>();
		Map<Fingerbook, Double> similsFbs = null;
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
//		FingerbookFeed fingerbookFeed = null;
//		FingerprintsFeed fingerprintsFeed = null;
		
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
    		
//    		fingerbookFeed = getFingerbookRest(request, id, size, page);
//    		fingerbookFeed = getFingerbookRest(request, id, size, page, size, page);
//    		
//    		if(fingerbookFeed != null) {
//    			response = fingerbookFeed.getResponse();
//    		}
//    		
//    		if(response != null) {
//    			modelMap.put("response", response);
//    			return "listfingerbooks/error_response";
//    		}
//    		
//    		fb = fingerbookFeed.getFingerbook();
//    		result = result + "<br />" + fb.toString();
//    		System.out.println(fb.toString());
//			SimilaritiesFeed similaritiesFeed = fingerbookFeed.getSimilaritiesFeed();
    		
    		SimilaritiesFeed similaritiesFeed = getSimilaritiesFeedREST(request, id, size, page);
			
			if(similaritiesFeed != null) {
				
				response = similaritiesFeed.getResponse();
				if(response != null) {
	    			//TODO: Nahuel: accion de error
					modelMap.put("response", response);
	    			return "listfingerbooks/error_response";
	    		}
				
				similsFbs = similaritiesFeed.getSimilsFbs();
				
				fbMatches = getFingerbookMatches(similsFbs);
				
				maxPages = (int) Math.ceil((double)similaritiesFeed.getTotalresults() / (double)size);
				
				if(similsFbs != null) {
					System.out.println("SIMILARITIES");
					for(Entry<Fingerbook, Double> entry: similsFbs.entrySet()) {
						
						System.out.println(entry.getKey() + " --> " + entry.getValue());
					}
				}
				
			}
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
//    	String referer = request.getHeader("Referer");
//    	modelMap.put("referer", referer);
//    	modelMap.put("result", result);
//    	modelMap.put("fb", fb);
    	
    	modelMap.put("maxPagess", maxPages);
    	modelMap.put("pages", page);
		modelMap.put("sizes", size);
		
		modelMap.put("fbMatches", fbMatches);
		
		
		return "listfingerbooks/fbmatches";
	}
	
	@RequestMapping(value="/{id}", params="table=compfblist", method = RequestMethod.GET)
	public String getCompFbList(@PathVariable("id") Long id, ModelMap modelMap, HttpServletRequest request) {
		
		
		Vector<CompositeFingerbook> compFbs = null;
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
				
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
    		
//    		SimilaritiesFeed similaritiesFeed = getSimilaritiesFeedREST(request, id, size, page);
    		CompositeFingerbookList compFbList = getCompositeFingerbookListREST(request, id, size, page);
			
			if(compFbList != null) {
				
				response = compFbList.getResponse();
				if(response != null) {
	    			//TODO: Nahuel: accion de error
					modelMap.put("response", response);
	    			return "listfingerbooks/error_response";
	    		}
				
//				similsFbs = similaritiesFeed.getSimilsFbs();
				compFbs = compFbList.getCompFbs();
				
				maxPages = (int) Math.ceil((double)compFbList.getTotalresults() / (double)size);
				
				if(compFbs != null) {
					System.out.println("COMPOSITE FBS LIST");
					for(CompositeFingerbook auxCompFb: compFbs) {
						
						System.out.println(auxCompFb);
					}
				}
				
			}
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	modelMap.put("maxPagesc", maxPages);
    	modelMap.put("pagec", page);
		modelMap.put("sizec", size);
		
		modelMap.put("compFbs", compFbs);
		
		
		return "listfingerbooks/compfblist";
	}
	
	public Vector<FingerbookMatch> getFingerbookMatches(Map<Fingerbook, Double> similsFbs) {
		
		Vector<FingerbookMatch> fbMatches = new Vector<FingerbookMatch>();
		
		if(similsFbs != null) {
			
			System.out.println("getFingerbookMatches");
			
			for(Entry<Fingerbook, Double> entry: similsFbs.entrySet()) {
				
				Fingerbook fb = entry.getKey();
				Double match = entry.getValue();
				
				System.out.println(fb + " --> " + match);
				
				FingerbookMatch fbMatch = new FingerbookMatch(fb, match);
				fbMatches.add(fbMatch);
			}
		}
		
		
		return fbMatches;
		
	}
	
//	@RequestMapping(value="/{id}", method = RequestMethod.GET)
//	public String get(@PathVariable("id") Long id, ModelMap modelMap, HttpServletRequest request) {
//		
//		Fingerbook fb = null;
//		String result = "";
//		
//		int size = LIMIT_PAG;
//		int page = 1;
//		int maxPages = 1;
//		
//		FingerbookFeed fingerbookFeed = null;
//		FingerprintsFeed fingerprintsFeed = null;
//		
//		Response response = null;
//		
//    	try {
//    		
////    		fb = getFingerbookRest(request, id);
//    		fingerbookFeed = getFingerbookRest(request, id);
//    		
//    		if(fingerbookFeed != null) {
//    			response = fingerbookFeed.getResponse();
//    		}
//    		
//    		if(response != null) {
//    			//TODO: Nahuel: accion de error
//    			modelMap.put("response", response);
//    			return "listfingerbooks/error_response";
//    		}
//    		
//    		fb = fingerbookFeed.getFingerbook();
//    		
//    		if(fb != null) {
//    		
//	    		if(request.getParameter("page") != null) {
//	    			page = Integer.parseInt(request.getParameter("page"));
//	    		}
//	    		if(request.getParameter("size") != null) {
//	    			size = Integer.parseInt(request.getParameter("size"));
//	    		}
//	    		fingerprintsFeed = getFingerprintsFeedREST(request, id, size, page);
//	    		
//	    		if(fingerprintsFeed != null) {
//	    			
//	    			response = fingerprintsFeed.getResponse();
//	    			if(response != null) {
//	        			//TODO: Nahuel: accion de error
//	    				modelMap.put("response", response);
//	        			return "listfingerbooks/error_response";
//	        		}
//	    			
//	    			maxPages = (int) Math.ceil((double)fingerprintsFeed.getTotalresults() / (double)size);
//	    			fb.setFingerPrints(fingerprintsFeed.getFingerPrints());
//	    			
//	    		}
//	
//	    		result = result + "<br />" + fb.toString();
//	    		System.out.println(fb.toString());
//    		}
//    		
//    		
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}
//    	
//    	String referer = request.getHeader("Referer");
//    	
//    	modelMap.put("referer", referer);
//    	
//    	modelMap.put("result", result);
//    	modelMap.put("fb", fb);
//    	
//    	modelMap.put("maxPages", maxPages);
//    	
//    	modelMap.put("page", page);
//		modelMap.put("size", size);
//		
//		if(request.getParameter("form") != null) {
//			return "listfingerbooks/update";
//		}
//		
//		return "listfingerbooks/show";
//	}
	
	
	public FingerbookList getFingerbookListRESTUser(HttpServletRequest request, String user, int size, int page) {
		
		FingerbookList fingerbookList = null;
		String urlStr = null;
		
    	try {
    		
    		String authMethod = getAuthMethod();
    		
    		if(authMethod.equalsIgnoreCase(ROLE_ADMIN)) {
    			
    			urlStr = URL_FINGERBOOKS_ADMIN_USER;
    			urlStr = urlStr + user;
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_USER)) {
    			urlStr = URL_FINGERBOOKS_USER;
    			
//    			String user = request.getUserPrincipal().getName();
    			urlStr = urlStr + user;
    		}
    		
    		else {
    			return fingerbookList;
    		}
    		
    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
    		RestTemplate restTemplate = buildRestTemplate(request, null);
    		fingerbookList = (FingerbookList) restTemplate.getForObject(urlStr, FingerbookList.class);
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
		return fingerbookList;
	}
	
	public FingerbookList getFingerbookListRESTTicket(HttpServletRequest request, String ticket, int size, int page) {
		
		FingerbookList fingerbookList = null;
		String urlStr = null;
		
    	try {
    		
    		String authMethod = getAuthMethod();
    		
    		if(authMethod.equalsIgnoreCase(ROLE_ADMIN)) {
    			
    			urlStr = URL_FINGERBOOKS_ADMIN_TICKET;
    			urlStr = urlStr + ticket;
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_SEMIAUTH)) {
    			urlStr = URL_FINGERBOOKS_TICKET;
    			
//    			String ticket = request.getUserPrincipal().getName();
    			urlStr = urlStr + ticket;
    		}
    		else {
    			return fingerbookList;
    		}
    		
    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
    		RestTemplate restTemplate = buildRestTemplate(request, null);
    		fingerbookList = (FingerbookList) restTemplate.getForObject(urlStr, FingerbookList.class);
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
		return fingerbookList;
	}
	
	public FingerbookList getFingerbookListRESTUserLogged(HttpServletRequest request, int size, int page) {
		
		FingerbookList fingerbookList = null;
		String urlStr = null;
		
    	try {
    		
    		String authMethod = getAuthMethod();
    		
    		if(authMethod.equalsIgnoreCase(ROLE_USER)) {
    			urlStr = URL_FINGERBOOKS_USER;
    			String user = request.getUserPrincipal().getName();
    			urlStr = urlStr + user;
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_SEMIAUTH)) {
    			urlStr = URL_FINGERBOOKS_TICKET;
    			String ticket = request.getUserPrincipal().getName();
    			urlStr = urlStr + ticket;
    		}
    		else {
    			return fingerbookList;
    		}
    		
    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
    		RestTemplate restTemplate = buildRestTemplate(request, null);
    		fingerbookList = (FingerbookList) restTemplate.getForObject(urlStr, FingerbookList.class);
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
		return fingerbookList;
	}
	
	public FingerbookList getFingerbookListRESTHash(HttpServletRequest request, String hash, int size, int page) {
		
		FingerbookList fingerbookList = null;
		String urlStr = null;
		
    	try {
    		
    		urlStr = URL_FINGERBOOKS_HASH;
    		urlStr = urlStr + hash;
    		
    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
    		RestTemplate restTemplate = buildRestTemplate(request, null);
    		fingerbookList = (FingerbookList) restTemplate.getForObject(urlStr, FingerbookList.class);
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
		return fingerbookList;
	}
	
	public FingerprintsFeed getFingerprintsFeedREST(HttpServletRequest request, Long id, int size, int page) {
		
		FingerprintsFeed fingerprintsFeed = null;
		String urlStr = null;
		
    	try {
    		
    		String authMethod = getAuthMethod();
    		
    		if(authMethod.equalsIgnoreCase(ROLE_ADMIN)) {
    			urlStr = URL_FINGEPRINTS_ADMIN;
    			urlStr = urlStr + id;
    			
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_USER)) {
    			urlStr = URL_FINGEPRINTS_AUTH;
    			urlStr = urlStr + id;
    			
    			String user = request.getUserPrincipal().getName();
    			urlStr = urlStr + "/user/" + user;
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_SEMIAUTH)) {
    			urlStr = URL_FINGEPRINTS_SEMIAUTH;
    			urlStr = urlStr + id;
    			
    			String ticket = request.getUserPrincipal().getName();
    			urlStr = urlStr + "/ticket/" + ticket;
    		}
    		else {
    			urlStr = URL_FINGEPRINTS_ANON;
    			urlStr = urlStr + id;
    		}
    		
    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
//    		String fingerPrintsUrl = "http://localhost:8080/fingerbookRESTM/fingerbooks/fingerprints/";
//    		fingerPrintsUrl = fingerPrintsUrl + id;
//    		fingerPrintsUrl = fingerPrintsUrl + "/limit/" + size + "/offset/" + (page-1)  * size;

//    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
//    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
    		
    		RestTemplate restTemplate = buildRestTemplate(request, null);
    		fingerprintsFeed = (FingerprintsFeed) restTemplate.getForObject(urlStr, FingerprintsFeed.class);
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
		return fingerprintsFeed;
	}
	
	public FingerprintsFeed getCompFingerprintsFeedREST(HttpServletRequest request, String id, int size, int page) {
		
		FingerprintsFeed fingerprintsFeed = null;
		String urlStr = null;
		
    	try {
    		
    		String authMethod = getAuthMethod();
    		
    		if(authMethod.equalsIgnoreCase(ROLE_ADMIN)) {
    			urlStr = URL_COMPFINGEPRINTS_ADMIN;
    			urlStr = urlStr + id;
    			
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_USER)) {
    			urlStr = URL_COMPFINGEPRINTS_AUTH;
    			urlStr = urlStr + id;
    			
    			String user = request.getUserPrincipal().getName();
    			urlStr = urlStr + "/user/" + user;
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_SEMIAUTH)) {
    			urlStr = URL_COMPFINGEPRINTS_SEMIAUTH;
    			urlStr = urlStr + id;
    			
    			String ticket = request.getUserPrincipal().getName();
    			urlStr = urlStr + "/ticket/" + ticket;
    		}
    		else {
    			urlStr = URL_COMPFINGEPRINTS_ANON;
    			urlStr = urlStr + id;
    		}
    		
    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
    		
    		RestTemplate restTemplate = buildRestTemplate(request, null);
    		fingerprintsFeed = (FingerprintsFeed) restTemplate.getForObject(urlStr, FingerprintsFeed.class);
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
		return fingerprintsFeed;
	}
	
	
	public SimilaritiesFeed getSimilaritiesFeedREST(HttpServletRequest request, Long id, int size, int page) {
		
		SimilaritiesFeed similaritiesFeed = null;
		String urlStr = null;
		
    	try {
    		
    		String authMethod = getAuthMethod();
    		
    		if(authMethod.equalsIgnoreCase(ROLE_ADMIN)) {
    			urlStr = URL_MATCHES_ADMIN;
    			urlStr = urlStr + id;
    			
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_USER)) {
    			urlStr = URL_MATCHES_AUTH;
    			urlStr = urlStr + id;
    			
    			String user = request.getUserPrincipal().getName();
    			urlStr = urlStr + "/user/" + user;
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_SEMIAUTH)) {
    			urlStr = URL_MATCHES_SEMIAUTH;
    			urlStr = urlStr + id;
    			
    			String ticket = request.getUserPrincipal().getName();
    			urlStr = urlStr + "/ticket/" + ticket;
    		}
    		else {
    			urlStr = URL_MATCHES_ANON;
    			urlStr = urlStr + id;
    		}
    		
    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
    		
    		RestTemplate restTemplate = buildRestTemplate(request, null);
    		similaritiesFeed = (SimilaritiesFeed) restTemplate.getForObject(urlStr, SimilaritiesFeed.class);
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
		return similaritiesFeed;
	}
	
	public CompositeFingerbookList getCompositeFingerbookListREST(HttpServletRequest request, Long id, int size, int page) {
		
		CompositeFingerbookList compositeFingerbookList = null;
		String urlStr = null;
		
    	try {
    		
    		String authMethod = getAuthMethod();
    		
    		if(authMethod.equalsIgnoreCase(ROLE_ADMIN)) {
    			urlStr = URL_COMPFBLIST_ADMIN;
    			urlStr = urlStr + id;
    			
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_USER)) {
    			urlStr = URL_COMPFBLIST_AUTH;
    			urlStr = urlStr + id;
    			
    			String user = request.getUserPrincipal().getName();
    			urlStr = urlStr + "/user/" + user;
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_SEMIAUTH)) {
    			urlStr = URL_COMPFBLIST_SEMIAUTH;
    			urlStr = urlStr + id;
    			
    			String ticket = request.getUserPrincipal().getName();
    			urlStr = urlStr + "/ticket/" + ticket;
    		}
    		else {
    			urlStr = URL_COMPFBLIST_ANON;
    			urlStr = urlStr + id;
    		}
    		
    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
    		
    		RestTemplate restTemplate = buildRestTemplate(request, null);
    		
//    		similaritiesFeed = (SimilaritiesFeed) restTemplate.getForObject(urlStr, SimilaritiesFeed.class);
    		compositeFingerbookList = (CompositeFingerbookList) restTemplate.getForObject(urlStr, CompositeFingerbookList.class);
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
		return compositeFingerbookList;
	}

	
	public FingerbookFeed getFingerbookRest(HttpServletRequest request, Long id, int size, int page, int sizeS, int pageS) {
		
		FingerbookFeed fingerbookFeed = null;
		String urlStr = null;
		
    	try {
    		
    		String authMethod = getAuthMethod();
    		
    		if(authMethod.equalsIgnoreCase(ROLE_ADMIN)) {
    			urlStr = URL_FINGERBOOK_ADMIN;
    			urlStr = urlStr + id;
    			
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_USER)) {
    			urlStr = URL_FINGERBOOK_USER;
    			urlStr = urlStr + id;
    			
    			String user = request.getUserPrincipal().getName();
    			urlStr = urlStr + "/user/" + user;
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_SEMIAUTH)) {
    			urlStr = URL_FINGERBOOK_TICKET;
    			urlStr = urlStr + id;
    			
    			String ticket = request.getUserPrincipal().getName();
    			urlStr = urlStr + "/ticket/" + ticket;
    		}
    		else {
    			urlStr = URL_FINGERBOOK_ANON;
    			urlStr = urlStr + id;
    		}
    		
    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
    		
    		urlStr = urlStr + "/limits/" + sizeS + "/offsets/" + (pageS-1)  * sizeS;
    		
    		RestTemplate restTemplate = buildRestTemplate(request, null);
    		fingerbookFeed = (FingerbookFeed) restTemplate.getForObject(urlStr, FingerbookFeed.class);
    		
//    		System.out.println(fb.toString());
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return fingerbookFeed;
	}
	
	
	public CompositeFingerbookFeed getCompositeFingerbookFeedRest(HttpServletRequest request, String id, int size, int page) {
		
		CompositeFingerbookFeed compositeFingerbookFeed = null;
		String urlStr = null;
		
    	try {
    		
    		String authMethod = getAuthMethod();
    		
    		if(authMethod.equalsIgnoreCase(ROLE_ADMIN)) {
    			urlStr = URL_COMPFINGERBOOK_ADMIN;
    			urlStr = urlStr + id;
    			
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_USER)) {
    			urlStr = URL_COMPFINGERBOOK_USER;
    			urlStr = urlStr + id;
    			
    			String user = request.getUserPrincipal().getName();
    			urlStr = urlStr + "/user/" + user;
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_SEMIAUTH)) {
    			urlStr = URL_COMPFINGERBOOK_TICKET;
    			urlStr = urlStr + id;
    			
    			String ticket = request.getUserPrincipal().getName();
    			urlStr = urlStr + "/ticket/" + ticket;
    		}
    		else {
    			urlStr = URL_COMPFINGERBOOK_ANON;
    			urlStr = urlStr + id;
    		}
    		
    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
    		
    		
    		RestTemplate restTemplate = buildRestTemplate(request, null);
//    		fingerbookFeed = (FingerbookFeed) restTemplate.getForObject(urlStr, FingerbookFeed.class);
    		compositeFingerbookFeed = (CompositeFingerbookFeed) restTemplate.getForObject(urlStr, CompositeFingerbookFeed.class);
    		
//    		System.out.println(fb.toString());
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return compositeFingerbookFeed;
	}
	
	
//	public FingerbookFeed getFingerbookRest(HttpServletRequest request, Long id, int size, int page) {
//		
//		FingerbookFeed fingerbookFeed = null;
//		String urlStr = null;
//		
//    	try {
//    		
//    		String authMethod = getAuthMethod();
//    		
//    		if(authMethod.equalsIgnoreCase(ROLE_USER)) {
//    			urlStr = URL_FINGERBOOK_USER;
//    			urlStr = urlStr + id;
//    			
//    			String user = request.getUserPrincipal().getName();
//    			urlStr = urlStr + "/user/" + user;
//    		}
//    		else if(authMethod.equalsIgnoreCase(ROLE_SEMIAUTH)) {
//    			urlStr = URL_FINGERBOOK_TICKET;
//    			urlStr = urlStr + id;
//    			
//    			String ticket = request.getUserPrincipal().getName();
//    			urlStr = urlStr + "/ticket/" + ticket;
//    		}
//    		else {
//    			urlStr = URL_FINGERBOOK_ANON;
//    			urlStr = urlStr + id;
//    		}
//    		
//    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
//    		
//    		RestTemplate restTemplate = buildRestTemplate(request, null);
//    		fingerbookFeed = (FingerbookFeed) restTemplate.getForObject(urlStr, FingerbookFeed.class);
//    		
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}
//    	
//    	return fingerbookFeed;
//	}
	
//	public FingerbookFeed getFingerbookRest(HttpServletRequest request, Long id) {
//		
//		FingerbookFeed fingerbookFeed = null;
//		String urlStr = null;
//		
//    	try {
//    		
//    		String authMethod = getAuthMethod();
//    		
//    		if(authMethod.equalsIgnoreCase(ROLE_USER)) {
//    			urlStr = URL_FINGERBOOK_USER;
//    			urlStr = urlStr + id;
//    			
//    			String user = request.getUserPrincipal().getName();
//    			urlStr = urlStr + "/user/" + user;
//    		}
//    		else if(authMethod.equalsIgnoreCase(ROLE_SEMIAUTH)) {
//    			urlStr = URL_FINGERBOOK_TICKET;
//    			urlStr = urlStr + id;
//    			
//    			String ticket = request.getUserPrincipal().getName();
//    			urlStr = urlStr + "/ticket/" + ticket;
//    		}
//    		else {
//    			urlStr = URL_FINGERBOOK_ANON;
//    			urlStr = urlStr + id;
//    		}
//    		
//    		RestTemplate restTemplate = buildRestTemplate(request, null);
//    		fingerbookFeed = (FingerbookFeed) restTemplate.getForObject(urlStr, FingerbookFeed.class);
//    		
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}
//    	
//    	return fingerbookFeed;
//	}
	

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
    
//    @RequestMapping(value="/{id}", method = RequestMethod.GET)
//	public String get(@PathVariable("id") Long id, ModelMap modelMap, HttpServletRequest request) {
//		
//		Fingerbook fb = null;
//		String result = "";
//		
//		int size = LIMIT_PAG;
//		int page = 1;
//		int maxPages = 1;
//		
//		FingerprintsFeed fingerprintsFeed = null;
//		
//    	try {
//    		
//    		String urlStr = "http://localhost:8080/fingerbookRESTM/fingerbooks/fingerbook/";
//    		urlStr = urlStr + id;
//    		
//    		String fingerPrintsUrl = "http://localhost:8080/fingerbookRESTM/fingerbooks/fingerprints/";
//    		fingerPrintsUrl = fingerPrintsUrl + id;
//    		
//    		if(request.getParameter("page") != null) {
//    			page = Integer.parseInt(request.getParameter("page"));
//    		}
//    		if(request.getParameter("size") != null) {
//    			size = Integer.parseInt(request.getParameter("size"));
//    		}
//    		fingerPrintsUrl = fingerPrintsUrl + "/limit/" + size + "/offset/" + (page-1)  * size;
//
//    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
//    		
//    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
//    		fb = (Fingerbook) restTemplate.getForObject(urlStr, Fingerbook.class);
//    		
//    		fingerprintsFeed = (FingerprintsFeed) restTemplate.getForObject(fingerPrintsUrl, FingerprintsFeed.class);
//    		
//    		if(fingerprintsFeed != null) {
//    			
//    			maxPages = (int) Math.ceil((double)fingerprintsFeed.getTotalresults() / (double)size);
//    			
//    			fb.setFingerPrints(fingerprintsFeed.getFingerPrints());
//    			
//    		}
//
//    		result = result + "<br />" + fb.toString();
//    		System.out.println(fb.toString());
//    		
//    		
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}
//    	
//    	modelMap.put("result", result);
//    	modelMap.put("fb", fb);
//    	
//    	modelMap.put("maxPages", maxPages);
//    	
//    	modelMap.put("page", page);
//		modelMap.put("size", size);
//		
//		if(request.getParameter("form") != null) {
//			return "listfingerbooks/update";
//		}
//		
//		return "listfingerbooks/show";
//	}
    
//	@RequestMapping(value="/{id}", method = RequestMethod.GET)
//	public String get(@PathVariable("id") Long id, ModelMap modelMap, HttpServletRequest request) {
//		
//		Fingerbook fb = null;
//		String result = "";
//		
//		int size = LIMIT_PAG;
//		int page = 1;
//		
//    	try {
//    		
//    		String urlStr = "http://localhost:8080/fingerbookRESTM/fingerbooks/fingerbook/";
//    		urlStr = urlStr + id;
//    		
//    		if(request.getParameter("page") != null) {
//    			page = Integer.parseInt(request.getParameter("page"));
//    		}
//    		if(request.getParameter("size") != null) {
//    			size = Integer.parseInt(request.getParameter("size"));
//    		}
//    		
//    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
//
//    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
//    		
//    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
//    		fb = (Fingerbook) restTemplate.getForObject(urlStr, Fingerbook.class);
//
//    		result = result + "<br />" + fb.toString();
//    		System.out.println(fb.toString());
//    		
//    		
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}
//    	
//    	modelMap.put("result", result);
//    	modelMap.put("fb", fb);
//    	
//    	modelMap.put("maxPages", 3);
//		
//		return "listfingerbooks/show";
//	}
	
	
//	@RequestMapping(value="/{id}", method = RequestMethod.GET)
//	public String get(@PathVariable("id") Long id, ModelMap modelMap) {
//		
//		Fingerbook fb = null;
//		String result = "";
//    	try {
//    		
//    		String urlStr = "http://localhost:8080/fingerbookRESTM/fingerbooks/fingerbook/";
//    		urlStr = urlStr + id;
//
//    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
//    		
//    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
//    		fb = (Fingerbook) restTemplate.getForObject(urlStr, Fingerbook.class);
//
//    		result = result + "<br />" + fb.toString();
//    		System.out.println(fb.toString());
//    		
//    		
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}
//    	
//    	modelMap.put("result", result);
//    	modelMap.put("fb", fb);
//    	
//    	modelMap.put("maxPages", 3);
//		
//		return "listfingerbooks/show";
//	}
//	
//	@RequestMapping(value="/pag/{id}", method = RequestMethod.GET)
//	public String get(@PathVariable("id") Long id, @RequestParam("page") int page, @RequestParam("size") int size, ModelMap modelMap) {
//		
//		Fingerbook fb = null;
//		String result = "";
//    	try {
//    		
//    		String urlStr = "http://localhost:8080/fingerbookRESTM/fingerbooks/fingerbook/";
//    		urlStr = urlStr + id;
//    		
//    		urlStr = urlStr + "/limit/" + size + "/offset/" + page * size;
//
//    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
//    		
//    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
//    		fb = (Fingerbook) restTemplate.getForObject(urlStr, Fingerbook.class);
//
//    		result = result + "<br />" + fb.toString();
//    		System.out.println(fb.toString());
//    		
//    		
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}
//    	
//    	modelMap.put("result", result);
//    	modelMap.put("fb", fb);
//    	
//    	modelMap.put("maxPages", 3);
//		
//		return "listfingerbooks/show";
//	}
	
}
