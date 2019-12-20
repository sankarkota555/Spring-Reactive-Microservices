package com.reactive.app.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ManufacturerDto implements Serializable {

	private static final long serialVersionUID = 2080587371204042374L;

	private String name;

	private String id;

	private ManufacturerCategoryDto manufacturerCategoryDto;

	public ManufacturerDto(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}

	public ManufacturerDto(String id) {
		super();
		this.id = id;
	}

}
