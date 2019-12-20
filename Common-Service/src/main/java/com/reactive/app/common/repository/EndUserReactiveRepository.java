package com.reactive.app.common.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.reactive.app.common.model.EndUserEntity;

import reactor.core.publisher.Flux;

@Repository
public interface EndUserReactiveRepository extends ReactiveMongoRepository<EndUserEntity, String> {

	@Query(value = "{ 'manufacturer.$id' : ?0 }")
	public Flux<EndUserEntity> findByManufacturerId(ObjectId manufacturerId);

	public Flux<EndUserEntity> findByIdIn(List<String> userIds);

	@Query(value = "{ 'contactDetails.value' : { $in : ?0} }")
	public Flux<EndUserEntity> findByEmailIn(List<String> emailIds);

}
