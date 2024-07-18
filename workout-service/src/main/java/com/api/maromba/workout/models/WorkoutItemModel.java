package com.api.maromba.workout.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_workout_item")
@Data
@EqualsAndHashCode(of = "id")
public class WorkoutItemModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(nullable = false)
	private UUID idExercise;
	@Column(nullable = false)
	private Integer reps;
	@Column(nullable = false)
	private Integer sets;
	@Column(nullable = true)
	private BigDecimal weight;
	@Column(nullable = true)
	private String observation;
	@Column(nullable = true)
	private BigDecimal recordWeight;
	@Column(nullable = false)
	private Integer coolDown;
	@ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private WorkoutModel workout;
	
}
