package com.api.maromba.workoutSheet.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "tb_workout_sheet")
@Data
public class WorkoutSheetModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(nullable = false)
	private UUID idUser;
	@Column(nullable = false)
	private UUID idWorkout;
	@Column(nullable = false)
	private LocalDate dateRegister;
	@Column(nullable = false)
	private Boolean active;
	@Column(nullable = false)
	private String name;

}
