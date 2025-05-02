package com.api.maromba.exercise.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.exercise.dtos.ExerciseDTO;
import com.api.maromba.exercise.dtos.MuscleGroupDTO;
import com.api.maromba.exercise.exceptions.ResponseConflictException;
import com.api.maromba.exercise.exceptions.ResponseNotFoundException;
import com.api.maromba.exercise.models.ExerciseModel;
import com.api.maromba.exercise.models.MuscleGroupModel;
import com.api.maromba.exercise.repositories.ExerciseRepository;

@Service
public class ExerciseService {

	@Autowired
	ExerciseRepository exerciseRepository;

	@Transactional
	public ExerciseDTO save(ExerciseDTO exerciseDTO) {
		if (exerciseRepository.existsByName(exerciseDTO.getName())) {
			throw new ResponseConflictException("Exercise already exists.");
		}
		var exerciseModel = convertDTOToModel(exerciseDTO);
		return convertModelToDTO(exerciseRepository.save(exerciseModel));
	}

	@Transactional
	public ExerciseDTO update(UUID id, ExerciseDTO exerciseDTO) {
		ExerciseModel exerciseModel = exerciseRepository.findById(id)
				.orElseThrow(() -> new ResponseNotFoundException("No exercise found."));

		UUID idemp = exerciseModel.getId();
		exerciseModel = convertDTOToModel(exerciseDTO);
		exerciseModel.setId(idemp);
		exerciseModel = exerciseRepository.save(exerciseModel);
		return convertModelToDTO(exerciseModel);
	}

	public ExerciseDTO getById(UUID id) {
		ExerciseModel exerciseModel = exerciseRepository.findById(id)
				.orElseThrow(() -> new ResponseNotFoundException("No exercise found."));

		return convertModelToDTO(exerciseModel);
	}

	@Transactional
	public void delete(UUID id) {
		ExerciseModel exerciseModel = exerciseRepository.findById(id)
				.orElseThrow(() -> new ResponseNotFoundException("No exercise found."));

		exerciseRepository.delete(exerciseModel);
	}

	public Page<ExerciseDTO> getAll(Pageable pageable) {
		Page<ExerciseModel> exercisePages = exerciseRepository.findAll(pageable);
		if (exercisePages.isEmpty()) {
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
		exerciseModel.setMuscleGroup(convertMuscleGroupDTOToModel(exerciseDTO.getMuscleGroup()));
		return exerciseModel;
	}

	private ExerciseDTO convertModelToDTO(ExerciseModel exercise) {
		ExerciseDTO exerciseDTO = new ExerciseDTO();
		BeanUtils.copyProperties(exercise, exerciseDTO);
		exerciseDTO.setMuscleGroup(convertMuscleGroupModelToDTO(exercise.getMuscleGroup()));
		return exerciseDTO;
	}
	
	private MuscleGroupDTO convertMuscleGroupModelToDTO(MuscleGroupModel muscleGroup) {
		MuscleGroupDTO muscleGroupDTO = new MuscleGroupDTO();
		BeanUtils.copyProperties(muscleGroup, muscleGroupDTO);
		return muscleGroupDTO;
	}
	
	private MuscleGroupModel convertMuscleGroupDTOToModel(MuscleGroupDTO muscleGroupDTO) {
		MuscleGroupModel muscleGroupModel = new MuscleGroupModel();
		BeanUtils.copyProperties(muscleGroupDTO, muscleGroupModel);
		return muscleGroupModel;
	}

}
