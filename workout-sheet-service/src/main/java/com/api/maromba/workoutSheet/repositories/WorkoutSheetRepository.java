package com.api.maromba.workoutSheet.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.maromba.workoutSheet.models.WorkoutSheetModel;

public interface WorkoutSheetRepository extends JpaRepository<WorkoutSheetModel, UUID> {

	@Query(value = "SELECT DISTINCT ws FROM WorkoutSheetModel ws " +
            "LEFT JOIN ws.divisions d " +
            "LEFT JOIN d.exercises",
     countQuery = "SELECT COUNT(DISTINCT ws) FROM WorkoutSheetModel ws")
	Page<WorkoutSheetModel> findAllPaginated(Pageable pageable);

	@EntityGraph(attributePaths = { "divisions" })
	Optional<WorkoutSheetModel> findById(UUID id);
}
