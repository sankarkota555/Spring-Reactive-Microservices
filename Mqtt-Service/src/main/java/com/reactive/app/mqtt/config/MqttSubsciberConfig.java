package com.reactive.app.mqtt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.corundumstudio.socketio.SocketIOServer;
import com.reactive.app.common.constants.BeanNameConstants;

@Configuration
public class MqttSubsciberConfig {

	private static final Logger log = LoggerFactory.getLogger(MqttSubsciberConfig.class);

	@Value("${mqtt.subscriber.clientName}")
	private String mqttSubscriberClientName;

	@Bean
	public MessageChannel mqttDirectInputChannel() {
		return new DirectChannel();
	}

	@Bean(name = BeanNameConstants.MQTT_SERVER_URL)
	public String mqttServerUrl(@Value("${mqtt.host}") String mqttHost, @Value("${mqtt.port}") String mqttPort) {
		return String.format("tcp://%s:%s", mqttHost, mqttPort);
	}

	@Bean
	public MessageProducer inbound() {

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(mqttServerUrl(null, null),
				mqttSubscriberClientName, "#");
		adapter.setCompletionTimeout(5000);

		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttDirectInputChannel());
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttDirectInputChannel")
	public MessageHandler handler(@Autowired SocketIOServer socketIOServer) {
		return new MessageHandler() {
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				String topicName = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
				log.info("tpic name:{}", topicName);
				log.info("topic:{} ", message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));

				socketIOServer.getBroadcastOperations().sendEvent("event", message);
			}

		};
	}

}
