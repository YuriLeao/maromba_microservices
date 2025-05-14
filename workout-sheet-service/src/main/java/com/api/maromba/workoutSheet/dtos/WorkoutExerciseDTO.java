package com.api.maromba.workoutSheet.dtos;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseDTO {
	
	private UUID id;
	@NotNull
	private UUID exerciseId;
	@NotNull
	private Integer reps;
	@NotNull
	private Integer sets;
	private String observation;
	@NotNull
	private Integer restTimeSeconds;
	
}