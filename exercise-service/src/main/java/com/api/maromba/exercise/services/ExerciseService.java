package com.api.maromba.exercise.services;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.exercise.models.ExerciseModel;
import com.api.maromba.exercise.repositories.ExerciseRepository;

@Service
public class ExerciseService {
	
	@Autowired
	ExerciseRepository exerciseRepository;
	
	@Transactional
	public ExerciseModel save(ExerciseModel exerciseModel) {
		return exerciseRepository.save(exerciseModel);
		
	}
	
	public boolean existsByName(String name) {
		return exerciseRepository.existsByName(name);
	}
	
	public Optional<ExerciseModel> findById(UUID id) {
		return exerciseRepository.findById(id);
	}

	public Optional<ExerciseModel> findByName(String name) {
		return exerciseRepository.findByName(name);
	}

	@Transactional
	public void delete(ExerciseModel exerciseModel) {
		exerciseRepository.deleteById(exerciseModel.getId());
	}

	public Page<ExerciseModel> findAll(Pageable pageable) {
		return exerciseRepository.findAll(pageable);
	}

}
