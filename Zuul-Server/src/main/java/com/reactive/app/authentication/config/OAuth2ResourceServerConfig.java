package com.reactive.app.authentication.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import com.reactive.app.authentication.constants.AuthenticationConstants;
import com.reactive.app.common.constants.CommonRouteConstants;
import com.reactive.app.common.constants.RoleConstants;

/**
 * 
 * Spring OAuth2 resource server configuration
 *
 */
@Configuration
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final Logger log = LoggerFactory.getLogger(OAuth2ResourceServerConfig.class);

	@Autowired
	private DefaultTokenServices resourceServerTokenServices;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(AuthenticationConstants.RESOURCE_ID).authenticationManager(OAuth2AuthenticationManager());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		// permit all options calls
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**/**").permitAll();

		// Permit all request for token generation
		// path -> /auth/**
		http.authorizeRequests().antMatchers(CommonRouteConstants.AUTH_SERVICE_PATTERN + "/**").permitAll();

		// Permit all requests for device registration template file
		// path -> /dc/deviceRegistrationTemplateFile
		http.authorizeRequests().antMatchers(CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN
				+ CommonRouteConstants.DEVICE_REGISTRATION_TEMPLATE_FILE_ROUTE).permitAll();

		// Permit all requests for user and admin registration
		http.authorizeRequests().antMatchers(HttpMethod.POST,
				// path -> /um/user - POST
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.USER_ROUTE,
				// path -> /um/admin - POST
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.ADMIN_ROUTE).permitAll();

		// Permit all requests for user verification
		// path -> /um/userVerification/** - GET
		http.authorizeRequests().antMatchers(HttpMethod.GET, CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN
				+ CommonRouteConstants.USER_VERIFICATION_ROUTE + "/**").permitAll();

		// Permit all request for triggering scheduled device control
		// path -> /dc/controlDevice/{id}
		http.authorizeRequests().antMatchers(
				CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.CONTROL_DEVICE_ROUTE + "/*")
				.permitAll();

		// Permit all request for forgot password and change password
		// path -> /auth/forgotPassword & auth/changePassword
		http.authorizeRequests().antMatchers(
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.FORGOT_PASSWORD_ROUTE,
				CommonRouteConstants.AUTH_SERVICE_PATTERN + CommonRouteConstants.CHANGE_PASSWORD_ROUTE).permitAll();

		// URL pattern for Admin role
		http.authorizeRequests().antMatchers(HttpMethod.GET,
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
		http.authorizeRequests().antMatchers(
				// path -> /um/admin
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.ADMIN_ROUTE,
				// path -> /um/admin/**
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.ADMIN_ROUTE + "/**")
				.hasAnyRole(RoleConstants.ADMIN_ROLE);

		// path -> /um/manufacturer - POST
		http.authorizeRequests().antMatchers(HttpMethod.POST,
				// path -> /um/manufacturer - POST
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.MANUFACTURER_ROUTE)
				.hasAnyRole(RoleConstants.ADMIN_ROLE);

		// path -> /um/manufacturer/**
		http.authorizeRequests()
				.antMatchers(CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN
						+ CommonRouteConstants.MANUFACTURER_ROUTE + "/**")
				.hasAnyRole(RoleConstants.MANUFACTURER_ROLE, RoleConstants.ADMIN_ROLE);

		// path -> /dc/deviceCategory/**
		http.authorizeRequests()
				.antMatchers(CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN
						+ CommonRouteConstants.DEVICE_CATEGORY_ROUTE + "/**")
				.hasAnyRole(RoleConstants.MANUFACTURER_ROLE);

		// path -> /dc/manufacturer/**
		// Example -> /dc/manufacturer/{id}/deviceCategory -> get device categories of a
		// specific manufacturer
		http.authorizeRequests()
				.antMatchers(CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN
						+ CommonRouteConstants.MANUFACTURER_ROUTE + "/**")
				.hasAnyRole(RoleConstants.MANUFACTURER_ROLE, RoleConstants.ADMIN_ROLE);

		// path -> /dc/device - POST
		http.authorizeRequests()
				.antMatchers(HttpMethod.POST,
						CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.DEVICE_ROUTE)
				.hasAnyRole(RoleConstants.MANUFACTURER_ROLE, RoleConstants.ADMIN_ROLE);

		// path -> /dc/device
		http.authorizeRequests()
				.antMatchers(CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.DEVICE_ROUTE)
				.hasAnyRole(RoleConstants.MANUFACTURER_ROLE, RoleConstants.USER_ROLE);

		// path -> /dc/device/**
		http.authorizeRequests()
				.antMatchers(HttpMethod.POST,
						CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.DEVICE_ROUTE + "/**")
				.hasAnyRole(RoleConstants.MANUFACTURER_ROLE, RoleConstants.USER_ROLE);

		// path -> /dc/room & /dc/room/**
		http.authorizeRequests()
				.antMatchers(
						CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.ROOM_ROUTE + "/**")
				.hasAnyRole(RoleConstants.USER_ROLE);

		// path -> /dc/bulkDeviceRegistration - POST
		http.authorizeRequests()
				.antMatchers(HttpMethod.POST,
						CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN
								+ CommonRouteConstants.BULK_DEVICE_REGISTRATION_ROUTE)
				.hasAnyRole(RoleConstants.MANUFACTURER_ROLE);

		// path -> /dc/user/...
		http.authorizeRequests()
				.antMatchers(
						CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.USER_ROUTE + "/**")
				.hasAnyRole(RoleConstants.USER_ROLE);

		// path -> /dc/admin/...
		http.authorizeRequests()
				.antMatchers(
						CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.ADMIN_ROUTE + "/**")
				.hasAnyRole(RoleConstants.ADMIN_ROLE);

		// path -> /dc/controlDevice & /dc/scheduled/controlDevice
		http.authorizeRequests()
				.antMatchers(
						CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.CONTROL_DEVICE_ROUTE,
						CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN + CommonRouteConstants.SCHEDULED_ROUTE
								+ CommonRouteConstants.CONTROL_DEVICE_ROUTE)
				.hasAnyRole(RoleConstants.USER_ROLE);

		// URL pattern for user role
		http.authorizeRequests().antMatchers(
				// path -> /um/user
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.USER_ROUTE,
				// path -> /um/user/**
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.USER_ROUTE + "/**")
				.hasAnyRole(RoleConstants.USER_ROLE);

		// URL pattern for user profile
		http.authorizeRequests().antMatchers(
				// path -> /um/userProfile
				CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN + CommonRouteConstants.USER_PROFILE_ROUTE)
				.hasAnyRole(RoleConstants.ADMIN_ROLE, RoleConstants.MANUFACTURER_ROLE, RoleConstants.USER_ROLE);

	}

	@Bean
	public OAuth2AuthenticationManager OAuth2AuthenticationManager() {
		OAuth2AuthenticationManager oAuth2AuthenticationManager = new OAuth2AuthenticationManager();
		oAuth2AuthenticationManager.setTokenServices(resourceServerTokenServices);
		oAuth2AuthenticationManager.setResourceId(AuthenticationConstants.RESOURCE_ID);
		return oAuth2AuthenticationManager;
	}
}
