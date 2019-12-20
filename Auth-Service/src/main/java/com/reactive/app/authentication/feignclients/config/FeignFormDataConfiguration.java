package com.reactive.app.authentication.feignclients.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

/**
 * Feign configuration for form data requests
 *
 */
public class FeignFormDataConfiguration {

	@Bean
	@Scope("prototype")
	public Encoder feignFormEncoder(@Autowired ObjectFactory<HttpMessageConverters> messageConverters) {
		return new SpringFormEncoder(new SpringEncoder(messageConverters));
	}
}