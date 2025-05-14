package com.api.maromba.executedWorkout.dtos;

import java.time.LocalDateTime;
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
public class ExecutedWorkoutDTO {
	
    private UUID id;
    
    @NotNull
    private String name;
    
    private String description;
    
    @NotNull
    private List<ExecutedDivisionDTO> divisions;

    @NotNull
    private UUID userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime finishAt;
    
    private boolean isActive;
    
}
