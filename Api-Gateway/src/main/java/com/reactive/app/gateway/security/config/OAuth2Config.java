package com.reactive.app.gateway.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
public class OAuth2Config {

	@Bean
	OAuth2AuthenticationManagerAdapter oAuth2AuthenticationManagerAdapter(
			@Autowired DefaultTokenServices tokenServices) {
		return new OAuth2AuthenticationManagerAdapter(tokenServices);
	}

	@Bean("oauthAuthenticationWebFilter")
	AuthenticationWebFilter oauthAuthenticationWebFilter(OAuth2AuthenticationManagerAdapter authManager,
			TokenConverter tokenConverter) {

		AuthenticationWebFilter filter = new AuthenticationWebFilter(authManager);
		filter.setServerAuthenticationConverter(tokenConverter);
		return filter;
	}

}