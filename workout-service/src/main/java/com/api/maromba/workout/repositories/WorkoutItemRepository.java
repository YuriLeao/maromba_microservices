package com.api.maromba.workout.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.maromba.workout.models.WorkoutItemModel;
import com.api.maromba.workout.models.WorkoutModel;

@Repository
public interface WorkoutItemRepository extends JpaRepository<WorkoutItemModel, UUID>{

}
