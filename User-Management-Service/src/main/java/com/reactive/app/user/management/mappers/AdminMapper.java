package com.reactive.app.user.management.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reactive.app.common.constants.RoleConstants;
import com.reactive.app.common.dto.UserDto;
import com.reactive.app.common.model.AdminEntity;
import com.reactive.app.common.model.RoleEntity;
import com.reactive.app.common.repository.AdminReactiveRepository;
import com.reactive.app.common.repository.RoleReactiveRepository;

import reactor.core.publisher.Mono;

@Component
public class AdminMapper {

	@Autowired
	private AdminReactiveRepository adminReactiveRepository;

	@Autowired
	private RoleReactiveRepository roleReactiveRepository;

	public Mono<AdminEntity> mapNewAdmin(UserDto userDto) {
		AdminEntity adminEntity = new AdminEntity();
		adminEntity.setContactDetails(userDto.getContactDetails());
		adminEntity.setFirstName(userDto.getFirstName());
		adminEntity.setLastName(userDto.getLastName());
		adminEntity.setMiddleName(userDto.getMiddleName());
		adminEntity.setEnabled(false);
		Mono<RoleEntity> roleMono = roleReactiveRepository.findByName("ROLE_" + RoleConstants.ADMIN_ROLE);
		return roleMono.flatMap(role -> {
			adminEntity.getRoles().add(role);
			return Mono.just(adminEntity);
		}).switchIfEmpty(
				// If role not found in DB throw error
				Mono.error(new Exception("Role '" + RoleConstants.ADMIN_ROLE + "' not found in DB.")));
	}

	public AdminEntity mapExistingAdmin(UserDto userDto) {
		Mono<AdminEntity> adminMono = adminReactiveRepository.findById(userDto.getId());
		AdminEntity adminEntity = adminMono.toProcessor().peek();
		if (!userDto.getContactDetails().isEmpty())
			adminEntity.setContactDetails(userDto.getContactDetails());

		if (userDto.getFirstName() != null)
			adminEntity.setFirstName(userDto.getFirstName());

		if (userDto.getLastName() != null)
			adminEntity.setLastName(userDto.getLastName());

		if (userDto.getMiddleName() != null)
			adminEntity.setMiddleName(userDto.getMiddleName());

		return adminEntity;
	}

}
