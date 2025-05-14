package com.api.maromba.workoutSheet.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.workoutSheet.dtos.WorkoutDivisionDTO;
import com.api.maromba.workoutSheet.dtos.WorkoutExerciseDTO;
import com.api.maromba.workoutSheet.dtos.WorkoutSheetDTO;
import com.api.maromba.workoutSheet.exceptions.ResponseNotFoundException;
import com.api.maromba.workoutSheet.models.WorkoutExerciseModel;
import com.api.maromba.workoutSheet.models.WorkoutDivisionModel;
import com.api.maromba.workoutSheet.models.WorkoutSheetModel;
import com.api.maromba.workoutSheet.repositories.WorkoutSheetRepository;

@Service
public class WorkoutSheetService {

	@Autowired
	WorkoutSheetRepository workoutSheetRepository;

	@Transactional
	public WorkoutSheetDTO save(WorkoutSheetDTO workoutSheetDTO) {
		var workoutSheetModel = convertDTOToModel(workoutSheetDTO);

		return convertModelToDTO(workoutSheetRepository.save(workoutSheetModel));

	}

	@Transactional
	public WorkoutSheetDTO update(UUID id, WorkoutSheetDTO workoutSheetDTO) {
		WorkoutSheetModel existingSheet = workoutSheetRepository.findById(id)
				.orElseThrow(() -> new ResponseNotFoundException("No workout sheet found."));

		UUID originalId = existingSheet.getId();
		BeanUtils.copyProperties(workoutSheetDTO, existingSheet, "id", "divisions");
		existingSheet.setId(originalId);

		processWorkoutDivisionsWithStream(existingSheet, workoutSheetDTO.getDivisions());

		return convertModelToDTO(workoutSheetRepository.save(existingSheet));
	}

	private void processWorkoutDivisionsWithStream(WorkoutSheetModel existingSheet,
			List<WorkoutDivisionDTO> divisionDTOs) {

		Map<UUID, WorkoutDivisionModel> existingDivisionsMap = existingSheet.getDivisions().stream()
				.collect(Collectors.toMap(WorkoutDivisionModel::getId, Function.identity()));

		List<WorkoutDivisionModel> updatedDivisions = IntStream.range(0, divisionDTOs.size()).mapToObj(i -> {
			WorkoutDivisionDTO divisionDTO = divisionDTOs.get(i);
			WorkoutDivisionModel divisionModel;

			if (divisionDTO.getId() != null && existingDivisionsMap.containsKey(divisionDTO.getId())) {
				divisionModel = existingDivisionsMap.get(divisionDTO.getId());
				BeanUtils.copyProperties(divisionDTO, divisionModel, "id", "exercises");
				processExercisesWithStream(divisionModel, divisionDTO.getExercises());
			} else {
				divisionModel = new WorkoutDivisionModel();
				BeanUtils.copyProperties(divisionDTO, divisionModel, "id");
				divisionModel.setExercises(new ArrayList<>());
				processExercisesWithStream(divisionModel, divisionDTO.getExercises());
				divisionModel.setWorkoutSheet(existingSheet);
			}
			return divisionModel;
		}).collect(Collectors.toList());

		existingSheet.getDivisions().clear();
		existingSheet.getDivisions().addAll(updatedDivisions);
	}

	private void processExercisesWithStream(WorkoutDivisionModel divisionModel,
			List<WorkoutExerciseDTO> exerciseDTOs) {

		Map<UUID, WorkoutExerciseModel> existingExercisesMap = divisionModel.getExercises().stream()
				.collect(Collectors.toMap(WorkoutExerciseModel::getId, Function.identity()));

		List<WorkoutExerciseModel> updatedExercises = IntStream.range(0, exerciseDTOs.size()).mapToObj(i -> {
			WorkoutExerciseDTO exerciseDTO = exerciseDTOs.get(i);
			WorkoutExerciseModel exerciseModel;

			if (exerciseDTO.getId() != null && existingExercisesMap.containsKey(exerciseDTO.getId())) {
				exerciseModel = existingExercisesMap.get(exerciseDTO.getId());
				BeanUtils.copyProperties(exerciseDTO, exerciseModel, "id");
			} else {
				exerciseModel = new WorkoutExerciseModel();
				BeanUtils.copyProperties(exerciseDTO, exerciseModel, "id");
				exerciseModel.setDivision(divisionModel);
			}
			return exerciseModel;
		}).collect(Collectors.toList());

		divisionModel.getExercises().clear();
		divisionModel.getExercises().addAll(updatedExercises);
	}

	public WorkoutSheetDTO getById(UUID id) {
		WorkoutSheetModel workoutSheetModel = workoutSheetRepository.findById(id)
				.orElseThrow(() -> new ResponseNotFoundException("No workout sheet found."));

		return convertModelToDTO(workoutSheetModel);
	}

	@Transactional
	public void delete(UUID id) {
		WorkoutSheetModel workoutSheetModel = workoutSheetRepository.findById(id)
				.orElseThrow(() -> new ResponseNotFoundException("No workout sheet found."));

		workoutSheetRepository.delete(workoutSheetModel);
	}

	public Page<WorkoutSheetDTO> getAll(Pageable pageable) {
	    Page<WorkoutSheetModel> page = workoutSheetRepository.findAll(pageable);
	    if (page.isEmpty()) {
	        throw new ResponseNotFoundException("No workouts sheet found.");
	    }
	    return page.map(this::convertModelToDTO);
	}

	private WorkoutSheetDTO convertModelToDTO(WorkoutSheetModel workoutSheet) {
	    WorkoutSheetDTO dto = new WorkoutSheetDTO();
	    BeanUtils.copyProperties(workoutSheet, dto);
	    
	    dto.setDivisions(Optional.ofNullable(workoutSheet.getDivisions())
	        .orElseGet(ArrayList::new)
	        .stream()
	        .map(div -> {
	            WorkoutDivisionDTO divDTO = new WorkoutDivisionDTO();
	            BeanUtils.copyProperties(div, divDTO);
	            divDTO.setExercises(Optional.ofNullable(div.getExercises())
	                .orElseGet(ArrayList::new)
	                .stream()
	                .map(ex -> {
	                    WorkoutExerciseDTO exDTO = new WorkoutExerciseDTO();
	                    BeanUtils.copyProperties(ex, exDTO);
	                    return exDTO;
	                }).toList());
	            return divDTO;
	        }).toList());
	    
	    return dto;
	}

	private WorkoutSheetModel convertDTOToModel(WorkoutSheetDTO workoutSheetDTO) {
		WorkoutSheetModel workoutSheetModel = new WorkoutSheetModel();
		BeanUtils.copyProperties(workoutSheetDTO, workoutSheetModel);

		List<WorkoutDivisionModel> divisions = workoutSheetDTO.getDivisions().stream().map(divisionDTO -> {
			WorkoutDivisionModel divisionModel = new WorkoutDivisionModel();
			BeanUtils.copyProperties(divisionDTO, divisionModel);
			divisionModel.setWorkoutSheet(workoutSheetModel);

			List<WorkoutExerciseModel> exercises = divisionDTO.getExercises().stream().map(exerciseDTO -> {
				WorkoutExerciseModel exerciseModel = new WorkoutExerciseModel();
				BeanUtils.copyProperties(exerciseDTO, exerciseModel);
				exerciseModel.setDivision(divisionModel);
				return exerciseModel;
			}).collect(Collectors.toList());

			divisionModel.setExercises(exercises);
			return divisionModel;
		}).collect(Collectors.toList());

		workoutSheetModel.setDivisions(divisions);
		return workoutSheetModel;
	}

}
