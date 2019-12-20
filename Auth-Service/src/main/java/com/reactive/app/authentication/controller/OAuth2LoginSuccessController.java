package com.reactive.app.authentication.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reactive.app.authentication.constants.AuthenticationConstants;
import com.reactive.app.authentication.constnats.OAuth2LoginConstnats;
import com.reactive.app.authentication.feignclients.AuthenticationFeignClient;
import com.reactive.app.authentication.feignclients.UserRegistrationFeignClient;
import com.reactive.app.authentication.response.OAuth2LoginResponse;
import com.reactive.app.common.constants.CommonRouteConstants;
import com.reactive.app.common.constants.ContactType;
import com.reactive.app.common.constants.QueryParameters;
import com.reactive.app.common.dto.RegistrationDto;
import com.reactive.app.common.dto.UserDto;
import com.reactive.app.common.model.ContactDetails;
import com.reactive.app.common.model.LoginEntity;
import com.reactive.app.common.repository.LoginRepository;

@RestController
public class OAuth2LoginSuccessController {

	private static final Logger log = LoggerFactory.getLogger(OAuth2LoginSuccessController.class);

	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private UserRegistrationFeignClient userRegistrationFeignClient;

	@Autowired
	private AuthenticationFeignClient authenticationFeignClient;

	@Autowired
	OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

	String basicAuthenticationString = null;

	@GetMapping(CommonRouteConstants.OAUTH2_LOGIN_SUCCESS_ROUTE)
	public ResponseEntity<Object> test(OAuth2AuthenticationToken oAuth2AuthenticationToken) {

		String loginProvider = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

		OAuth2LoginResponse oAuth2LoginResponse = new OAuth2LoginResponse();

		/*
		 * if (loginProvider.equals(OAuth2LoginConstnats.GOOGLE_OAUTH2_PROVIDER)) {
		 * DefaultOidcUser googleUser = (DefaultOidcUser)
		 * oAuth2AuthenticationToken.getPrincipal(); email = googleUser.getEmail();
		 * token = googleUser.getIdToken().getTokenValue(); name =
		 * googleUser.getFullName(); log.info("token:{}", token);
		 * 
		 * }
		 */
		if (loginProvider.equals(OAuth2LoginConstnats.FACEBOOK_OAUTH2_PROVIDER)
				|| loginProvider.equals(OAuth2LoginConstnats.GOOGLE_OAUTH2_PROVIDER)) {

			DefaultOAuth2User oAuth2User = (DefaultOAuth2User) oAuth2AuthenticationToken.getPrincipal();

			String email = oAuth2User.getAttribute(OAuth2LoginConstnats.USER_EMAIL_ATTRIBUTE);
			String name = oAuth2User.getAttribute(OAuth2LoginConstnats.USER_NAME_ATTRIBUTE);
			String token = oAuth2AuthorizedClientService
					.loadAuthorizedClient(loginProvider, oAuth2AuthenticationToken.getName()).getAccessToken()
					.getTokenValue();

			LoginEntity loginObj = loginRepository.findByUsername(email);
			// if object found, update the token
			if (loginObj != null) {

				switch (loginProvider) {
				case OAuth2LoginConstnats.FACEBOOK_OAUTH2_PROVIDER:
					log.info("Updating Facebook token");
					loginObj.setFacebookToken(token);
					break;

				case OAuth2LoginConstnats.GOOGLE_OAUTH2_PROVIDER:
					log.info("Updating google token");
					loginObj.setGoogleToken(token);
					break;
				default:
					log.error("loginProvider:{} is not handled for token updation", loginProvider);
					break;
				}

				loginRepository.save(loginObj);

			}
			// If object not found, register the user
			else {
				RegistrationDto registrationDto = new RegistrationDto();
				registrationDto.setUsername(email);
				UserDto userDto = new UserDto();
				userDto.setFirstName(name);
				ContactDetails contactDetails = new ContactDetails();
				contactDetails.setValue(email);
				contactDetails.setType(ContactType.EMAILID);
				userDto.getContactDetails().add(contactDetails);
				registrationDto.setUserDto(userDto);

				log.info("Registering new OAuth2 user with loginProvider:{} - email:{}, name:{}, token:{}",
						loginProvider, email, name, token);
				ResponseEntity<Object> entity = userRegistrationFeignClient.registerUser(registrationDto);
				// If not success, then return error details
				if (!entity.getStatusCode().is2xxSuccessful()) {
					oAuth2LoginResponse.setUserRegistrationErrorMessage(entity.getBody());
					return new ResponseEntity<>(oAuth2LoginResponse, entity.getStatusCode());
				}
			}

			Map<String, String> params = new HashMap<>();
			params.put("grant_type", "password");
			params.put("username", email);
			params.put("password", token);
			params.put(QueryParameters.LOGIN_PROVIDER, loginProvider);

			// Make request to get access token
			ResponseEntity<Object> tokenResponse = authenticationFeignClient.getToken(this.basicAuthenticationString,
					params);

			if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
				oAuth2LoginResponse.setTokenErrorMessage(tokenResponse.getBody());
				return new ResponseEntity<>(oAuth2LoginResponse, tokenResponse.getStatusCode());
			}

			return new ResponseEntity<>(tokenResponse.getBody(), tokenResponse.getStatusCode());
		}

		return new ResponseEntity<>("Login provider:" + loginProvider + " is not listed",
				HttpStatus.PRECONDITION_FAILED);
	}

	@PostConstruct
	public void initialisation() {
		// Initialize basic authentication value
		this.basicAuthenticationString = String.format("Basic %s",
				new String(Base64.getEncoder().encode(String.format("%s:%s", AuthenticationConstants.TAPAS_CLIENT_ID,
						AuthenticationConstants.TAPAS_CLIENT_SECRET_TEXT).getBytes())));

	}

}
