package com.reactive.app.gateway.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import com.reactive.app.common.constants.CommonRouteConstants;
import com.reactive.app.common.constants.RoleConstants;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain oauthTokenAuthConfig(ServerHttpSecurity security,
			AuthenticationWebFilter oauthAuthenticationWebFilter) {

		security.csrf().disable();

		// security.logout().disable();

		// security.httpBasic().disable();

		// security.formLogin().disable();

		// security.exceptionHandling();

		final AuthorizeExchangeSpec authorizeExchange = security.authorizeExchange();

		// Add OAuth authentication filter
		security.securityMatcher(notMatches(CommonRouteConstants.AUTH_SERVICE_PATTERN + "/**"))
				.addFilterAt(oauthAuthenticationWebFilter, SecurityWebFiltersOrder.HTTP_BASIC);

		// permit all options calls
		authorizeExchange.pathMatchers(HttpMethod.OPTIONS).permitAll();

		// Permit all request for token generation
		// path -> /auth/**
		authorizeExchange.pathMatchers(CommonRouteConstants.AUTH_SERVICE_PATTERN + "/**").permitAll();

		// Permit all requests for socket and social login
		authorizeExchange.pathMatchers(CommonRouteConstants.OAUTH2_SERVICE_PATTERN + "/**",
				CommonRouteConstants.SOCKET_IO_ROUTE + "/**").permitAll();

		// Permit all requests for device registration template file
		// path -> /dc/deviceRegistrationTemplateFile
		authorizeExchange.pathMatchers(CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN
				+ CommonRouteConstants.DEVICE_REGISTRATION_TEMPLATE_FILE_ROUTE).permitAll();

		// Permit all requests for user and admin registration
		authorizeExchange.pathMatchers(HttpMethod.POST,
				// path -> /um/user - POST
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.USER_ROUTE,
				// path -> /um/admin - POST
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.ADMIN_ROUTE).permitAll();

		// Permit all requests for user verification
		// path -> /um/userVerification/** - GET
		authorizeExchange.pathMatchers(HttpMethod.GET, CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN
				+ CommonRouteConstants.USER_VERIFICATION_ROUTE + "/**").permitAll();

		// Permit all request for triggering scheduled device control
		// path -> /dc/controlDevice/{id}
		authorizeExchange.pathMatchers(
				CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.CONTROL_DEVICE_ROUTE + "/*")
				.permitAll();

		// Permit all request for forgot password and change password
		// path -> /auth/forgotPassword & auth/changePassword
		authorizeExchange
				.pathMatchers(
						CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN
								+ CommonRouteConstants.FORGOT_PASSWORD_ROUTE,
						CommonRouteConstants.AUTH_SERVICE_PATTERN + CommonRouteConstants.CHANGE_PASSWORD_ROUTE)
				.permitAll();

		// URL pattern for Admin role
		authorizeExchange.pathMatchers(HttpMethod.GET,
				// path -> /um/admin - GET
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.ADMIN_ROUTE,
				// path -> /um/manufacturer - GET
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.MANUFACTURER_ROUTE,
				// path -> /um/user - GET
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.USER_ROUTE,
				// path -> /um/manufacturer - POST
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.MANUFACTURER_ROUTE)
				.hasRole(RoleConstants.ADMIN_ROLE);

		// URL pattern for Admin role
		authorizeExchange.pathMatchers(
				// path -> /um/admin
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.ADMIN_ROUTE,
				// path -> /um/admin/**
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.ADMIN_ROUTE + "/**")
				.hasAnyRole(RoleConstants.ADMIN_ROLE);

		// path -> /um/manufacturer - POST
		authorizeExchange.pathMatchers(HttpMethod.POST,
				// path -> /um/manufacturer - POST
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.MANUFACTURER_ROUTE)
				.hasAnyRole(RoleConstants.ADMIN_ROLE);

		// path -> /um/manufacturer/**
		authorizeExchange
				.pathMatchers(CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN
						+ CommonRouteConstants.MANUFACTURER_ROUTE + "/**")
				.hasAnyRole(RoleConstants.MANUFACTURER_ROLE, RoleConstants.ADMIN_ROLE);

		// path -> /dc/deviceCategory/**
		authorizeExchange
				.pathMatchers(CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN
						+ CommonRouteConstants.DEVICE_CATEGORY_ROUTE + "/**")
				.hasAnyRole(RoleConstants.MANUFACTURER_ROLE);

		// path -> /dc/manufacturer/**
		// Example -> /dc/manufacturer/{id}/deviceCategory -> get device categories of a
		// specific manufacturer
		authorizeExchange
				.pathMatchers(CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN
						+ CommonRouteConstants.MANUFACTURER_ROUTE + "/**")
				.hasAnyRole(RoleConstants.MANUFACTURER_ROLE, RoleConstants.ADMIN_ROLE);

		// path -> /dc/device - POST
		authorizeExchange
				.pathMatchers(HttpMethod.POST,
						CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.DEVICE_ROUTE)
				.hasAnyRole(RoleConstants.MANUFACTURER_ROLE, RoleConstants.ADMIN_ROLE);

		// path -> /dc/device
		authorizeExchange
				.pathMatchers(CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.DEVICE_ROUTE)
				.hasAnyRole(RoleConstants.MANUFACTURER_ROLE, RoleConstants.USER_ROLE);

		// path -> /dc/device/**
		authorizeExchange
				.pathMatchers(HttpMethod.POST,
						CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.DEVICE_ROUTE + "/**")
				.hasAnyRole(RoleConstants.MANUFACTURER_ROLE, RoleConstants.USER_ROLE);

		// path -> /dc/room & /dc/room/**
		authorizeExchange
				.pathMatchers(
						CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.ROOM_ROUTE + "/**")
				.hasAnyRole(RoleConstants.USER_ROLE);

		// path -> /dc/bulkDeviceRegistration - POST
		authorizeExchange
				.pathMatchers(HttpMethod.POST,
						CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN
								+ CommonRouteConstants.BULK_DEVICE_REGISTRATION_ROUTE)
				.hasAnyRole(RoleConstants.MANUFACTURER_ROLE);

		// path -> /dc/user/...
		authorizeExchange
				.pathMatchers(
						CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.USER_ROUTE + "/**")
				.hasAnyRole(RoleConstants.USER_ROLE);

		// path -> /dc/admin/...
		authorizeExchange
				.pathMatchers(
						CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.ADMIN_ROUTE + "/**")
				.hasAnyRole(RoleConstants.ADMIN_ROLE);

		// path -> /dc/controlDevice & /dc/scheduled/controlDevice
		authorizeExchange.pathMatchers(
				CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.CONTROL_DEVICE_ROUTE,
				CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.SCHEDULED_ROUTE
						+ CommonRouteConstants.CONTROL_DEVICE_ROUTE)
				.hasAnyRole(RoleConstants.USER_ROLE);

		// URL pattern for user role
		authorizeExchange.pathMatchers(
				// path -> /um/user
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.USER_ROUTE,
				// path -> /um/user/**
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.USER_ROUTE + "/**")
				.hasAnyRole(RoleConstants.USER_ROLE);

		// URL pattern for user profile
		authorizeExchange.pathMatchers(
				// path -> /um/userProfile
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.USER_PROFILE_ROUTE)
				.hasAnyRole(RoleConstants.ADMIN_ROLE, RoleConstants.MANUFACTURER_ROLE, RoleConstants.USER_ROLE);

		authorizeExchange.anyExchange().authenticated();

		return security.build();

	}

	private ServerWebExchangeMatcher matches(String... routes) {
		return ServerWebExchangeMatchers.pathMatchers(routes);
	}

	private ServerWebExchangeMatcher notMatches(String... routes) {
		return new NegatedServerWebExchangeMatcher(matches(routes));
	}

}
