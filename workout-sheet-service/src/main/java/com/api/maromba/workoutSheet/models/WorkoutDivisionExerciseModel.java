package com.api.maromba.workoutSheet.models;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_workout_division_exercise")
@Data
@EqualsAndHashCode(of = "id")
public class WorkoutDivisionExerciseModel implements Serializable {

    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;
    
    @Column(nullable = false, columnDefinition = "UUID")
    private UUID exerciseId;

    @Column(nullable = false)
    private Integer reps;

    @Column(nullable = false)
    private Integer sets;

    @Column(name = "rest_time_seconds", nullable = false)
    private Integer restTimeSeconds;
    
    @Column(length = 500)
    @Size(max = 500)
    private String observation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id", nullable = false)
    private WorkoutDivisionModel division;
    
}