package com.reactive.app.user.management.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.app.common.constants.PathVariables;
import com.reactive.app.common.handler.CommonHandler;
import com.reactive.app.common.repository.LoginReactiveRepository;

import reactor.core.publisher.Mono;

/**
 * Admin handler functions for reactive web programming
 *
 */
@Service
public class UserVerificationHandler extends CommonHandler {

	private static final Logger log = LoggerFactory.getLogger(UserVerificationHandler.class);

	@Autowired
	private LoginReactiveRepository loginReactiveRepository;

	@Value("classpath:/views/userVerificationSuccess.html")
	private Resource successResponse;

	@Value("classpath:/views/userVerificationFailed.html")
	private Resource failResponse;

	public Mono<ServerResponse> verifyEmail(ServerRequest request) {

		String loginId = request.pathVariable(PathVariables.ID);
		String verificationKey = request.pathVariable(PathVariables.MAIL_VERIFICATION_KEY);
		return loginReactiveRepository.findById(loginId).flatMap(loginEntity -> {
			if (loginEntity.getVerificationKey().equals(verificationKey)) {
				loginEntity.setVerified(true);
				loginReactiveRepository.save(loginEntity);
				return ok().contentType(MediaType.TEXT_HTML).bodyValue(successResponse);
			} else {
				return ok().contentType(MediaType.TEXT_HTML).bodyValue(failResponse);
			}
		});
	}

}
