package com.api.maromba.exercicio.dtos;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExercicioDto {

	private UUID id;
	@NotBlank
	private String nome;
	@NotBlank
	private String video;
	@NotBlank
	private String grupoMuscular;
	
}