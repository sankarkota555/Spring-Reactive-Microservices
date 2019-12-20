package com.reactive.app.user.management.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.app.common.constants.CommonRouteConstants;
import com.reactive.app.common.constants.PathVariables;
import com.reactive.app.user.management.handler.UserVerificationHandler;

/**
 * user verification router functions for reactive web programming
 *
 */
@Configuration
public class UserVerificationRouterFunction {

	@Bean
	RouterFunction<ServerResponse> userVerificationRouter(UserVerificationHandler userVerificationHandler) {
		return RouterFunctions.route()

				// Nested requests for 'userVerification/...'
				.path(CommonRouteConstants.USER_VERIFICATION_ROUTE, builder -> {

					// verify email
					builder.GET(
							CommonRouteConstants.VERIFY_EMAIL_ROUTE + String.format("/{%s}", PathVariables.ID)
									+ String.format("/{%s}", PathVariables.MAIL_VERIFICATION_KEY),
							userVerificationHandler::verifyEmail);

				}).build();

	}

}
