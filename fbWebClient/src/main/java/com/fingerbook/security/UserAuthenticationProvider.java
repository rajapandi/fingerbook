package com.fingerbook.security;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
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
import org.springframework.util.StringUtils;

public class UserAuthenticationProvider extends
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

        String password = (String) authentication.getCredentials();
        if (!StringUtils.hasText(password)) {
            throw new BadCredentialsException("Please enter password");
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        try {
            // TODO: Authenticate using fingerbookRESTM
        	authenticateAgainstRESTM(username, password);
        	
        	// Speaker speaker = Speaker.findSpeakersByEmailAndPasswordEquals(username, password).getSingleResult();
            authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
        } catch (EmptyResultDataAccessException e) {
            throw new BadCredentialsException("Invalid username or password");
        } catch (NonUniqueResultException e) {
            throw new BadCredentialsException("Non-unique user, contact administrator");
        } catch (Exception e) {
        	throw new BadCredentialsException("Invalid username or password");
        }
        return new User(username, password, true, // enabled
                true, // account not expired
                true, // credentials not expired
                true, // account not locked
                authorities);
	}

	
	private void authenticateAgainstRESTM(String username, String password) throws EmptyResultDataAccessException, 
	BadCredentialsException, Exception {
    		
		// TODO: Hardwired URL
		URL url = new URL("http://localhost:8080/fingerbookRESTM/authenticate/"+ username + "/" + password);
		URLConnection conn = url.openConnection ();

		// Get the response
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line;
		// TODO: Use xml marshalling and unmarshalling instead of parsing the lines
		line = rd.readLine();
		
		rd.close();
		
		// TODO: Ugly succesful login checking
		if(!line.contains("Authentication succesful for user")) {
			throw new BadCredentialsException("Invalid credentials");
		}
	}

}