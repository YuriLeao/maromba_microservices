package com.api.maromba.exercise.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.maromba.exercise.models.ExerciseModel;

public interface ExerciseRepository extends JpaRepository<ExerciseModel, UUID>{
	
	boolean existsByName(String name);
	
    @Query("SELECT e FROM ExerciseModel e WHERE e.companyId = :companyId OR e.companyId IS NULL")
    Page<ExerciseModel> findAllByCompanyId(@Param("companyId") UUID companyId, Pageable pageable);

}
