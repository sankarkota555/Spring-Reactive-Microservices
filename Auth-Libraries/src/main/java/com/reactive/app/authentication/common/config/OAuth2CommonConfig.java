package com.reactive.app.authentication.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import com.reactive.app.authentication.constants.AuthenticationConstants;
import com.reactive.app.authentication.tokenstore.CustomMongoTokenStore;

@Configuration
public class OAuth2CommonConfig {

	@Primary
	@Bean
	public DefaultTokenServices tokenServices(CustomMongoTokenStore mongoTokenStore) {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setTokenStore(mongoTokenStore);
		tokenServices.setAccessTokenValiditySeconds(AuthenticationConstants.ACCESS_TOKEN_EXPIRY_TIME);
		tokenServices.setRefreshTokenValiditySeconds(AuthenticationConstants.REFRESH_TOKEN_EXPIRY_TIME);
		return tokenServices;
	}

}
