package com.api.maromba.workoutSheet.dtos;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDivisionDTO {
	
	private UUID id;
	@NotNull
	private String name;
	@NotNull
	private List<WorkoutExerciseDTO> exercises;
	
}