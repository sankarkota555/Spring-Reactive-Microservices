package com.reactive.app.common.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "end_user")
@Setter
@Getter
public class EndUserEntity extends ManufacturerEntity {

	@DBRef(lazy = true)
	private ManufacturerEntity manufacturer;

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
