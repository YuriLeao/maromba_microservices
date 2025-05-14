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
public class ExecutedExerciseDTO {

    private UUID id;

    @NotNull
    private UUID exerciseId; 

    @NotNull
    private Integer plannedReps;

    @NotNull
    private Integer plannedSets;

    @NotNull
    private Integer restTime;

    private String notes;
    
    @NotNull
    private ExecutionStatus status;
    
    private List<ExecutedRepetitionDTO> executedRepetitions;
}
