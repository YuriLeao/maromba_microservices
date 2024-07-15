package com.api.maromba.workout.dtos;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDTO {
	
	private UUID id;
	@NotEmpty
	private List<UUID> musclesGroup;
	@NotNull
	private UUID user;
	
}