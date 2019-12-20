package com.reactive.app.gateway.config;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.DedupeResponseHeaderGatewayFilterFactory.Strategy;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMethod;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.reactive.app.authentication.user.CustomSpringSecurityUser;
import com.reactive.app.common.constants.CommonRouteConstants;
import com.reactive.app.common.constants.HeaderConstants;

import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

	private static final Logger log = LoggerFactory.getLogger(GatewayConfig.class);

	@Bean
	public GlobalFilter globalFilter() {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			// Don't process 'OPTIONS' method, it is a pre-flight request comes from browser
			if (request.getMethod().name().equals(RequestMethod.OPTIONS.name())) {
				log.info("Skipping OPTIONS call");
				return Mono.empty();
			}

			return ReactiveSecurityContextHolder.getContext().flatMap(t -> {

				Object principal = t.getAuthentication().getPrincipal();

				if (principal instanceof CustomSpringSecurityUser) {

					CustomSpringSecurityUser user = (CustomSpringSecurityUser) principal;
					request.mutate().headers(headers -> {
						headers.add(HeaderConstants.LOGIN_USER_ID, user.getId());

						// TODO: if logged in user role is not needed, then remove below code
						headers.add(HeaderConstants.LOGIN_USER_ROLE,
								user.getAuthorities().toArray()[0].toString());
					});

				}

				return chain.filter(exchange.mutate().request(request).build());

			}).switchIfEmpty(chain.filter(exchange.mutate().request(request).build()));

		};
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder, @Autowired EurekaClient eurekaClient,
			@Value("${service-names.device-contol-service-name}") String deviceControlServiceName,
			@Value("${netty.socket.port}") String nettySocketPort) {

		return builder.routes()
				.route("Socket-Route", r -> r.path(CommonRouteConstants.SOCKET_IO_ROUTE).filters(filterSpec -> {

					filterSpec.changeRequestUri(serverWebExchange -> {

						// Remove duplicate headers with Strategy
						filterSpec.dedupeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
								Strategy.RETAIN_LAST.name());

						// manipulate request uri with socket uri
						InstanceInfo deviceControlInstance = eurekaClient.getApplication(deviceControlServiceName)
								.getInstances().get(0);
						URI currentUri = serverWebExchange.getRequest().getURI();
						String socketUrl = currentUri.toASCIIString().replace(currentUri.getHost(),
								deviceControlInstance.getHostName());
						socketUrl = socketUrl.replace(String.valueOf(currentUri.getPort()), nettySocketPort);
						System.out.println("newUrl 2:" + socketUrl);
						return Optional.of(URI.create(socketUrl));
					});
					return filterSpec;
				}).uri("lb://" + deviceControlServiceName)).build();
	}

}
