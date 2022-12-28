package com.api.maromba.grupoMuscular.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrupoMuscularDto {
	
	private UUID id;
	@NotEmpty
	private List<UUID> exercicios;
	@NotEmpty
	private String nome;
	@NotEmpty
	private List<LocalDate> diasRealizados;
	
}