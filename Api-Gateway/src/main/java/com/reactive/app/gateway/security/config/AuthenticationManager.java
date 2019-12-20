package com.reactive.app.gateway.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

/**
 * This bean is not for real authentication purpose, just to fix application
 * start-up error AuthenticationManager bean to fix application start-up error.
 *
 */
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationManager.class);

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		log.error("This authentication should not called, if called take proper action");
		return Mono.just(new UsernamePasswordAuthenticationToken("user", null, null));
	}
}