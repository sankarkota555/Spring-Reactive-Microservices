package com.reactive.app.authentication.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.reactive.app.authentication.service.SerializableObjectConverter;

@Document(collection = "oauth_refresh_token")
public class OAuth2RefreshTokenEntity {

	@Id
	private String id;

	@Version
	private Long versionNumber;

	@Indexed
	private String tokenId;

	private OAuth2RefreshToken token;

	private String authentication;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public OAuth2RefreshToken getToken() {
		return token;
	}

	public void setToken(OAuth2RefreshToken token) {
		this.token = token;
	}

	public OAuth2Authentication getAuthentication() {
		return SerializableObjectConverter.deserialize(authentication);
	}

	public void setAuthentication(OAuth2Authentication authentication) {
		this.authentication = SerializableObjectConverter.serialize(authentication);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

}
