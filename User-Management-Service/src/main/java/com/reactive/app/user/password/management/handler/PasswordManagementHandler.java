package com.reactive.app.user.password.management.handler;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.app.common.dto.ErrorMessageDto;
import com.reactive.app.common.dto.RegistrationDto;
import com.reactive.app.common.dto.SuccessMessageDto;
import com.reactive.app.common.handler.CommonHandler;
import com.reactive.app.common.mail.service.PasswordManagementMailService;
import com.reactive.app.common.repository.LoginReactiveRepository;
import com.reactive.app.user.password.management.dto.PasswordManagementDto;

import reactor.core.publisher.Mono;

@Service
public class PasswordManagementHandler extends CommonHandler {

	@Autowired
	private LoginReactiveRepository loginReactiveRepository;

	@Autowired
	private PasswordManagementMailService passwordManagementMailService;

	public Mono<ServerResponse> forgotPassword(ServerRequest request) {

		return ok().body(request.bodyToMono(RegistrationDto.class).flatMap(registrationDto -> {
			return loginReactiveRepository.findByUsername(registrationDto.getUsername()).flatMap(loginEntity -> {
				String otp = generateOtp();
				loginEntity.setOtp(otp);
				// save otp in db
				return loginReactiveRepository.save(loginEntity).flatMap(savedEntity -> {
					String body = "You have been requested for forgot password, OTP for yor request is:" + otp;
					passwordManagementMailService.sendPasswordManagementMail(loginEntity, body);
					return Mono.just(new SuccessMessageDto("Sent OTP to mail"));
				});
			}).switchIfEmpty(Mono.error(new Exception("User not found with given username")));
		}), SuccessMessageDto.class);

	}

	public Mono<ServerResponse> changePassword(ServerRequest request) {

		return ok().body(request.bodyToMono(PasswordManagementDto.class).flatMap(passwordManagementDto -> {
			return loginReactiveRepository.findByUsername(passwordManagementDto.getUsername()).flatMap(loginEntity -> {
				// change password
				if (passwordManagementDto.getOldPassword() != null
						&& !passwordManagementDto.getOldPassword().isBlank()) {
					if (passwordManagementDto.getOldPassword().equals(loginEntity.getPassword())) {
						loginEntity.setPassword(passwordManagementDto.getNewPassword());
					} else {
						return Mono.just(new ErrorMessageDto("Passwords didn't match"));
					}
				}
				// reset password through forgot password
				else {
					if (passwordManagementDto.getOtp().equals(loginEntity.getOtp())) {
						loginEntity.setOtp(null);
						loginEntity.setPassword(passwordManagementDto.getNewPassword());
					} else {
						return Mono.just(new ErrorMessageDto("OTP didn't match"));
					}
				}
				return loginReactiveRepository.save(loginEntity).flatMap(savedEntity -> {
					String body = "You have successfully changed your password";
					passwordManagementMailService.sendPasswordManagementMail(loginEntity, body);
					return Mono.just(new SuccessMessageDto("Successfully updated password"));
				});

			}).switchIfEmpty(Mono.error(new Exception("User not found with given username")));
		}), SuccessMessageDto.class);

	}

	private String generateOtp() {
		SecureRandom random = new SecureRandom();
		int num = random.nextInt(100000);
		return String.format("%06d", num);
	}

}
