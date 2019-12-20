package com.reactive.app.netty.socket.io.config;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class NettySocketIoConfig {

	private static final Logger log = LoggerFactory.getLogger(NettySocketIoConfig.class);

	@Autowired
	private ObjectMapper objectMapper;

	@Bean(initMethod = "start", destroyMethod = "stop")
	public SocketIOServer test(@Value("${netty.socket.port}") String nettySocketPort) {

		com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
		// config.setHostname("localhost");
		config.setPort(Integer.parseInt(nettySocketPort));

		final SocketIOServer server = new SocketIOServer(config);

		server.addEventListener("chatevent", Object.class, new DataListener<Object>() {

			@Override
			public void onData(SocketIOClient client, Object data, AckRequest ackRequest) {
				try {
					log.info("client:{}", objectMapper.writeValueAsString(client));
					log.info("data:{}", objectMapper.writeValueAsString(data));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// broadcast messages to all clients
				server.getBroadcastOperations().sendEvent("chatevent", data);
			}
		});

		server.addConnectListener(client -> {
			UUID clientSessionId = client.getSessionId();
			log.info("connected clientSessionId:{}", clientSessionId);
		});

		server.addDisconnectListener(client -> {
			UUID clientSessionId = client.getSessionId();
			log.info("disconnected clientSessionId:{}", clientSessionId);

		});

		return server;

	}

}
