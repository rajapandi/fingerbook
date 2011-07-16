package com.fingerbook.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Response;
import com.fingerbook.models.UserInfo;
import com.fingerbook.models.transfer.FingerbookList;
import com.fingerbook.models.transfer.FingerprintsFeed;



@RequestMapping("/listfingerbooks/**")
@Controller
public class ListFingerbooksController {
	
//	public static final String URL_FINGERBOOK = "http://localhost:8080/fingerbookRESTM/fingerbooks/fingerbook/";
	public static final String URL_FINGERBOOK_USER = "http://localhost:8080/fingerbookRESTM/fingerbooks/authenticated/fingerbook/";
	public static final String URL_FINGERBOOK_TICKET = "http://localhost:8080/fingerbookRESTM/fingerbooks/semiauthenticated/fingerbook/";
	public static final String URL_FINGERBOOK_ANON = "http://localhost:8080/fingerbookRESTM/fingerbooks/anonymous/fingerbook/";
	
	public static final String URL_FINGEPRINTS_AUTH = "http://localhost:8080/fingerbookRESTM/fingerbooks/authenticated/fingerprints/";
	public static final String URL_FINGEPRINTS_SEMIAUTH = "http://localhost:8080/fingerbookRESTM/fingerbooks/semiauthenticated/fingerprints/";
	public static final String URL_FINGEPRINTS_ANON = "http://localhost:8080/fingerbookRESTM/fingerbooks/anonymous/fingerprints/";
	
	public static final String URL_FINGERBOOKS_USER = "http://localhost:8080/fingerbookRESTM/fingerbooks/authenticated/user/";
	public static final String URL_FINGERBOOKS_TICKET = "http://localhost:8080/fingerbookRESTM/fingerbooks/semiauthenticated/ticket/";
	public static final String URL_FINGERBOOKS_HASH = "http://localhost:8080/fingerbookRESTM/fingerbooks/hash/";
	public static final String URL_UPDATE_USER = "http://localhost:8080/fingerbookRESTM/fingerbooks/authenticated/update/";
	public static final String URL_UPDATE_TICKET = "http://localhost:8080/fingerbookRESTM/fingerbooks/semiauthenticated/update/";
	
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_SEMIAUTH = "ROLE_SEMIAUTH";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
	
	
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
	
