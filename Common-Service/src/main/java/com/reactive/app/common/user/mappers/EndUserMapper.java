package com.reactive.app.common.user.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reactive.app.common.constants.RoleConstants;
import com.reactive.app.common.dto.ManufacturerDto;
import com.reactive.app.common.dto.UserDto;
import com.reactive.app.common.model.EndUserEntity;
import com.reactive.app.common.model.ManufacturerEntity;
import com.reactive.app.common.model.RoleEntity;
import com.reactive.app.common.repository.EndUserReactiveRepository;
import com.reactive.app.common.repository.RoleReactiveRepository;

import reactor.core.publisher.Mono;

@Component
public class EndUserMapper {

	@Autowired
	private EndUserReactiveRepository endUserReactiveRepository;

	@Autowired
	private RoleReactiveRepository roleReactiveRepository;

	public Mono<EndUserEntity> mapNewUser(UserDto userDto) {
		EndUserEntity user = new EndUserEntity();
		user.setContactDetails(userDto.getContactDetails());
		user.setDeleted(userDto.isDeleted());
		user.setDeletedDate(userDto.getDeletedDate());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setMiddleName(userDto.getMiddleName());
		Mono<RoleEntity> roleMono = roleReactiveRepository.findByName("ROLE_" + RoleConstants.USER_ROLE);
		return roleMono.flatMap(role -> {
			user.getRoles().add(role);
			return Mono.just(user);
		}).switchIfEmpty(Mono.error(new Exception("Role " + RoleConstants.USER_ROLE + "not found in DB")));
	}

	public EndUserEntity mapExistingUser(UserDto userDto) {
		Mono<EndUserEntity> userMono = endUserReactiveRepository.findById(userDto.getId());
		EndUserEntity user = userMono.toProcessor().peek();
		if (!userDto.getContactDetails().isEmpty())
			user.setContactDetails(userDto.getContactDetails());
		user.setDeleted(userDto.isDeleted());
		if (userDto.getDeletedDate() != null)
			user.setDeletedDate(userDto.getDeletedDate());

		if (userDto.getFirstName() != null)
			user.setFirstName(userDto.getFirstName());

		if (userDto.getLastName() != null)
			user.setLastName(userDto.getLastName());

		if (userDto.getMiddleName() != null)
			user.setMiddleName(userDto.getMiddleName());

		return user;
	}

	public UserDto mapEndUser(EndUserEntity endUserEntity, boolean includeManufacturer) {
		UserDto user = new UserDto();
		if (endUserEntity != null) {
			user.setId(endUserEntity.getId());
			user.setContactDetails(endUserEntity.getContactDetails());
			user.setDeleted(endUserEntity.isDeleted());
			user.setDeletedDate(endUserEntity.getDeletedDate());
			user.setFirstName(endUserEntity.getFirstName());
			user.setLastName(endUserEntity.getLastName());
			user.setMiddleName(endUserEntity.getMiddleName());
			user.setEnabled(endUserEntity.isEnabled());
			if (includeManufacturer) {
				ManufacturerEntity manufacturer = endUserEntity.getManufacturer();
				user.setManufacturerDto(new ManufacturerDto(manufacturer.getFirstName(), manufacturer.getId()));
			}
		}
		return user;
	}

}
