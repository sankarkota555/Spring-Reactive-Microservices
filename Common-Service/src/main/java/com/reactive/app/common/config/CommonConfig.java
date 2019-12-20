package com.reactive.app.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import com.reactive.app.common.constants.BeanNameConstants;
import com.reactive.app.common.repository.EnvironmentDetailsRepository;

@Configuration
@PropertySource("classpath:common.properties")
@EnableReactiveMongoRepositories(basePackages = "com.reactive")
@EnableMongoRepositories(basePackages = "com.reactive")
@EnableAsync
@EnableTransactionManagement
@EnableMongoAuditing
public class CommonConfig {

	@Bean
	@Primary
	public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
		FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
		bean.setTemplateLoaderPath("classpath:mailTemplates/");
		return bean;
	}

	/**
	 * Transaction manager for reactive mongo
	 */
	@Bean(name = BeanNameConstants.MONGO_REATIVE_TRANSACTION_MANAGER)
	public ReactiveMongoTransactionManager reactiveMongoTransactionManager(ReactiveMongoDatabaseFactory factory) {
		return new ReactiveMongoTransactionManager(factory);
	}

	/**
	 * Transaction manager for mongo
	 */

	@Bean
	@Primary
	public MongoTransactionManager mongoTransactionManager(MongoDbFactory dbFactory) {
		return new MongoTransactionManager(dbFactory);
	}

	@Bean("applicationHost")
	public String applicationHost(@Value("${spring.profiles.active}") String activeEnvironment,
			EnvironmentDetailsRepository environmentDetailsRepository) {

		return environmentDetailsRepository.findByEnvironmentName(activeEnvironment).getApplicationUrl();
	}

}
