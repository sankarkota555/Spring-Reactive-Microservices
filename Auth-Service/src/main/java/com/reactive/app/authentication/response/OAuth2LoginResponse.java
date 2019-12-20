package com.reactive.app.authentication.response;

import java.io.Serializable;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OAuth2LoginResponse implements Serializable {

	private static final long serialVersionUID = -4546135992676405951L;

	private OAuth2AccessToken token;

	private Object userRegistrationErrorMessage;

	private Object tokenErrorMessage;

}
