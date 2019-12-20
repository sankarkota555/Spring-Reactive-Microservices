package com.reactive.app.authentication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.reactive.app.authentication.model.OAuth2AccessTokenEntity;

@Repository
public interface OAuth2AccessTokenRepository extends MongoRepository<OAuth2AccessTokenEntity, String> {

	public Optional<OAuth2AccessTokenEntity> findByTokenId(String tokenId);

	public Optional<OAuth2AccessTokenEntity> findByAuthenticationId(String authenticationId);

	public List<OAuth2AccessTokenEntity> findByUsernameAndClientId(String username, String clientId);

	public List<OAuth2AccessTokenEntity> findByClientId(String clientId);

	@DeleteQuery
	public void deleteByTokenId(String tokenId);

	@DeleteQuery
	public void deleteByRefreshToken(String refreshToken);
}
