package com.reactive.app.common.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.reactive.app.common.model.ManufacturerCategoryEntity;
import com.reactive.app.common.model.ManufacturerEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ManufacturerReactiveRepository extends ReactiveMongoRepository<ManufacturerEntity, String> {

	@Query("{ 'adminEntity.$id' : ?0 }")
	public Flux<ManufacturerEntity> findByAdminId(ObjectId adminId);

	public Flux<ManufacturerEntity> findByManufacturerCategory(ManufacturerCategoryEntity manufacturerCategoryEntity);

	public Mono<Long> countByManufacturerCategory(ManufacturerCategoryEntity manufacturerCategoryEntity);

}
