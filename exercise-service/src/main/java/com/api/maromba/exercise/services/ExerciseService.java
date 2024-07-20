package com.api.maromba.exercise.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.exercise.dtos.ExerciseDTO;
import com.api.maromba.exercise.exceptions.ResponseConflictException;
import com.api.maromba.exercise.exceptions.ResponseNotFoundException;
import com.api.maromba.exercise.models.ExerciseModel;
import com.api.maromba.exercise.repositories.ExerciseRepository;

@Service
public class ExerciseService {
	
	@Autowired
	ExerciseRepository exerciseRepository;
	
	@Transactional
	public ExerciseDTO save(ExerciseDTO exerciseDTO) {
		if(exerciseRepository.existsByName(exerciseDTO.getName())){
			throw new ResponseConflictException("Exercise already exists.");
		}
		var exerciseModel = convertDTOToModel(exerciseDTO);
		return convertModelToDTO(exerciseRepository.save(exerciseModel));
	}
	
	@Transactional
	public ExerciseDTO update(UUID id, ExerciseDTO exerciseDTO) {
		Optional<ExerciseModel> exerciseModelOptional = exerciseRepository.findById(id);
		if(!exerciseModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No exercise found.");
		}
		
		UUID idemp = exerciseModelOptional.get().getId();
		var exerciseModel = convertDTOToModel(exerciseDTO);
		exerciseModel.setId(idemp);
		exerciseModel = exerciseRepository.save(exerciseModel);
		return convertModelToDTO(exerciseModel);
	}
	
	public ExerciseDTO getById(UUID id) {
		Optional<ExerciseModel> exerciseModelOptional = exerciseRepository.findById(id);
		if(!exerciseModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No exercise found.");
		}
		return convertModelToDTO(exerciseModelOptional.get());
	}

	@Transactional
	public void delete(UUID id) {
		Optional<ExerciseModel> exerciseModelOptional = exerciseRepository.findById(id);
		if(!exerciseModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No exercise found.");
		}
		exerciseRepository.delete(exerciseModelOptional.get());
	}

	public Page<ExerciseDTO> getAll(Pageable pageable) {
		Page<ExerciseModel> exercisePages = exerciseRepository.findAll(pageable);
		if(exercisePages.isEmpty()) {
			throw new ResponseNotFoundException("No exercise found.");
		}
		List<ExerciseDTO> exercisesDTO = new ArrayList<ExerciseDTO>();
		for (ExerciseModel exercise : exercisePages) {
			ExerciseDTO exerciseDTO = convertModelToDTO(exercise);
			exercisesDTO.add(exerciseDTO);
		}
		return new PageImpl<ExerciseDTO>(exercisesDTO);
	}
	
	private ExerciseModel convertDTOToModel(ExerciseDTO exerciseDTO) {
		var exerciseModel = new ExerciseModel();
		BeanUtils.copyProperties(exerciseDTO, exerciseModel);
		return exerciseModel;
	}
	
	private ExerciseDTO convertModelToDTO(ExerciseModel exercise) {
		ExerciseDTO exerciseDTO = new ExerciseDTO();
		BeanUtils.copyProperties(exercise, exerciseDTO);
		return exerciseDTO;
	}

}
