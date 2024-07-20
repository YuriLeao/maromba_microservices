package com.api.maromba.workout.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.maromba.workout.models.WorkoutItemModel;

@Repository
public interface WorkoutItemRepository extends JpaRepository<WorkoutItemModel, UUID>{

}
