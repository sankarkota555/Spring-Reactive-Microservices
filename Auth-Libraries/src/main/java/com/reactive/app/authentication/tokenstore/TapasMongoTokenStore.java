package com.reactive.app.authentication.tokenstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import com.reactive.app.authentication.model.OAuth2AccessTokenEntity;
import com.reactive.app.authentication.model.OAuth2RefreshTokenEntity;
import com.reactive.app.authentication.repository.OAuth2AccessTokenRepository;
import com.reactive.app.authentication.repository.OAuth2RefreshTokenRepository;

@Component
public class TapasMongoTokenStore implements TokenStore {

	private static final Logger log = LoggerFactory.getLogger(TapasMongoTokenStore.class);

	@Autowired
	private OAuth2AccessTokenRepository oAuth2AccessTokenRepository;

	@Autowired
	private OAuth2RefreshTokenRepository oAuth2RefreshTokenRepository;

	private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

	@Override
	public OAuth2Authentication readAuthentication(OAuth2AccessToken accessToken) {
		return readAuthentication(accessToken.getValue());
	}

	@Override
	public OAuth2Authentication readAuthentication(String token) {

		Optional<OAuth2AccessTokenEntity> mongoAccessToken = oAuth2AccessTokenRepository
				.findByTokenId(extractTokenKey(token));
		if (mongoAccessToken.isPresent()) {
			return mongoAccessToken.get().getAuthentication();
		}
		return null;
	}

	@Override
	public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		String refreshToken = null;
		if (accessToken.getRefreshToken() != null) {
			refreshToken = accessToken.getRefreshToken().getValue();
		}

		if (readAccessToken(accessToken.getValue()) != null) {
			this.removeAccessToken(accessToken);
		}

		OAuth2AccessTokenEntity accessTokenEntity = new OAuth2AccessTokenEntity();
		accessTokenEntity.setTokenId(extractTokenKey(accessToken.getValue()));
		accessTokenEntity.setToken(accessToken);
		accessTokenEntity.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
		accessTokenEntity.setUsername(authentication.isClientOnly() ? null : authentication.getName());
		accessTokenEntity.setClientId(authentication.getOAuth2Request().getClientId());
		accessTokenEntity.setAuthentication(authentication);
		accessTokenEntity.setRefreshToken(extractTokenKey(refreshToken));
		log.info("save access token:{}", accessToken.getValue());
		oAuth2AccessTokenRepository.save(accessTokenEntity);
	}

	@Override
	public OAuth2AccessToken readAccessToken(String tokenValue) {
		log.info("read access token:{}", tokenValue);
		Optional<OAuth2AccessTokenEntity> tokenOptional = oAuth2AccessTokenRepository
				.findByTokenId(extractTokenKey(tokenValue));
		return tokenOptional.isPresent() ? tokenOptional.get().getToken() : null;
	}

	@Override
	public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {
		log.info("remove access token:{}", oAuth2AccessToken.getValue());
		oAuth2AccessTokenRepository.deleteByTokenId(extractTokenKey(oAuth2AccessToken.getValue()));
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
		OAuth2RefreshTokenEntity refreshTokenEntity = new OAuth2RefreshTokenEntity();
		refreshTokenEntity.setTokenId(extractTokenKey(refreshToken.getValue()));
		refreshTokenEntity.setToken(refreshToken);
		refreshTokenEntity.setAuthentication(authentication);

		oAuth2RefreshTokenRepository.save(refreshTokenEntity);
	}

	@Override
	public OAuth2RefreshToken readRefreshToken(String tokenValue) {
		Optional<OAuth2RefreshTokenEntity> refreshTokenOpt = oAuth2RefreshTokenRepository
				.findByTokenId(extractTokenKey(tokenValue));
		return refreshTokenOpt.isPresent() ? refreshTokenOpt.get().getToken() : null;
	}

	@Override
	public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken refreshToken) {

		Optional<OAuth2RefreshTokenEntity> refreshTokenOpt = oAuth2RefreshTokenRepository
				.findByTokenId(extractTokenKey(refreshToken.getValue()));
		return refreshTokenOpt.isPresent() ? refreshTokenOpt.get().getAuthentication() : null;
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
		oAuth2RefreshTokenRepository.deleteByTokenId(extractTokenKey(refreshToken.getValue()));
	}

	@Override
	public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
		oAuth2AccessTokenRepository.deleteByRefreshToken(extractTokenKey(refreshToken.getValue()));
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		OAuth2AccessToken accessToken = null;
		String authenticationId = authenticationKeyGenerator.extractKey(authentication);

		Optional<OAuth2AccessTokenEntity> tokenOpt = oAuth2AccessTokenRepository
				.findByAuthenticationId(authenticationId);
		if (tokenOpt.isPresent()) {
			accessToken = tokenOpt.get().getToken();
			if (accessToken != null && !authenticationId
					.equals(this.authenticationKeyGenerator.extractKey(this.readAuthentication(accessToken)))) {
				this.removeAccessToken(accessToken);
				this.storeAccessToken(accessToken, authentication);
			}
		}
		return accessToken;
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
		Collection<OAuth2AccessToken> tokens = new ArrayList<>();

		List<OAuth2AccessTokenEntity> tokensList = oAuth2AccessTokenRepository.findByUsernameAndClientId(username,
				clientId);
		tokensList.stream().forEach(token -> tokens.add(token.getToken()));

		return tokens;
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
		Collection<OAuth2AccessToken> tokens = new ArrayList<>();
		List<OAuth2AccessTokenEntity> tokenList = oAuth2AccessTokenRepository.findByClientId(clientId);
		tokenList.stream().forEach(token -> tokens.add(token.getToken()));

		return tokens;
	}

	private String extractTokenKey(String value) {
		if (value == null) {
			return null;
		} else {
			return value.replace("-", "");
		}
	}

}