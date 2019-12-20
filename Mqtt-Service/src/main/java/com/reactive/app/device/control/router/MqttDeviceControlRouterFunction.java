package com.reactive.app.device.control.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.app.common.constants.CommonRouteConstants;
import com.reactive.app.device.control.handler.MqttDeviceControlHandler;

/**
 * Router function to handle <strong> dc/controlDevice/** </strong>
 *
 */
@Configuration
public class MqttDeviceControlRouterFunction {

	@Bean
	RouterFunction<ServerResponse> azureDeviceControlRouter(MqttDeviceControlHandler mqttDeviceControlHandler) {

		return RouterFunctions.route()

				// Path -> /dc/controlDevice/**
				.path(CommonRouteConstants.CONTROL_DEVICE_ROUTE, builder -> {

					// Path -> /dc/controlDevice
					// control device through mqtt
					builder.POST("", mqttDeviceControlHandler::controlDevice);

				})

				.build();
	}
}
