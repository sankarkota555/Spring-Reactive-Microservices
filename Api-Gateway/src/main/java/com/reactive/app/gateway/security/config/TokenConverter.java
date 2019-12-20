package com.reactive.app.gateway.security.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class TokenConverter implements ServerAuthenticationConverter {

	private static final Logger log = LoggerFactory.getLogger(TokenConverter.class);

	private static final String BEARER = "bearer ";

	@Override
	public Mono<Authentication> convert(ServerWebExchange exchange) {
		log.info("apply");
		String token = extractToken(exchange.getRequest());
		if (token != null) {
			return Mono.just(new PreAuthenticatedAuthenticationToken(token, ""));
		}
		return Mono.empty();
	}

	private String extractToken(ServerHttpRequest request) {
		log.info("extractToken");
		String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (StringUtils.isBlank(token) || !token.toLowerCase().startsWith(BEARER)) {
			return null;
		}
		return token.substring(BEARER.length());
	}

}
