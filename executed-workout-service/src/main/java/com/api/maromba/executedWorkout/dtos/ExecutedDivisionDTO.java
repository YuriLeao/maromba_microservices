package com.api.maromba.executedWorkout.dtos;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.api.maromba.executedWorkout.models.enums.ExecutionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutedDivisionDTO {

    private UUID id;
    
    @NotNull
    private String name;
    
    @NotNull
    private ExecutionStatus status;

    @NotNull
    private List<ExecutedExerciseDTO> executedExercises;
    
}