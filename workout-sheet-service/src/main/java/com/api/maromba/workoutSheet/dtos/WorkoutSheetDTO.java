package com.api.maromba.workoutSheet.dtos;

import java.time.LocalDate;
import java.util.UUID;
 
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSheetDTO {
	
	private UUID id;
	@NotNull
	private UUID idUser;
	@NotNull
	private UUID idWorkout;
	@NotNull
	private LocalDate dateRegister;
	@NotNull
	private Boolean active;
	@NotNull
	private String name;
	
}