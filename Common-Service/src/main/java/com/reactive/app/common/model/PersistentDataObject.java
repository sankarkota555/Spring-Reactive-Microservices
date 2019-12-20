package com.reactive.app.common.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersistentDataObject {

	@Id
	private String id;

	@Version
	private Long versionNumber;

	@CreatedDate
	public Long createdDate;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersistentDataObject other = (PersistentDataObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
