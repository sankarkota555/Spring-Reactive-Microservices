package com.reactive.app.redisson.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.redisson.RedissonNode;
import org.redisson.config.Config;
import org.redisson.config.RedissonNodeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class RedissonWorkerNodeConfig {

	private static final Logger log = LoggerFactory.getLogger(RedissonWorkerNodeConfig.class);

	@Value("classpath:${redisson.config}")
	private Resource redissonConfigFile;

	@Bean(destroyMethod = "shutdown")
	public RedissonNode test() throws IOException {

		Config config = Config.fromYAML(redissonConfigFile.getInputStream());
		RedissonNodeConfig nodeConfig = new RedissonNodeConfig(config);
		Map<String, Integer> workers = new HashMap<>();
		workers.put("executorServiceOne", 10);
		nodeConfig.setExecutorServiceWorkers(workers);

		// create Redisson node and start
		RedissonNode node = RedissonNode.create(nodeConfig);
		node.start();
		log.info("Redission worker node started with executor service workers:{}",
				nodeConfig.getExecutorServiceWorkers());
		return node;
	}

}
