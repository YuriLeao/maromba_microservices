package com.api.maromba.executedWorkout.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.api.maromba.executedWorkout.models.enums.ExecutionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutedRepetitionDTO {

    private UUID id;

    @NotNull
    private Integer performedReps;

    @NotNull
    private Integer performedSets;

    private BigDecimal usedWeight;
    
    @NotNull
    private ExecutionStatus status;
}
