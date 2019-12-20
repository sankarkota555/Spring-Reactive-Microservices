
package com.reactive.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableDiscoveryClient
@EnableWebFlux
public class SchedulerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulerServiceApplication.class, args);
	}
}
