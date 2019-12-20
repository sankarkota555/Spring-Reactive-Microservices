package com.reactive.app.common.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.reactive.app.common.model.EnvironmentDetailsEntity;

import reactor.core.publisher.Mono;

@Repository
public interface EnvironmentDetailsReactiveRepository
		extends ReactiveMongoRepository<EnvironmentDetailsEntity, String> {

	public Mono<EnvironmentDetailsEntity> findByEnvironmentName(String envName);

}
