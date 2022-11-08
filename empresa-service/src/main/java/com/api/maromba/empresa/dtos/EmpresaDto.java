package com.api.maromba.empresa.dtos;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDto {

	private UUID id;
	@NotBlank
	private String nome;
	@NotBlank
	private String cnpj;
	@NotBlank
	private String email;
	@NotBlank
	private String inscricaoEstadual;
	@NotBlank
	private String telefone;
}
