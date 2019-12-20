package com.reactive.app.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistrationDto implements Serializable {

	private static final long serialVersionUID = 330714745284934499L;

	private String username;

	private String password;

	private UserDto userDto;

}
