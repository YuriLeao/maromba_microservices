package com.api.maromba.workoutSheet.services;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.workoutSheet.models.WorkoutSheetModel;
import com.api.maromba.workoutSheet.repositories.WorkoutSheetRepository;

@Service
public class WorkoutSheetService {
	
	@Autowired
	WorkoutSheetRepository exercicioRepository;
	
	@Transactional
	public WorkoutSheetModel save(WorkoutSheetModel exercicioModel) {
		return exercicioRepository.save(exercicioModel);
		
	}
	
	public Optional<WorkoutSheetModel> findById(UUID id) {
		return exercicioRepository.findById(id);
	}

	@Transactional
	public void delete(WorkoutSheetModel exercicioModel) {
		exercicioRepository.deleteById(exercicioModel.getId());
	}

	public Page<WorkoutSheetModel> findAll(Pageable pageable) {
		return exercicioRepository.findAll(pageable);
	}

}
