package com.api.maromba.treino.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreinoDto {
	
	private UUID id;
	@NotEmpty
	private List<UUID> gruposMusculares;
	@NotNull
	private UUID usuario;
	
}