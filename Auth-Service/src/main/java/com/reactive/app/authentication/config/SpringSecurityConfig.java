package com.reactive.app.authentication.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.reactive.app.authentication.handlers.OAuth2AuthenticationSuccessHandler;
import com.reactive.app.common.constants.CommonRouteConstants;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger log = LoggerFactory.getLogger(SpringSecurityConfig.class);

	@Autowired
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();

		http.authorizeRequests().antMatchers(CommonRouteConstants.OAUTH_ROUTE + "/**",
				CommonRouteConstants.OAUTH2_SERVICE_PATTERN + "/**").permitAll();

		http.oauth2Login().authorizationEndpoint()
				// base uri: /oauth2/authorize
				.baseUri(CommonRouteConstants.OAUTH2_ROUTE + CommonRouteConstants.OAUTH2_AUTHORIZE)
				// redirect uri: /oauth2/callback/**
				.and().redirectionEndpoint().baseUri(CommonRouteConstants.OAUTH2_SERVICE_PATTERN + "/**")

				.and().successHandler(oAuth2AuthenticationSuccessHandler);

	}

	@Bean(name = "authenticationManager")
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}