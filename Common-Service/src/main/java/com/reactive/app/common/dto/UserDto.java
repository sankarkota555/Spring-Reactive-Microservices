package com.reactive.app.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.reactive.app.common.model.ContactDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserDto implements Serializable {

	private static final long serialVersionUID = -107861008443445770L;

	private String id;

	private String firstName;

	private String middleName;

	private String lastName;

	private boolean isDeleted;

	private Date deletedDate;

	private String verificationKey;

	private ManufacturerDto manufacturerDto;

	private ManufacturerCategoryDto manufacturerCategoryDto;

	private boolean enabled;

	private List<ContactDetails> contactDetails = new ArrayList<>();

	private Set<RoleDto> roles = new HashSet<>();

}
