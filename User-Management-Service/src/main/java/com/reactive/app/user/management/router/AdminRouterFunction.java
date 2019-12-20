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
import com.reactive.app.user.management.handler.AdminHandler;

/**
 * Admin router functions for reactive web programming
 *
 */
@Configuration
public class AdminRouterFunction {

	@Bean
	RouterFunction<ServerResponse> adminRouter(AdminHandler adminHandler) {
		return RouterFunctions.route()

				// save admin request
				.POST(CommonRouteConstants.ADMIN_ROUTE, RequestPredicates.accept(MediaType.APPLICATION_JSON),
						adminHandler::saveAdmin)

				// Nested requests for 'admin/...'
				.path(CommonRouteConstants.ADMIN_ROUTE, builder -> {

					// Get all admin list
					builder.GET("", adminHandler::getAllAdmin);

					String idPathVariable = String.format("/{%s}", PathVariables.ID);

					// Get admin by ID
					builder.GET(idPathVariable, adminHandler::getAdmin);

					// update admin request
					builder.PUT(idPathVariable, adminHandler::updateAdmin);

					// delete manufacturer request
					// builder.DELETE(idPathVariable, manufacturerHandler::updateManufacturer);

					// update a manufacture
					builder.path(idPathVariable, builderTwo -> {
						builderTwo.PUT(
								CommonRouteConstants.MANUFACTURER_ROUTE
										+ String.format("/{%s}", PathVariables.MANUFACTURER_ID),
								adminHandler::updateManufacturer);
					});

				}).build();

	}

}
