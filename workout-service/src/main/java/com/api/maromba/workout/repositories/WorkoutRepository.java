package com.api.maromba.workout.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.workout.models.WorkoutModel;

public interface WorkoutRepository extends JpaRepository<WorkoutModel, UUID>{

	void deleteById(UUID id);

}
