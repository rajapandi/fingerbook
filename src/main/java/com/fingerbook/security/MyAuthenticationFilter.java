package com.fingerbook.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class MyAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {

	public MyAuthenticationFilter() {
		super();
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		String ticket = request.getParameter("j_ticket");
		String username = request.getParameter("j_username");
		String password = request.getParameter("j_password");
	
		UsernamePasswordAuthenticationToken authRequest = null;

		if(ticket != null && !ticket.equals("")) {
	        authRequest = new TicketAuthenticationToken(ticket, "");
	    } else {
	        authRequest = new UserAuthenticationToken(username, password);
	    }
	    setDetails(request, authRequest);

	    return super.getAuthenticationManager().authenticate(authRequest);

	}
	
	/*
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
	    FilterChain chain) throws IOException, ServletException {
		
		final HttpServletRequest request = (HttpServletRequest) req;
	    final HttpServletResponse response = (HttpServletResponse) res;
	    if(request.getMethod().equals("POST")) {
	      // If the incoming request is a POST, then we send it up
	      // to the AbstractAuthenticationProcessingFilter.
	      super.doFilter(request, response, chain);
	    } else {
	      // If it's a GET, we ignore this request and send it
	      // to the next filter in the chain.  In this case, that
	      // pretty much means the request will hit the /login
	      // controller which will process the request to show the
	      // login page.
	      chain.doFilter(request, response);
	    }
	  }
	*/
	/*
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		String username = request.getParameter("j_username");
		String password = request.getParameter("j_password");
		
	    UsernamePasswordAuthenticationToken authRequest = null;

	    if ("ticket".equals(request.getParameter("radioAuthenticationType"))) {
	        authRequest = new TicketAuthenticationToken(username, password);
	    } else {
	        authRequest = new UserAuthenticationToken(username, password);
	    }

	    setDetails(request, authRequest);

	    return super.getAuthenticationManager().authenticate(authRequest);
	}
	*/
}
