package com.reactive.app.common.user.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reactive.app.common.constants.RoleConstants;
import com.reactive.app.common.dto.ManufacturerDto;
import com.reactive.app.common.dto.UserDto;
import com.reactive.app.common.model.ManufacturerCategoryEntity;
import com.reactive.app.common.model.ManufacturerEntity;
import com.reactive.app.common.model.RoleEntity;
import com.reactive.app.common.repository.AdminReactiveRepository;
import com.reactive.app.common.repository.ManufacturerCategoryReactiveRepository;
import com.reactive.app.common.repository.ManufacturerReactiveRepository;
import com.reactive.app.common.repository.RoleReactiveRepository;

import reactor.core.publisher.Mono;

@Component
public class ManufacturerMapper {

	@Autowired
	private ManufacturerReactiveRepository manufacturerReactiveRepository;

	@Autowired
	private RoleReactiveRepository roleReactiveRepository;

	@Autowired
	private ManufacturerCategoryReactiveRepository manufacturerCategoryReactiveRepository;

	@Autowired
	private AdminReactiveRepository adminReactiveRepository;

	public Mono<ManufacturerEntity> mapNewManufacturer(UserDto userDto, String adminUserId) {
		ManufacturerEntity manufacturerEntity = new ManufacturerEntity();
		Mono<ManufacturerCategoryEntity> manufacturerCategoryEntity = manufacturerCategoryReactiveRepository
				.findByName(userDto.getManufacturerCategoryDto().getName());
		return manufacturerCategoryEntity.flatMap(manufacturerCategryEntity -> {
			manufacturerEntity.setManufacturerCategory(manufacturerCategryEntity);
			manufacturerEntity.setContactDetails(userDto.getContactDetails());
			manufacturerEntity.setDeleted(userDto.isDeleted());
			manufacturerEntity.setDeletedDate(userDto.getDeletedDate());
			manufacturerEntity.setFirstName(userDto.getFirstName());
			manufacturerEntity.setLastName(userDto.getLastName());
			manufacturerEntity.setMiddleName(userDto.getMiddleName());
			manufacturerEntity.setEnabled(false);
			return adminReactiveRepository.findById(adminUserId).flatMap(adminEntity -> {
				manufacturerEntity.setAdminEntity(adminEntity);
				Mono<RoleEntity> roleMono = roleReactiveRepository
						.findByName("ROLE_" + RoleConstants.MANUFACTURER_ROLE);
				return roleMono.flatMap(role -> {
					manufacturerEntity.getRoles().add(role);
					return Mono.just(manufacturerEntity);
				}).switchIfEmpty(
						// If role not found in DB throw error
						Mono.error(new Exception("Role '" + RoleConstants.MANUFACTURER_ROLE + "' not found in DB.")));
			});

		});
	}

	public ManufacturerDto mapManufacturer(ManufacturerEntity manufacturerEntity) {
		ManufacturerDto manufacturerDto = new ManufacturerDto(manufacturerEntity.getFirstName(),
				manufacturerEntity.getId());
		return manufacturerDto;
	}

	public ManufacturerEntity mapExistingUser(UserDto userDto) {
		Mono<ManufacturerEntity> userMono = manufacturerReactiveRepository.findById(userDto.getId());
		ManufacturerEntity user = userMono.toProcessor().peek();

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

}
