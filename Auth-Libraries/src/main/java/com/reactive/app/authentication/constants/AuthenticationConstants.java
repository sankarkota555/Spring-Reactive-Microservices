package com.reactive.app.authentication.constants;

public class AuthenticationConstants {

	private AuthenticationConstants() {

	}

	public static final String RESOURCE_ID = "oauth2-resource";

	public static final String TAPAS_CLIENT_ID = "tapasOAuth2ClientApp";

	public static final String TAPAS_CLIENT_SECRET_TEXT = "123456";

	public static final String TAPAS_CLIENT_SECRET = "{noop}" + TAPAS_CLIENT_SECRET_TEXT;

	public static final Integer ACCESS_TOKEN_EXPIRY_TIME = 60 * 60 * 24; // 24 HRS

	public static final Integer REFRESH_TOKEN_EXPIRY_TIME = 60 * 60 * 24 * 30; // 30 Days

}
