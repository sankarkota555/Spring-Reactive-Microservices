package com.reactive.app.user.management.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.app.common.constants.PathVariables;
import com.reactive.app.common.dto.RegistrationDto;
import com.reactive.app.common.dto.UserDto;
import com.reactive.app.common.handler.CommonHandler;
import com.reactive.app.common.model.EndUserEntity;
import com.reactive.app.common.model.LoginEntity;
import com.reactive.app.common.repository.EndUserReactiveRepository;
import com.reactive.app.common.user.mappers.EndUserMapper;
import com.reactive.app.user.management.service.RegistrationService;

import reactor.core.publisher.Mono;

/**
 * End user handler functions for reactive web programming
 *
 */
@Service
public class EndUserHandler extends CommonHandler {

	private static final Logger log = LoggerFactory.getLogger(EndUserHandler.class);

	@Autowired
	private EndUserReactiveRepository endUserRepository;

	@Autowired
	private EndUserMapper endUserMapper;

	@Autowired
	private RegistrationService registrationService;

	public Mono<ServerResponse> saveUser(ServerRequest request) {

		return request.bodyToMono(RegistrationDto.class).flatMap(
				registrationDto -> ok().body(registrationService.registerEndUser(registrationDto), LoginEntity.class));
	}

	public Mono<ServerResponse> updateUser(ServerRequest request) {
		String userId = request.pathVariable(PathVariables.ID);
		Mono<UserDto> userMono = request.bodyToMono(UserDto.class);
		return userMono.flatMap(user -> {
			log.info("update user id: {}", userId);
			user.setId(userId);
			Mono<EndUserEntity> savedUser = endUserRepository.save(endUserMapper.mapExistingUser(user));
			return ok().body(savedUser, EndUserEntity.class);
		});

	}

	public Mono<ServerResponse> getUser(ServerRequest request) {
		String userId = request.pathVariable(PathVariables.ID);
		return ok().body(endUserRepository.findById(userId), EndUserEntity.class);
	}
}
