package com.fingerbook.rest.back;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationProcessingFilterEntryPoint;

public class AuthenticationEntryPointImpl extends AuthenticationProcessingFilterEntryPoint  {

	@Override
	public void commence(HttpServletRequest arg0, HttpServletResponse arg1,
			AuthenticationException arg2) throws IOException, ServletException {
		// TODO Auto-generated method stub

		super.commence(arg0, arg1, arg2);
	}

}
