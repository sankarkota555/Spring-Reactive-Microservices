package com.reactive.app.redisson.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

@Configuration
public class RedissonConfiguration {

	private static final Logger log = LoggerFactory.getLogger(RedissonConfiguration.class);

	@Autowired
	private RedisProperties redisProperties;

	@Autowired
	private RedissonProperties redissonProperties;

	@Autowired
	private ApplicationContext ctx;

	private static final String REDIS_HTTP_PREFIX = "redis://";

	private static final String REDIS_HTTPS_PREFIX = "rediss://";

	@Bean
	public Config redissonConfig() {

		Config config;

		Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
		Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, redisProperties);
		Method clusterMethod = ReflectionUtils.findMethod(RedisProperties.class, "getCluster");
		int timeout;
		if (null == timeoutValue) {
			timeout = 10000;
		} else if (!(timeoutValue instanceof Integer)) {
			Method millisMethod = ReflectionUtils.findMethod(timeoutValue.getClass(), "toMillis");
			timeout = ((Long) ReflectionUtils.invokeMethod(millisMethod, timeoutValue)).intValue();
		} else {
			timeout = (Integer) timeoutValue;
		}

		if (redissonProperties.getConfig() != null) {
			log.info("Redisson config from configuration file");

			try {
				InputStream is = getConfigStream();
				config = Config.fromJSON(is);
			} catch (IOException e) {
				// trying next format
				try {
					InputStream is = getConfigStream();
					config = Config.fromYAML(is);
				} catch (IOException e1) {
					throw new IllegalArgumentException("Can't parse config", e1);
				}
			}
		} else if (clusterMethod != null && ReflectionUtils.invokeMethod(clusterMethod, redisProperties) != null) {
			log.info("Redisson config in cluster mode");
			Object clusterObject = ReflectionUtils.invokeMethod(clusterMethod, redisProperties);
			Method nodesMethod = ReflectionUtils.findMethod(clusterObject.getClass(), "getNodes");
			List<String> nodesObject = (List) ReflectionUtils.invokeMethod(nodesMethod, clusterObject);

			String[] nodes = convert(nodesObject);

			config = new Config();
			config.useClusterServers().addNodeAddress(nodes).setConnectTimeout(timeout)
					.setPassword(redisProperties.getPassword());
		} else {
			log.info("Redisson config in single server mode");
			String prefix = REDIS_HTTP_PREFIX;

			Method method = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
			if (method != null && (Boolean) ReflectionUtils.invokeMethod(method, redisProperties)) {
				prefix = REDIS_HTTPS_PREFIX;
			}

			config = new Config();
			config.useSingleServer().setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
					.setConnectTimeout(timeout).setDatabase(redisProperties.getDatabase())
					.setPassword(redisProperties.getPassword());

		}

		return config;

	}

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean(RedissonClient.class)
	public RedissonClient redissonClient() {
		return Redisson.create(redissonConfig());
	}

	private String[] convert(List<String> nodesObject) {
		List<String> nodes = new ArrayList<>(nodesObject.size());
		for (String node : nodesObject) {
			if (!node.startsWith(REDIS_HTTP_PREFIX) && !node.startsWith(REDIS_HTTPS_PREFIX)) {
				nodes.add(REDIS_HTTP_PREFIX + node);
			} else {
				nodes.add(node);
			}
		}
		return nodes.toArray(new String[nodes.size()]);
	}

	private InputStream getConfigStream() throws IOException {
		return ctx.getResource(redissonProperties.getConfig()).getInputStream();
	}

}
