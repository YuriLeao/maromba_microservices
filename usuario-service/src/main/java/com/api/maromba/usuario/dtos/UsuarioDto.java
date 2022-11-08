package com.api.maromba.usuario.dtos;

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
public class UsuarioDto {

	private UUID id;
	@NotBlank
	private String usuario;
	@NotBlank
	private String senha;
	@NotBlank
	private String email;
	@NotBlank
	private String genero;
	@NotBlank
	private String telefone;
	@DecimalMin("30.00")
	private Double peso;
	@NotNull
	private List<String> autorizacoes;
	@NotNull
	private UUID empresaId;
	private String empresaNome;
	@NotNull
	private LocalDate dataNascimento;
	private String token;
	
}
