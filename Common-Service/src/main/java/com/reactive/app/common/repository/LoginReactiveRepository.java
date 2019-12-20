package com.reactive.app.common.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.reactive.app.common.model.LoginEntity;

import reactor.core.publisher.Mono;

@Repository
public interface LoginReactiveRepository extends ReactiveMongoRepository<LoginEntity, String> {

	@Query("{ 'userEntity.$id': ?0 }")
	public Mono<LoginEntity> findByUserId(ObjectId userId);

	public Mono<LoginEntity> findByUsername(String username);

}
