package com.api.maromba.executedWorkout.models;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.api.maromba.executedWorkout.models.enums.ExecutionStatus;

import lombok.Data;

@Entity
@Table(name = "tb_executed_repetition")
@Data
public class ExecutedRepetitionModel {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executed_exercise_id", nullable = false)
    private ExecutedExerciseModel executedExercise;

    @Column
    private Integer performedReps;

    @Column
    private Integer performedSets;

    @Column
    private BigDecimal usedWeight;
    
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;
}
