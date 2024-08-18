package com.api.maromba.user.dtos;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

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
	private String name;
	@NotBlank
	private String cpf;
	@NotNull
	private GenderDTO gender;
	@NotBlank
	private String phoneNumber;
	@DecimalMin("30.00")
	private Double weight;
	@NotNull
	private AuthorizationDTO authorization;
	@NotNull
	private UUID companyId;
	private CompanyDTO company;
	@NotNull
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@DateTimeFormat(iso = ISO.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate birthDate;
	private String token;
	
}
