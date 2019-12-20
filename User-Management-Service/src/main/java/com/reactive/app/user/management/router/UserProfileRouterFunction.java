package com.reactive.app.user.management.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.app.common.constants.CommonRouteConstants;
import com.reactive.app.user.management.handler.UserProfileHandler;

/**
 * Logged in user profile router functions for reactive web programming
 *
 */
@Configuration
public class UserProfileRouterFunction {

	@Bean
	RouterFunction<ServerResponse> userProfileRouter(UserProfileHandler userProfileHandler) {
		return RouterFunctions.route()
				// get user profile
				.GET(CommonRouteConstants.USER_PROFILE_ROUTE, userProfileHandler::getUserProfile).build();

	}

}
