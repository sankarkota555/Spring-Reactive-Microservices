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
import com.reactive.app.common.model.AdminEntity;
import com.reactive.app.common.model.LoginEntity;
import com.reactive.app.common.model.ManufacturerEntity;
import com.reactive.app.common.repository.AdminReactiveRepository;
import com.reactive.app.common.repository.ManufacturerReactiveRepository;
import com.reactive.app.user.management.mappers.AdminMapper;
import com.reactive.app.user.management.service.RegistrationService;

import reactor.core.publisher.Mono;

/**
 * Admin handler functions for reactive web programming
 *
 */
@Service

public class AdminHandler extends CommonHandler {

	private static final Logger log = LoggerFactory.getLogger(AdminHandler.class);

	@Autowired
	private AdminReactiveRepository adminReactiveRepository;

	@Autowired
	private ManufacturerReactiveRepository manufacturerReactiveRepository;

	@Autowired
	private AdminMapper adminMapper;

	@Autowired
	private RegistrationService registrationService;

	public Mono<ServerResponse> saveAdmin(ServerRequest request) {

		return request.bodyToMono(RegistrationDto.class).flatMap(
				registrationDto -> ok().body(registrationService.registerAdmin(registrationDto), LoginEntity.class));
	}

	public Mono<ServerResponse> updateAdmin(ServerRequest request) {
		String userId = request.pathVariable(PathVariables.ID);
		Mono<UserDto> adminMono = request.bodyToMono(UserDto.class);
		return adminMono.flatMap(admin -> {
			admin.setId(userId);
			Mono<AdminEntity> savedUser = adminReactiveRepository.save(adminMapper.mapExistingAdmin(admin));
			return ok().body(savedUser, AdminEntity.class);
		});

	}

	public Mono<ServerResponse> getAdmin(ServerRequest request) {
		String id = request.pathVariable(PathVariables.ID);
		return ok().body(adminReactiveRepository.findById(id), AdminEntity.class);
	}

	public Mono<ServerResponse> getAllAdmin(ServerRequest request) {
		return ok().body(adminReactiveRepository.findAll(), AdminEntity.class);
	}

	public Mono<ServerResponse> updateManufacturer(ServerRequest request) {
		String manufacturerId = request.pathVariable(PathVariables.MANUFACTURER_ID);
		return ok().body(request.bodyToMono(UserDto.class).flatMap(
				userDto -> manufacturerReactiveRepository.findById(manufacturerId).flatMap(manufacturerEntity -> {
					manufacturerEntity.setEnabled(userDto.isEnabled());
					return manufacturerReactiveRepository.save(manufacturerEntity);
				})), ManufacturerEntity.class);
	}

}
