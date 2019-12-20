package com.reactive.app.common.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.reactive.app.common.model.LoginEntity;

@Repository
public interface LoginRepository extends MongoRepository<LoginEntity, String> {

	public LoginEntity findByUsername(String username);
}
