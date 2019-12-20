package com.reactive.app.common.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "role")
@Setter
@Getter
public class RoleEntity extends PersistentDataObject {

	private String name;

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
