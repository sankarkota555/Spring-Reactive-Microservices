package com.reactive.app.user.password.management.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordManagementDto implements Serializable {

	private static final long serialVersionUID = -4940985620461109336L;

	private String username;

	private String newPassword;

	private String oldPassword;

	private String otp;

}
