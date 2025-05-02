package com.api.maromba.exercise.dtos;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MuscleGroupDTO {

	private String id;
	@NotBlank
	private String description;
	
}