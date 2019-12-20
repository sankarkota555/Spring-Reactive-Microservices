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
import com.reactive.app.user.management.handler.ManufacturerHandler;

/**
 * Manufacturer router functions for reactive web programming
 *
 */
@Configuration
public class ManufacturerRouterFunction {

	@Bean
	RouterFunction<ServerResponse> manufacturerRouter(ManufacturerHandler manufacturerHandler) {
		return RouterFunctions.route()

				// save manufacturer request
				.POST(CommonRouteConstants.MANUFACTURER_ROUTE, RequestPredicates.accept(MediaType.APPLICATION_JSON),
						manufacturerHandler::saveManufacturer)

				// Nested requests for 'manufacturer/...'
				.path(CommonRouteConstants.MANUFACTURER_ROUTE, builder -> {

					// Get all manufacturers
					builder.GET("", manufacturerHandler::getAllManufacturers);

					builder.GET(CommonRouteConstants.COUNT_ROUTE, manufacturerHandler::getManufacturerCount);

					String idPathVariable = String.format("/{%s}", PathVariables.ID);

					// Get manufacturer by ID
					builder.GET(idPathVariable, manufacturerHandler::getManufacturer);

					// update manufacturer request
					builder.PUT(idPathVariable, manufacturerHandler::updateManufacturer);

					// delete manufacturer request
					// builder.DELETE(idPathVariable, manufacturerHandler::updateManufacturer);

					// Nested requests for 'manufacturer/{id}/...'
					builder.path(idPathVariable, builderTwo -> {
						builderTwo.GET(CommonRouteConstants.USER_ROUTE, manufacturerHandler::getManufacturerEndUsers);
					});

				}).build();

	}

}
