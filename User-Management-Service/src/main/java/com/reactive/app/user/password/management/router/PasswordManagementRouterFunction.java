package com.reactive.app.user.password.management.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.app.common.constants.CommonRouteConstants;
import com.reactive.app.user.password.management.handler.PasswordManagementHandler;

/**
 * Password management router functions for reactive web programming
 *
 */
@Configuration
public class PasswordManagementRouterFunction {

	@Bean
	RouterFunction<ServerResponse> passwordManagementRouter(PasswordManagementHandler passwordManagementHandler) {
		return RouterFunctions.route()

				// forgot password
				.POST(CommonRouteConstants.FORGOT_PASSWORD_ROUTE, passwordManagementHandler::forgotPassword)

				// change password
				.POST(CommonRouteConstants.CHANGE_PASSWORD_ROUTE, passwordManagementHandler::changePassword)

				.build();

	}

}
