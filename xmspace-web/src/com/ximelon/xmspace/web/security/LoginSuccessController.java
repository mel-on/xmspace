package com.ximelon.xmspace.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

public class LoginSuccessController 
	extends org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter {

	
	private Logger logger=LoggerFactory.getLogger(UsernamePasswordAuthenticationFilter.class);

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult)
			throws IOException, ServletException {
				
	}
	
}
