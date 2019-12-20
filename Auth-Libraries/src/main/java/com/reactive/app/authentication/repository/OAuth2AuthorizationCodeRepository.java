package com.reactive.app.authentication.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.reactive.app.authentication.model.OAuth2AuthorizationCodeEntity;

@Repository
public interface OAuth2AuthorizationCodeRepository extends MongoRepository<OAuth2AuthorizationCodeEntity, String> {

	public Optional<OAuth2AuthorizationCodeEntity> findByCode(String code);

}
