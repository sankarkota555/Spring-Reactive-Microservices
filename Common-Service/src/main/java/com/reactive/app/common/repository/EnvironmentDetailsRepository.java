package com.reactive.app.common.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.reactive.app.common.model.EnvironmentDetailsEntity;

@Repository
public interface EnvironmentDetailsRepository extends MongoRepository<EnvironmentDetailsEntity, String> {

	public EnvironmentDetailsEntity findByEnvironmentName(String envName);

}
