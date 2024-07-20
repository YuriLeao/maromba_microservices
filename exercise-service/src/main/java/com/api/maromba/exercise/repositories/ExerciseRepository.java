package com.api.maromba.exercise.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.exercise.models.ExerciseModel;

public interface ExerciseRepository extends JpaRepository<ExerciseModel, UUID>{
	
	boolean existsByName(String name);

}
