package com.reactive.app.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturerCategoryDto implements Serializable {

	private static final long serialVersionUID = -4067379355716002372L;

	private String name;

}
