package com.fingerbook.security;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.fingerbook.web.FingerbookWebClientUtils;

public class TicketAuthenticationProvider extends
		AbstractUserDetailsAuthenticationProvider {

	@Override
	protected void additionalAuthenticationChecks(UserDetails arg0,
			UsernamePasswordAuthenticationToken arg1)
			throws AuthenticationException {
		// TODO Auto-generated method stub

	}

	// TODO: Incorrect Exceptions treatment
	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {

		String ticket = username;
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        try {
       
        	String role = authenticateAgainstRESTM(ticket);    
        	
            authorities.add(new GrantedAuthorityImpl(role));
            
        } catch (EmptyResultDataAccessException e) {
            throw new BadCredentialsException("Invalid username or password");
        } catch (NonUniqueResultException e) {
            throw new BadCredentialsException("Non-unique user, contact administrator");
        } catch (Exception e) {
        	throw new BadCredentialsException("Invalid username or password");
        }
        return new User(ticket, "", true, // enabled
                true, // account not expired
                true, // credentials not expired
                true, // account not locked
                authorities);
	}

	
	private String authenticateAgainstRESTM(String ticket) throws EmptyResultDataAccessException, 
	BadCredentialsException, Exception {
    	
		// Construct parameters
	    String parameters = URLEncoder.encode("ticket", "UTF-8") + "=" + URLEncoder.encode(ticket, "UTF-8");

		String urlString = "http://localhost:8080/fingerbookRESTM/ticketAuthenticate";	
		
		StringBuffer sb = FingerbookWebClientUtils.makeBasicPostRequest(urlString, parameters);	
		
		// TODO: Ugly succesful login checking
		if(!sb.toString().contains("Authentication succesful for ticket")) {
			throw new BadCredentialsException("Invalid credentials");
		}
		
		// Return the assigned role
		return sb.substring(sb.toString().indexOf("[")+1, sb.toString().indexOf("]"));
	}
	
	@Override
	public boolean supports(Class<? extends Object> authentication) {
	    return (TicketAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
