package com.reactive.app.user.management.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.app.common.constants.CommonRouteConstants;
import com.reactive.app.common.constants.PathVariables;
import com.reactive.app.user.management.handler.EndUserHandler;

/**
 * End user router functions for reactive web programming
 *
 */
@Configuration
public class EndUserRouterFunction {

	@Bean
	RouterFunction<ServerResponse> userRouter(EndUserHandler endUserHandler) {
		return RouterFunctions.route()

				// save end user request
				.POST(CommonRouteConstants.USER_ROUTE, RequestPredicates.accept(MediaType.APPLICATION_JSON),
						endUserHandler::saveUser)

				// Nested requests for user/...
				.path(CommonRouteConstants.USER_ROUTE, builder -> {

					// Get all end users
					// builder.GET("", userHandler::getUser);

					String idPathVariable = String.format("/{%s}", PathVariables.ID);

					// Get end user by ID
					builder.GET(idPathVariable, endUserHandler::getUser);

					// update end user request
					builder.PUT(idPathVariable, endUserHandler::updateUser);

					// delete end user request
					// builder.DELETE(idPathVariable, userHandler::updateUser);

				}).build();

	}

}
