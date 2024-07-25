package com.api.maromba.workout.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.maromba.workout.models.WorkoutItemModel;

public interface WorkoutItemRepository extends JpaRepository<WorkoutItemModel, UUID>{

}
