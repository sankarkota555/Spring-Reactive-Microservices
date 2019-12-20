package com.reactive.app.common.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorMessageDto extends SuccessMessageDto {

	private static final long serialVersionUID = 5831303694417518203L;

	public ErrorMessageDto(String message) {
		super(message);
	}

}
