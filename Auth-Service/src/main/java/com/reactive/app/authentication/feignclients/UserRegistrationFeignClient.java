package com.reactive.app.authentication.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.reactive.app.authentication.feignclients.fallback.UserRegistrationFeignClientFallbackFactory;
import com.reactive.app.common.constants.CommonRouteConstants;
import com.reactive.app.common.dto.RegistrationDto;

@FeignClient(name = "${service-names.user-management-service-name}", fallbackFactory = UserRegistrationFeignClientFallbackFactory.class)
public interface UserRegistrationFeignClient {

	@PostMapping(value = CommonRouteConstants.USER_ROUTE)
	public ResponseEntity<Object> registerUser(RegistrationDto registrationDto);

}