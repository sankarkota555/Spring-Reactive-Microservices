package com.reactive.app.gateway.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import com.tapas.authentication.constants.AuthenticationConstants;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class OAuth2AuthenticationManagerAdapter implements ReactiveAuthenticationManager {

	private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationManagerAdapter.class);

	private final AuthenticationManager authenticationManager;

	public OAuth2AuthenticationManagerAdapter(ResourceServerTokenServices tokenServices) {
		this.authenticationManager = oauthManager(tokenServices);
	}

	public Mono<Authentication> authenticate(Authentication token) {
		return Mono.just(token).publishOn(Schedulers.elastic()).flatMap(t -> {
			try {
				Authentication auth = this.authenticationManager.authenticate(t);
				return Mono.just(auth);
			} catch (Exception x) {
				log.error("Authentication error:", x);
				return Mono.error(new BadCredentialsException("Invalid or expired access token presented"));
			}
		}).filter(Authentication::isAuthenticated);
	}

	private AuthenticationManager oauthManager(ResourceServerTokenServices tokenServices) {
		OAuth2AuthenticationManager oauthAuthenticationManager = new OAuth2AuthenticationManager();
		oauthAuthenticationManager.setResourceId(AuthenticationConstants.RESOURCE_ID);
		oauthAuthenticationManager.setTokenServices(tokenServices);
		return oauthAuthenticationManager;
	}

}