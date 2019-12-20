package com.reactive.app.authentication.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.reactive.app.authentication.model.OAuth2RefreshTokenEntity;

@Repository
public interface OAuth2RefreshTokenRepository extends MongoRepository<OAuth2RefreshTokenEntity, String> {

	public Optional<OAuth2RefreshTokenEntity> findByTokenId(String tokenId);

	@DeleteQuery
	public void deleteByTokenId(String tokenId);
}
