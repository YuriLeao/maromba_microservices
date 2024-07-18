package com.api.maromba.workoutSheet.dtos;

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
public class WorkoutSheetDTO {
	
	private UUID id;
	@NotNull
	private List<UUID> idUser;
	@NotNull
	private UUID idWorkout;
	private LocalDate dateRegister;
	private Boolean active;
	@NotNull
	private String name;
	
}