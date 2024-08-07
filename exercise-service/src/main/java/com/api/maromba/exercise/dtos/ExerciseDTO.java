package com.api.maromba.exercise.dtos;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDTO {

	private UUID id;
	@NotBlank
	private String name;
	@NotBlank
	private String gif;
	@NotBlank
	private String muscleGroup;
	
}