package com.reactive.app.device.control.scheduler.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.app.common.constants.CommonRouteConstants;
import com.reactive.app.device.control.scheduler.handler.ScheduledDeviceControlHandler;

/**
 * 
 * Router function to handle <strong> dc/scheduled/** </strong>
 *
 */
@Configuration
public class ScheduledDeviceControlRouterFunction {

	@Bean
	RouterFunction<ServerResponse> scheduledDeviceControlRouter(
			ScheduledDeviceControlHandler scheduledDeviceControlHandler) {

		return RouterFunctions.route()

				// schedule device control
				.POST(CommonRouteConstants.SCHEDULED_ROUTE + CommonRouteConstants.CONTROL_DEVICE_ROUTE,
						scheduledDeviceControlHandler::scheduleDeviceControl)

				.build();
	}

}
