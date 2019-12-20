package com.reactive.app.authentication.feignclients.fallback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reactive.app.authentication.feignclients.AuthenticationFeignClient;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@Component
public class AuthenticationFeignClientFallbackFactory implements FallbackFactory<AuthenticationFeignClient> {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public AuthenticationFeignClient create(Throwable cause) {
		// Lambda for anonymous inner class
		return ((authorizationHeader, paramsMap) -> {
			FeignException ex = (FeignException) cause;
			try {
				return new ResponseEntity<>(objectMapper.readTree(ex.contentUTF8()), HttpStatus.resolve(ex.status()));
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		});
	}
}
