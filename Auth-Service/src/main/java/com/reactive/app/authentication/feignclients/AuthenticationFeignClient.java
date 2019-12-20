package com.reactive.app.authentication.feignclients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.reactive.app.authentication.feignclients.config.FeignFormDataConfiguration;
import com.reactive.app.authentication.feignclients.fallback.AuthenticationFeignClientFallbackFactory;
import com.reactive.app.common.constants.CommonRouteConstants;

@FeignClient(name = "${service-names.authentication-service-name}", fallbackFactory = AuthenticationFeignClientFallbackFactory.class, configuration = {
		FeignFormDataConfiguration.class })
public interface AuthenticationFeignClient {

	@PostMapping(value = CommonRouteConstants.OAUTH_ROUTE
			+ "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<Object> getToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
			Map<String, ?> paramsMap);

}