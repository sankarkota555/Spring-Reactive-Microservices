package com.reactive.app.user.management.mappers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.reactive.app.common.constants.ContactType;
import com.reactive.app.common.dto.RegistrationDto;
import com.reactive.app.common.model.LoginEntity;
import com.reactive.app.common.model.UserEntity;

@Component
public class RegistrationMapper {

	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	public LoginEntity mapNewLogin(RegistrationDto registrationDto, UserEntity userEntity) {
		LoginEntity loginEntity = new LoginEntity();
		loginEntity.setUsername(registrationDto.getUsername());
		loginEntity.setPassword(registrationDto.getPassword());
		loginEntity.setUserEntity(userEntity);
		// set verification key to login
		registrationDto.getUserDto().getContactDetails().stream()
				.filter(contactDetail -> contactDetail.getType().equals(ContactType.EMAILID)).findAny()
				.ifPresent(emailContact -> loginEntity.setVerificationKey(
						bCryptPasswordEncoder.encode(emailContact.getValue()).replaceAll("/", "-")));
		return loginEntity;

	}

}
