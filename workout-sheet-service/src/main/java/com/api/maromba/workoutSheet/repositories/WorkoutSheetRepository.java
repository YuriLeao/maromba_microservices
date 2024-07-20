package com.api.maromba.workoutSheet.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.workoutSheet.models.WorkoutSheetModel;

public interface WorkoutSheetRepository extends JpaRepository<WorkoutSheetModel, UUID>{


}
