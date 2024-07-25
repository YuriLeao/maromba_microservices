package com.api.maromba.workout.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.api.maromba.workout.models.WorkoutModel;

public interface WorkoutRepository extends JpaRepository<WorkoutModel, UUID>{
	
	@EntityGraph(attributePaths = "workoutItems")
	Page<WorkoutModel> findAll(@Param("pageable")  Pageable pageable);

}
