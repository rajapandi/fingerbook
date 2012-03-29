package com.fingerbook.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.fingerbook.models.SpringSecurityUser;

@RequestMapping("/listUsers")
@Controller
public class ListUsersController {

	// Show
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public String show(@PathVariable("username") String username, ModelMap uiModel) {
    	
    	SpringSecurityUser user = getUser(username);
    	
		uiModel.addAttribute("user", user);
        uiModel.addAttribute("itemId", username);
        
        return "listusers/show";
    }
    
    // Update
    @RequestMapping(value = "/{username}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("username") String username, ModelMap uiModel) {
    	
    	SpringSecurityUser user = getUser(username);
    	
		uiModel.addAttribute("user", user);
        uiModel.addAttribute("itemId", username);
        
        return "modifyuser/modifyUserForm";
    }
    
    // Create
	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(ModelMap uiModel) {
		return "createuser/createUserForm";
	}
	   
    // Delete
    @RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("username") String username, ModelMap uiModel) {
    	
    	SpringSecurityUser user = getUser(username);
    	
		uiModel.addAttribute("user", user);
        uiModel.addAttribute("itemId", username);
        
        return "deleteuser/deleteUserForm";
    }
	
    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    	
    	List<SpringSecurityUser> users = null;
    	String result = "";
    	
    	users = getUsers(request);
    		
		for(SpringSecurityUser user: users) {
			result = result + "<br />" + user.toString();
			System.out.println(user.toString());
		}
    	
    	modelMap.put("maxPages", 3);
    	modelMap.put("result", result);
    	modelMap.put("users", users);
    	
    	return "listusers/list";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }

    @RequestMapping
    public String index() {
        return "listusers/index";
    }
    
    @SuppressWarnings("unchecked")
	private List<SpringSecurityUser> getUsers(HttpServletRequest request) {
    	
    	List<SpringSecurityUser> users = null;
    	
    	try {
    		
    		String authenticatedUser = request.getUserPrincipal().getName();
    		String authenticatedUserPasswword = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
    		
    		String urlStr = "http://localhost:8080/fingerbookRESTM/admin/listUsers/";
    		
    		/* -------------------------------------- 1
    		URL url = new URL(urlStr);
    		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    		conn.setRequestMethod("POST");
    		
    		// Basic Authentication: Setting Auth Cred
            conn.setRequestProperty("Authorization", "Basic " +
            		new String(Base64.encode(new String(authenticatedUser + ":" + authenticatedUserPasswword).getBytes())));    
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    		conn.setRequestProperty("Content-Language", "en-US"); 

    		
    		conn.setDoOutput(true);
    		conn.setDoInput(true);
    		conn.setUseCaches(false);
    		conn.setAllowUserInteraction(true);
    		
    		
    		//Send request
    	    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
    	    wr.flush();
    	    wr.close();
    	      
    	    users = (List<SpringSecurityUser>) conn.getContent();
    	    */
    	    
    		// Get the response
    	    /*
    		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    		
    		StringBuffer sb = new StringBuffer();
    		String line;
    		while ((line = rd.readLine()) != null) {
    			sb.append(line);
    		}
    		rd.close();
    		
    		return sb;
    		*/
    	    
    		/* -----------------------------------------------2
    		HttpClient client = new HttpClient();
    		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(authenticatedUser,authenticatedUserPasswword);
    		client.getState().setCredentials(new AuthScope("localhost", 8080, AuthScope.ANY_REALM), credentials);
    		CommonsClientHttpRequestFactory commons = new CommonsClientHttpRequestFactory(client);

    		RestTemplate template = new RestTemplate(commons);
    		users = (List<SpringSecurityUser>) template.getForObject(urlStr, List.class);
    		*/
    		
    		
    		// -----------------------------------------------3
    		WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
    		
    		RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
    		users = (List<SpringSecurityUser>) restTemplate.getForObject(urlStr, List.class);
    		
    	} catch (BeansException e) {
			e.printStackTrace();
		} catch (RestClientException e) {
			e.printStackTrace();
		}
    	
    	return users;
    }
    
    private SpringSecurityUser getUser(String username) {
    	
    	SpringSecurityUser user = null;
    	
    	try {
			String urlStr = "http://localhost:8080/fingerbookRESTM/admin/listUsers/" + username;
			
			WebApplicationContext wap = ContextLoaderListener.getCurrentWebApplicationContext();
			
			RestTemplate restTemplate = wap.getBean("restTemplate", RestTemplate.class);
			user = (SpringSecurityUser) restTemplate.getForObject(urlStr, SpringSecurityUser.class);
			
		} catch (BeansException e) {
			e.printStackTrace();
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		return user;
    }
}
