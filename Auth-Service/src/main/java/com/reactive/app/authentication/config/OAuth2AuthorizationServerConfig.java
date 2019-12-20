package com.reactive.app.authentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import com.reactive.app.authentication.constants.AuthenticationConstants;
import com.reactive.app.authentication.service.CustomUserDetailsService;
import com.reactive.app.authentication.service.TapasMongoAuthorizationCodeServices;

/**
 * Spring OAuth2 authorization server configuration
 *
 */
@Configuration
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TapasMongoAuthorizationCodeServices mongoAuthorizationCodeServices;

	@Autowired
	private DefaultTokenServices authorizationServerTokenServices;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient(AuthenticationConstants.TAPAS_CLIENT_ID)
				.secret(AuthenticationConstants.TAPAS_CLIENT_SECRET)
				.authorizedGrantTypes("password", "authorization_code", "refresh_token").authorities("ADMIN")
				.scopes("all").resourceIds(AuthenticationConstants.RESOURCE_ID)
				.accessTokenValiditySeconds(AuthenticationConstants.ACCESS_TOKEN_EXPIRY_TIME)
				.refreshTokenValiditySeconds(AuthenticationConstants.REFRESH_TOKEN_EXPIRY_TIME);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		endpoints.authorizationCodeServices(mongoAuthorizationCodeServices)
				.tokenServices(authorizationServerTokenServices).authenticationManager(authenticationManager)
				.userDetailsService(customUserDetailsService);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()");
		oauthServer.allowFormAuthenticationForClients();
	}

}
