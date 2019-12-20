package com.reactive.app.authentication.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import com.reactive.app.authentication.constants.AuthenticationConstants;
import com.reactive.app.authentication.tokenstore.TapasMongoTokenStore;

@Configuration
public class OAuth2CommonConfig {

	@Primary
	@Bean
	public DefaultTokenServices tokenServices(TapasMongoTokenStore tapasMongoTokenStore) {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setTokenStore(tapasMongoTokenStore);
		tokenServices.setAccessTokenValiditySeconds(AuthenticationConstants.ACCESS_TOKEN_EXPIRY_TIME);
		tokenServices.setRefreshTokenValiditySeconds(AuthenticationConstants.REFRESH_TOKEN_EXPIRY_TIME);
		return tokenServices;
	}

}
