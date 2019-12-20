package com.reactive.app.authentication.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 
 * Security configuration
 *
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger log = LoggerFactory.getLogger(SpringSecurityConfig.class);

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.anonymous().disable();

		http.authorizeRequests().antMatchers("/**").permitAll();

		http.cors().disable();
	}

	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}