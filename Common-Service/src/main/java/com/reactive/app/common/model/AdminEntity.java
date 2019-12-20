package com.reactive.app.common.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin")
public class AdminEntity extends UserEntity {

}
