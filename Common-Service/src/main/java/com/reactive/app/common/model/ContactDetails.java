package com.reactive.app.common.model;

import java.io.Serializable;

import com.reactive.app.common.constants.ContactType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContactDetails implements Serializable {

	private static final long serialVersionUID = 1574087095042895768L;

	private ContactType type;

	private String value;

	private boolean primary;

}
