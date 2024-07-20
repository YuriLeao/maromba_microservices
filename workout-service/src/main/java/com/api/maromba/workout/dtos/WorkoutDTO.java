package com.api.maromba.workout.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDTO {
	
	private UUID id;
	@NotNull
	private String name;
	private LocalDate dateDone;
	@NotNull
	private List<WorkoutItemDTO> workoutItems;
	
}