	@RequestMapping(value="/ticket", method = RequestMethod.GET)
//	public String listByTicket(@RequestParam("ticket_input") String ticket, @RequestParam("page_number") int page, ModelMap modelMap) {
	public String listByTicket(@RequestParam("ticket_input") String ticket, ModelMap modelMap, HttpServletRequest request) {
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
		FingerbookList fingerbookList = null;
		Vector<Fingerbook> fbs = null;
		String result = "";
    	try {
    		
//    		String urlStr = "http://localhost:8080/fingerbookRESTM/fingerbooks/ticket/";
    		String urlStr = URL_FINGERBOOKS_TICKET;
    		urlStr = urlStr + ticket;
    		
    		if(request.getParameter("page") != null) {
    			page = Integer.parseInt(request.getParameter("page"));
    		}
    		if(request.getParameter("size") != null) {
    			size = Integer.parseInt(request.getParameter("size"));
    		}
    		
    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;
    		
    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
    		
    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
    		
//    		fbs = (List<Fingerbook>) restTemplate.getForObject(urlStr, List.class);
    		fingerbookList = (FingerbookList) restTemplate.getForObject(urlStr, FingerbookList.class);
    		

    		if(fingerbookList != null) {
    			
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
    	
    	modelMap.put("result", result);
    	modelMap.put("fbs", fbs);
    	
    	modelMap.put("maxPages", maxPages);
    	
    	modelMap.put("requiredParams", "ticket_input");
		
//        return "listfingerbooks/list";
    	return "listfingerbooks/list_by_user";
    	
    }

	
	@RequestMapping(value="/user", method = RequestMethod.GET)
	public String listByUser(@RequestParam("user_input") String user, ModelMap modelMap, HttpServletRequest request) {
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
		FingerbookList fingerbookList = null;
		Vector<Fingerbook> fbs = null;
		String result = "";
    	try {
    		
    		if(request.getParameter("page") != null) {
    			page = Integer.parseInt(request.getParameter("page"));
    		}
    		if(request.getParameter("size") != null) {
    			size = Integer.parseInt(request.getParameter("size"));
    		}
    		
    		fingerbookList = getFingerbookListRESTUser(request, size, page);
    		
    		
    		if(fingerbookList != null) {
    			
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
    	
    	modelMap.put("result", result);
    	modelMap.put("fbs", fbs);
    	
    	modelMap.put("maxPages", maxPages);
    	
    	modelMap.put("requiredParams", "user_input");
		
        return "listfingerbooks/list_by_user";
    }
	
	public RestTemplate buildRestTemplate(HttpServletRequest request, Fingerbook loadfb) {
		
		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
		UserInfo userInfo = new UserInfo();
		
		String role = getAuthMethod();
		
		System.out.println("ROLE AUTH METHOD!! " + role);
		
		if(role.equalsIgnoreCase(ROLE_USER)) {
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
		
		Response ret = null;
		Fingerbook fb = null;
		Fingerbook loadfb = null;
		String result = "";
		
		Long id = null;
		String comment = "";
		Set<String> tags = new HashSet<String>();
		String[] tagsInput = null;
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
		String urlStr = null;
		FingerprintsFeed fingerprintsFeed = null;
		
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
    		
    		if(authMethod.equalsIgnoreCase(ROLE_USER)) {
    			urlStr = URL_UPDATE_USER;
    		}
    		else if(authMethod.equalsIgnoreCase(ROLE_SEMIAUTH)) {
    			urlStr = URL_UPDATE_TICKET;
    		}
    		else {
    			urlStr = URL_UPDATE_TICKET;
    		}
    		
    		RestTemplate restTemplate = buildRestTemplate(request, loadfb);
    		ret = restTemplate.postForObject(urlStr, loadfb, Response.class);

    		
    		fb = getFingerbookRest(request, id);
    		
    		if(request.getParameter("page") != null) {
    			page = Integer.parseInt(request.getParameter("page"));
    		}
    		if(request.getParameter("size") != null) {
    			size = Integer.parseInt(request.getParameter("size"));
    		}
    		fingerprintsFeed = getFingerprintsFeedREST(request, id, size, page);
    		
    		if(fingerprintsFeed != null) {
    			
    			maxPages = (int) Math.ceil((double)fingerprintsFeed.getTotalresults() / (double)size);
    			
    			fb.setFingerPrints(fingerprintsFeed.getFingerPrints());
    			
    		}

    		result = result + "<br />" + fb.toString();
    		System.out.println(fb.toString());

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	modelMap.put("result", result);
    	modelMap.put("fb", fb);
    	
    	modelMap.put("maxPages", maxPages);
    	
    	modelMap.put("page", page);
		modelMap.put("size", size);
		
		return "listfingerbooks/show";
    }
	
//	@RequestMapping(value="/user", method = RequestMethod.GET)
//	public String listByUser(@RequestParam("user_input") String user, ModelMap modelMap, HttpServletRequest request) {
//		
//		int size = LIMIT_PAG;
//		int page = 1;
//		
//		List<Fingerbook> fbs = null;
//		String result = "";
//    	try {
//    		
//    		String urlStr = "http://localhost:8080/fingerbookRESTM/fingerbooks/user/";
//    		urlStr = urlStr + user;
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
//    		fbs = (List<Fingerbook>) restTemplate.getForObject(urlStr, List.class);
//    		    		
//    		for(Fingerbook fb: fbs) {
//    			result = result + "<br />" + fb.toString();
//    			System.out.println(fb.toString());
//    		}
//    		
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}
//    	
//    	modelMap.put("result", result);
//    	modelMap.put("fbs", fbs);
//    	
//    	modelMap.put("maxPages", 3);
//		
//        return "listfingerbooks/list";
//    }
	
	@RequestMapping(value="/hash", method = RequestMethod.GET)
	public String listByHash(@RequestParam("hash_input") String hash, ModelMap modelMap, HttpServletRequest request) {
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
		FingerbookList fingerbookList = null;
		Vector<Fingerbook> fbs = null;
		String result = "";
    	try {
    		
//    		String urlStr = "http://localhost:8080/fingerbookRESTM/fingerbooks/hash/";
    		String urlStr = URL_FINGERBOOKS_HASH;
    		urlStr = urlStr + hash;

    		if(request.getParameter("page") != null) {
    			page = Integer.parseInt(request.getParameter("page"));
    		}
    		if(request.getParameter("size") != null) {
    			size = Integer.parseInt(request.getParameter("size"));
    		}
    		
    		urlStr = urlStr + "/limit/" + size + "/offset/" + (page-1)  * size;

    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
    		
    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
    		
    		fingerbookList = (FingerbookList) restTemplate.getForObject(urlStr, FingerbookList.class);

    		
    		if(fingerbookList != null) {
    			
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
    	modelMap.put("result", result);
    	modelMap.put("fbs", fbs);
    	
    	modelMap.put("maxPages", maxPages);
    	
    	modelMap.put("requiredParams", "hash_input");
		
        return "listfingerbooks/list_by_hash";
    	
    }
	
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public String get(@PathVariable("id") Long id, ModelMap modelMap, HttpServletRequest request) {
		
		Fingerbook fb = null;
		String result = "";
		
		int size = LIMIT_PAG;
		int page = 1;
		int maxPages = 1;
		
		FingerprintsFeed fingerprintsFeed = null;
		
    	try {
    		
    		fb = getFingerbookRest(request, id);
    		
    		if(request.getParameter("page") != null) {
    			page = Integer.parseInt(request.getParameter("page"));
    		}
    		if(request.getParameter("size") != null) {
    			size = Integer.parseInt(request.getParameter("size"));
    		}
    		fingerprintsFeed = getFingerprintsFeedREST(request, id, size, page);
    		
    		if(fingerprintsFeed != null) {
    			
    			maxPages = (int) Math.ceil((double)fingerprintsFeed.getTotalresults() / (double)size);
    			
    			fb.setFingerPrints(fingerprintsFeed.getFingerPrints());
    			
    		}

    		result = result + "<br />" + fb.toString();
    		System.out.println(fb.toString());
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	modelMap.put("result", result);
    	modelMap.put("fb", fb);
    	
    	modelMap.put("maxPages", maxPages);
    	
    	modelMap.put("page", page);
		modelMap.put("size", size);
		
		if(request.getParameter("form") != null) {
			return "listfingerbooks/update";
		}
		
		return "listfingerbooks/show";
	}
	
	public FingerbookList getFingerbookListRESTUser(HttpServletRequest request, int size, int page) {
		
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
    		
    		if(authMethod.equalsIgnoreCase(ROLE_USER)) {
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
	
	public Fingerbook getFingerbookRest(HttpServletRequest request, Long id) {
		
		Fingerbook fb = null;
		String urlStr = null;
		
    	try {
    		
    		String authMethod = getAuthMethod();
    		
    		if(authMethod.equalsIgnoreCase(ROLE_USER)) {
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
    		
    		RestTemplate restTemplate = buildRestTemplate(request, null);
    		fb = (Fingerbook) restTemplate.getForObject(urlStr, Fingerbook.class);
    		
    		System.out.println(fb.toString());
    		
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return fb;
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
