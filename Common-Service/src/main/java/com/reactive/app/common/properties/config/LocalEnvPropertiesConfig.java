package com.reactive.app.common.properties.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Profile("local")
@Configuration
@PropertySource("classpath:common-local.properties")
public class LocalEnvPropertiesConfig {

}
