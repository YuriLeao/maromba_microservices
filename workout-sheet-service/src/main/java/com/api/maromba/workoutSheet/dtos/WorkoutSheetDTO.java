package com.api.maromba.workoutSheet.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSheetDTO {

	private UUID id;
	
	@NotNull
	private String name;
	
	private String description;
	
	@NotNull
	private List<WorkoutDivisionDTO> divisions;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDate createdAt;

}