package com.reactive.app.common.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.reactive.app.common.model.ManufacturerCategoryEntity;

import reactor.core.publisher.Mono;

@Repository
public interface ManufacturerCategoryReactiveRepository extends ReactiveMongoRepository<ManufacturerCategoryEntity, String> {

	public Mono<ManufacturerCategoryEntity> findByName(String name);

}
