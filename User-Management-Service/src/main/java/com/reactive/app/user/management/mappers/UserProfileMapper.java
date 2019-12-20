package com.reactive.app.user.management.mappers;

import org.springframework.stereotype.Component;

import com.reactive.app.common.dto.RoleDto;
import com.reactive.app.common.dto.UserDto;
import com.reactive.app.common.model.UserEntity;

@Component
public class UserProfileMapper {

	public UserDto mapUser(UserEntity userEntity) {
		UserDto userDto = new UserDto();
		userDto.setContactDetails(userEntity.getContactDetails());
		userDto.setEnabled(userEntity.isEnabled());
		userDto.setFirstName(userEntity.getFirstName());
		userDto.setId(userEntity.getId());
		userDto.setLastName(userEntity.getLastName());
		userDto.setMiddleName(userEntity.getLastName());
		userEntity.getRoles().stream().forEach(roleEntity -> userDto.getRoles().add(new RoleDto(roleEntity.getName())));
		return userDto;
	}

}
