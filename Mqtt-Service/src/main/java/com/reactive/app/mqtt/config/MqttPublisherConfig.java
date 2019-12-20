package com.reactive.app.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;

import com.reactive.app.common.constants.BeanNameConstants;

@Configuration
public class MqttPublisherConfig {

	@Autowired
	@Qualifier(BeanNameConstants.MQTT_SERVER_URL)
	private String mqttServerUrl;

	@Value("${mqtt.publisher.clientName}")
	private String mqttPublisherClientName;

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {

		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(new String[] { mqttServerUrl });
		// options.setUserName("username");
		// options.setPassword("password".toCharArray());
		options.setAutomaticReconnect(true);

		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(options);
		return factory;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttDirectOutboundChannel")
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(mqttPublisherClientName,
				mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultQos(1);
		return messageHandler;
	}

	@Bean
	public MessageChannel mqttDirectOutboundChannel() {
		return new DirectChannel();
	}

	@MessagingGateway(defaultRequestChannel = "mqttDirectOutboundChannel")
	public interface MqttMessagingGateway {
		void sendToMqtt(@Header(name = MqttHeaders.TOPIC) String topicName, String data);
	}

}
