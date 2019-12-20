package com.reactive.app.user.management.handler;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.app.common.dto.UserDto;
import com.reactive.app.common.handler.CommonHandler;
import com.reactive.app.common.repository.LoginReactiveRepository;
import com.reactive.app.user.management.mappers.UserProfileMapper;

import reactor.core.publisher.Mono;

/**
 * logged in user profile handler functions for reactive web programming
 *
 */
@Service
public class UserProfileHandler extends CommonHandler {

	private static final Logger log = LoggerFactory.getLogger(UserProfileHandler.class);

	@Autowired
	private LoginReactiveRepository loginReactiveRepository;

	@Autowired
	private UserProfileMapper userProfileMapper;

	public Mono<ServerResponse> getUserProfile(ServerRequest request) {
		return ok().body(loginReactiveRepository.findByUserId(new ObjectId(getLoginUserId(request)))
				.map(loginEntity -> userProfileMapper.mapUser(loginEntity.getUserEntity())), UserDto.class);
	}

}
