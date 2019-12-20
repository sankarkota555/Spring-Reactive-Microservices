package com.reactive.app.user.management.handler;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.app.common.constants.PathVariables;
import com.reactive.app.common.constants.QueryParameters;
import com.reactive.app.common.dto.ManufacturerDto;
import com.reactive.app.common.dto.RegistrationDto;
import com.reactive.app.common.dto.UserDto;
import com.reactive.app.common.handler.CommonHandler;
import com.reactive.app.common.model.LoginEntity;
import com.reactive.app.common.model.ManufacturerEntity;
import com.reactive.app.common.repository.EndUserReactiveRepository;
import com.reactive.app.common.repository.ManufacturerCategoryReactiveRepository;
import com.reactive.app.common.repository.ManufacturerReactiveRepository;
import com.reactive.app.common.user.mappers.EndUserMapper;
import com.reactive.app.common.user.mappers.ManufacturerMapper;
import com.reactive.app.user.management.service.RegistrationService;

import reactor.core.publisher.Mono;

/**
 * manufacturer handler functions for reactive web programming
 *
 */
@Service
public class ManufacturerHandler extends CommonHandler {

	private static final Logger log = LoggerFactory.getLogger(ManufacturerHandler.class);

	@Autowired
	private ManufacturerReactiveRepository manufacturerReactiveRepository;

	@Autowired
	private EndUserReactiveRepository endUserReactiveRepository;

	@Autowired
	private ManufacturerMapper manufacturerMapper;

	@Autowired
	private EndUserMapper endUserMapper;

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private ManufacturerCategoryReactiveRepository manufacturerCategoryReactiveRepository;

	public Mono<ServerResponse> saveManufacturer(ServerRequest request) {
		String loginUserId = getLoginUserId(request);
		return request.bodyToMono(RegistrationDto.class).flatMap(registrationDto -> ok()
				.body(registrationService.registerManufacturer(registrationDto, loginUserId), LoginEntity.class));
	}

	public Mono<ServerResponse> updateManufacturer(ServerRequest request) {
		String userId = request.pathVariable(PathVariables.ID);
		Mono<UserDto> userMono = request.bodyToMono(UserDto.class);
		return userMono.flatMap(user -> {
			log.info("update user id: {}", userId);
			user.setId(userId);
			Mono<ManufacturerEntity> savedUser = manufacturerReactiveRepository
					.save(manufacturerMapper.mapExistingUser(user));
			return ok().body(savedUser, ManufacturerEntity.class);
		});

	}

	public Mono<ServerResponse> getManufacturer(ServerRequest request) {
		String id = request.pathVariable(PathVariables.ID);
		return ok().body(manufacturerReactiveRepository.findById(id), ManufacturerEntity.class);
	}

	public Mono<ServerResponse> getAllManufacturers(ServerRequest request) {
		Optional<String> manufacturerType = request.queryParam(QueryParameters.MANUFACTURER_TYPE);
		if (manufacturerType.isPresent()) {
			return ok().body(manufacturerCategoryReactiveRepository.findByName(manufacturerType.get())
					.flatMapMany(manufacturerCategory -> {
						return manufacturerReactiveRepository.findByManufacturerCategory(manufacturerCategory)
								.map(manufacturerMapper::mapManufacturer);
					}), ManufacturerDto.class);
		} else {
			return badRequest().body(Mono.just("Query param:" + QueryParameters.MANUFACTURER_TYPE + " is required"),
					String.class);
		}
	}

	public Mono<ServerResponse> getManufacturerEndUsers(ServerRequest request) {
		String manufacturerId = request.pathVariable(PathVariables.ID);
		return ok().body(endUserReactiveRepository.findByManufacturerId(new ObjectId(manufacturerId))
				.map(endUserEntity -> endUserMapper.mapEndUser(endUserEntity, false)), UserDto.class);
	}

	public Mono<ServerResponse> getManufacturerCount(ServerRequest request) {
		Optional<String> manufacturerType = request.queryParam(QueryParameters.MANUFACTURER_TYPE);
		if (manufacturerType.isPresent()) {
			return ok().body(manufacturerCategoryReactiveRepository.findByName(manufacturerType.get())
					.flatMap(manufacturerCategory -> {
						return manufacturerReactiveRepository.countByManufacturerCategory(manufacturerCategory);
					}), Long.class);
		} else {
			return badRequest().body(Mono.just("Query param:" + QueryParameters.MANUFACTURER_TYPE + " is required"),
					String.class);
		}
	}
}
