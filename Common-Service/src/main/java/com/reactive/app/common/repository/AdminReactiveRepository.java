package com.reactive.app.common.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.reactive.app.common.model.AdminEntity;

@Repository
public interface AdminReactiveRepository extends ReactiveMongoRepository<AdminEntity, String> {

}
