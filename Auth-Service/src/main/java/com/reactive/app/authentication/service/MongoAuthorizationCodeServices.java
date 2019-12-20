package com.reactive.app.authentication.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Service;

import com.reactive.app.authentication.model.OAuth2AuthorizationCodeEntity;
import com.reactive.app.authentication.repository.OAuth2AuthorizationCodeRepository;

@Service
public class MongoAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

	@Autowired
	private OAuth2AuthorizationCodeRepository oAuth2AuthorizationCodeRepository;

	@Override
	protected void store(String code, OAuth2Authentication authentication) {
		OAuth2AuthorizationCodeEntity authorizationCode = new OAuth2AuthorizationCodeEntity();
		authorizationCode.setCode(code);
		authorizationCode.setAuthentication(authentication);
		oAuth2AuthorizationCodeRepository.save(authorizationCode);
	}

	@Override
	protected OAuth2Authentication remove(String code) {
		OAuth2Authentication authentication = null;
		Optional<OAuth2AuthorizationCodeEntity> authorizationCodeOpt = oAuth2AuthorizationCodeRepository
				.findByCode(code);
		if (authorizationCodeOpt.isPresent()) {
			authentication = authorizationCodeOpt.get().getAuthentication();
		}
		return authentication;
	}
}