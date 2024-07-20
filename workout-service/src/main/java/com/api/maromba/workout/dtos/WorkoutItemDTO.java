package com.api.maromba.workout.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutItemDTO {
	
	private UUID id;
	@NotNull
	private UUID idExercise;
	@NotNull
	private Integer reps;
	@NotNull
	private Integer sets;
	private BigDecimal weight;
	private String observation;
	private BigDecimal recordWeight;
	@NotNull
	private Integer coolDown;
	
}