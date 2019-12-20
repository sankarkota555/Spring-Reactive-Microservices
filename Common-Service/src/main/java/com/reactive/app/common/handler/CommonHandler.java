package com.reactive.app.common.handler;

import java.util.List;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerResponse.BodyBuilder;

import com.reactive.app.common.constants.HeaderConstants;

public class CommonHandler {

	protected BodyBuilder ok() {
		return ServerResponse.ok();
	}

	protected BodyBuilder unprocessableEntity() {
		return ServerResponse.unprocessableEntity();
	}

	protected BodyBuilder badRequest() {
		return ServerResponse.badRequest();
	}

	protected String getLoginUserId(ServerRequest request) {
		return request.headers().header(HeaderConstants.LOGIN_USER_ID).get(0);
	}

	protected List<String> getLoginUserRoles(ServerRequest request) {
		return request.headers().header(HeaderConstants.LOGIN_USER_ROLE);
	}

	protected boolean isLoginUserHasRole(ServerRequest request, String roleName) {
		return getLoginUserRoles(request).stream().anyMatch(role -> {
			return role.equals("ROLE_" + roleName);
		});
	}
}
