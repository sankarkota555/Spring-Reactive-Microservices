package com.reactive.app.device.control.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.app.common.handler.CommonHandler;
import com.reactive.app.device.control.constants.MqttConstants;
import com.reactive.app.device.control.dto.DeviceControlDto;
import com.reactive.app.mqtt.config.MqttPublisherConfig.MqttMessagingGateway;

import reactor.core.publisher.Mono;

@Service
public class MqttDeviceControlHandler extends CommonHandler {

	private static final Logger log = LoggerFactory.getLogger(MqttDeviceControlHandler.class);

	@Autowired
	private MqttMessagingGateway mqttMessagingGateway;

	public Mono<ServerResponse> controlDevice(ServerRequest serverRequest) {
		Mono<DeviceControlDto> body = serverRequest.bodyToMono(DeviceControlDto.class);

		return ok().body(body.flatMap(deviceControlDto -> {
			return sendCommandToDeviceViaMqtt(deviceControlDto);
		}), String.class);
	}

	private Mono<String> sendCommandToDeviceViaMqtt(DeviceControlDto deviceControlDto) {

		mqttMessagingGateway.sendToMqtt(
				String.format(MqttConstants.COMMAND_TOPIC_FORMAT, deviceControlDto.getDeviceId()),
				deviceControlDto.getDeviceData());
		return Mono.just("Command sent");
	}

}