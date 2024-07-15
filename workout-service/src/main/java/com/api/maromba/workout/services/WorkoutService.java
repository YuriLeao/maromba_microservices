package com.api.maromba.workout.services;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.workout.models.WorkoutModel;
import com.api.maromba.workout.repositories.WorkoutRepository;

@Service
public class WorkoutService {
	
	@Autowired
	WorkoutRepository workoutRepository;
	
	@Transactional
	public WorkoutModel save(WorkoutModel treinoModel) {
		return workoutRepository.save(treinoModel);
		
	}
	
	public Optional<WorkoutModel> findById(UUID id) {
		return workoutRepository.findById(id);
	}

	@Transactional
	public void delete(WorkoutModel workoutModel) {
		workoutRepository.deleteById(workoutModel.getId());
	}

	public Page<WorkoutModel> findAll(Pageable pageable) {
		return workoutRepository.findAll(pageable);
	}

}
