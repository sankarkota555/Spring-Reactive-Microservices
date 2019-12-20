package com.reactive.app.common.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "login")
@Setter
@Getter
public class LoginEntity extends PersistentDataObject {

	@Indexed(unique = true)
	private String username;

	private String password;

	private String googleToken;

	private String facebookToken;

	private String verificationKey;

	private boolean verified;

	// TODO: use cache for otp related stuff
	private String otp;

	@DBRef
	private UserEntity userEntity;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		return getClass() == obj.getClass();
	}

}
