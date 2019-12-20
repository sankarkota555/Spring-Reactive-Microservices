package com.reactive.app.authentication.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.reactive.app.common.constants.CommonRouteConstants;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		// Set redirect url
		getRedirectStrategy().sendRedirect(request, response,
				CommonRouteConstants.AUTH_SERVICE_PATTERN + CommonRouteConstants.OAUTH2_LOGIN_SUCCESS_ROUTE);

		super.onAuthenticationSuccess(request, response, authentication);
	}

}