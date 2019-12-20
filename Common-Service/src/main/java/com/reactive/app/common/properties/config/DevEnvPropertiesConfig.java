package com.reactive.app.common.properties.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Profile("dev")
@Configuration
@PropertySource("classpath:common-dev.properties")
public class DevEnvPropertiesConfig {

}