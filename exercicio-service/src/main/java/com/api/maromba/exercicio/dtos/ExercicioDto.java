package com.api.maromba.exercicio.dtos;

import java.util.UUID;
 
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExercicioDto {
	
	private UUID id;
	@NotNull
	private UUID idMovimento;
	@NotNull
	private Integer tempoDescanso;
	@NotNull
	private Integer qtdSeries;
	@NotNull
	private Integer qtdRepeticoes;
	@NotNull
	private Integer carga;
	@NotNull
	private Integer recordCarga;
	@NotBlank
	private String observacao;
	
}