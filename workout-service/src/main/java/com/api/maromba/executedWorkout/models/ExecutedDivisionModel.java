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
import javax.validation.constraints.Size;

import com.api.maromba.executedWorkout.models.enums.ExecutionStatus;

import lombok.Data;

@Entity
@Table(name = "tb_executed_division")
@Data
public class ExecutedDivisionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false, length = 100)
    @Size(max = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executed_workout_id", nullable = false)
    private ExecutedWorkoutModel executedWorkout;
    
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    @OneToMany(mappedBy = "executedDivision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecutedExerciseModel> executedExercises;
    
}