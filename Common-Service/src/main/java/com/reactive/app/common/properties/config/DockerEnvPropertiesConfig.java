package com.reactive.app.common.properties.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Profile("docker")
@Configuration
@PropertySource("classpath:common-docker.properties")
public class DockerEnvPropertiesConfig {

}