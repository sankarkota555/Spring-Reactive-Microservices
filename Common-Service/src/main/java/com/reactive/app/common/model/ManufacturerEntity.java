package com.reactive.app.common.model;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "manufacturer")
@Setter
@Getter
public class ManufacturerEntity extends AdminEntity {

	private boolean isDeleted;

	private Date deletedDate;

	@DBRef
	@Indexed
	private ManufacturerCategoryEntity manufacturerCategory;

	@DBRef
	@Indexed
	private AdminEntity adminEntity;

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
