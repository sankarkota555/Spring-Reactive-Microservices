package com.reactive.app.user.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reactive.app.common.constants.BeanNameConstants;
import com.reactive.app.common.dto.RegistrationDto;
import com.reactive.app.common.model.LoginEntity;
import com.reactive.app.common.repository.AdminReactiveRepository;
import com.reactive.app.common.repository.EndUserReactiveRepository;
import com.reactive.app.common.repository.LoginReactiveRepository;
import com.reactive.app.common.repository.ManufacturerReactiveRepository;
import com.reactive.app.common.user.mappers.EndUserMapper;
import com.reactive.app.common.user.mappers.ManufacturerMapper;
import com.reactive.app.user.management.mappers.AdminMapper;
import com.reactive.app.user.management.mappers.RegistrationMapper;
import com.reactive.app.user.management.registration.mail.service.RegistrationMailService;

import reactor.core.publisher.Mono;

@Service
public class RegistrationService {

	@Autowired
	private AdminReactiveRepository adminReactiveRepository;

	@Autowired
	private LoginReactiveRepository loginReactiveRepository;

	@Autowired
	private EndUserReactiveRepository endUserReactiveRepository;

	@Autowired
	private ManufacturerReactiveRepository manufacturerReactiveRepository;

	@Autowired
	private AdminMapper adminMapper;

	@Autowired
	private RegistrationMapper registrationMapper;

	@Autowired
	private EndUserMapper endUserMapper;

	@Autowired
	private ManufacturerMapper manufacturerMapper;

	@Autowired
	private RegistrationMailService registrationMailService;

	@Transactional(transactionManager = BeanNameConstants.MONGO_REATIVE_TRANSACTION_MANAGER)
	public Mono<LoginEntity> registerAdmin(RegistrationDto registrationDto) {
		// save objects inside a transaction
		return adminMapper.mapNewAdmin(registrationDto.getUserDto()).flatMap(adminEntity -> {
			// save objects inside a transaction return
			return adminReactiveRepository.save(adminEntity).flatMap(savedAdmin -> {
				return saveLoginAndSendRegistrationMail(registrationMapper.mapNewLogin(registrationDto, savedAdmin));
			});
		});
	}

	@Transactional(transactionManager = BeanNameConstants.MONGO_REATIVE_TRANSACTION_MANAGER)
	public Mono<LoginEntity> registerEndUser(RegistrationDto registrationDto) {
		// save objects inside a transaction
		return endUserMapper.mapNewUser(registrationDto.getUserDto()).flatMap(userEntity -> {
			return endUserReactiveRepository.save(userEntity).flatMap(savedUser -> {
				// Save login entity
				return saveLoginAndSendRegistrationMail(registrationMapper.mapNewLogin(registrationDto, savedUser));
			});
		});
	}

	@Transactional(transactionManager = BeanNameConstants.MONGO_REATIVE_TRANSACTION_MANAGER)
	public Mono<LoginEntity> registerManufacturer(RegistrationDto registrationDto, String adminUserId) {
		// save objects inside a transaction
		return manufacturerMapper.mapNewManufacturer(registrationDto.getUserDto(), adminUserId)
				.flatMap(manufacturerEntity -> {
					return manufacturerReactiveRepository.save(manufacturerEntity).flatMap(savedManufacturer -> {
						// Save login entity
						return saveLoginAndSendRegistrationMail(
								registrationMapper.mapNewLogin(registrationDto, savedManufacturer));
					});

				});
	}

	private Mono<LoginEntity> saveLoginAndSendRegistrationMail(LoginEntity loginEntity) {
		return loginReactiveRepository.save(loginEntity).flatMap(savedLoginEntity -> {
			registrationMailService.sendRegistrationSuccessMail(savedLoginEntity);
			return Mono.just(savedLoginEntity);
		});
	}
}
