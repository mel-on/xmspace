package com.ximelon.xmspace.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class MyUrlLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		response.sendRedirect(this.getDefaultTargetUrl());
		
		this.getRedirectStrategy().sendRedirect(request, response, this.getDefaultTargetUrl());
	//	super.onLogoutSuccess(request, response, authentication);
	}

}
