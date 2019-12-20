package com.reactive.app.common.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.reactive.app.common.model.RoleEntity;

import reactor.core.publisher.Mono;

@Repository
public interface RoleReactiveRepository extends ReactiveMongoRepository<RoleEntity, String> {

	public Mono<RoleEntity> findByName(String name);
}
