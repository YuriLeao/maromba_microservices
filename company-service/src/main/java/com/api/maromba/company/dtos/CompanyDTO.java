package com.api.maromba.company.dtos;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {

	private UUID id;
	@NotBlank
	private String name;
	@NotBlank
	private String cnpj;
	@NotBlank
	private String email;
	@NotBlank
	private String stateRegistration;
	@NotBlank
	private String phoneNumber;
}
