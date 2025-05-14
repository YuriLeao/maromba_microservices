package com.api.maromba.executedWorkout.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.executedWorkout.models.ExecutedWorkoutModel;

public interface ExecutedWorkoutRepository extends JpaRepository<ExecutedWorkoutModel, UUID>{

	@EntityGraph(attributePaths = { "divisions" })
	Optional<ExecutedWorkoutModel> findById(UUID id);
	
}
