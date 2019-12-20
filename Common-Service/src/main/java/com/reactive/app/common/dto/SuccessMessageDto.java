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
public class SuccessMessageDto implements Serializable {

	private static final long serialVersionUID = -6991471245884327364L;

	private String message;

}
