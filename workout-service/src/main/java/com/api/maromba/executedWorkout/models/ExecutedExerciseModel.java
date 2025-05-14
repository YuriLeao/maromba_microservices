package com.api.maromba.executedWorkout.models;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.api.maromba.executedWorkout.models.enums.ExecutionStatus;

import lombok.Data;

@Entity
@Table(name = "tb_executed_exercise")
@Data
public class ExecutedExerciseModel {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executed_division_id", nullable = false)
    private ExecutedDivisionModel executedDivision;

    @Column(nullable = false, columnDefinition = "UUID")
    private UUID exerciseId; 

    @Column(nullable = false)
    private Integer plannedReps;

    @Column(nullable = false)
    private Integer plannedSets;

    @Column(nullable = false)
    private Integer restTime;

    @Column(length = 300)
    private String notes;
    
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;
    
    @OneToMany(mappedBy = "executedExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecutedRepetitionModel> executedRepetitions;
}
