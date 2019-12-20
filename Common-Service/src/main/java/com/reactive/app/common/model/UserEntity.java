package com.reactive.app.common.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserEntity extends PersistentDataObject {

	private String firstName;

	private String middleName;

	private String lastName;

	private boolean enabled = true;

	private List<ContactDetails> contactDetails = new ArrayList<>();

	@DBRef(lazy = true)
	private Set<RoleEntity> roles = new HashSet<>();

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
