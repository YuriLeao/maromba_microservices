package com.api.maromba.user.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private UUID id;
	@NotBlank
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private String name;
	@NotBlank
	private String gender;
	@NotBlank
	private String cellphone;
	@DecimalMin("30.00")
	private Double weight;
	@NotNull
	private List<String> authorizations;
	@NotNull
	private UUID companyId;
	private String companyName;
	@NotNull
	private LocalDate birthDate;
	private String token;
	
}
