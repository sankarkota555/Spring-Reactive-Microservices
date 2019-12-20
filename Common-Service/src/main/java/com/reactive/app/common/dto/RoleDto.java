package com.reactive.app.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto implements Serializable {

	private static final long serialVersionUID = 3045183243202618618L;

	private String name;

}
