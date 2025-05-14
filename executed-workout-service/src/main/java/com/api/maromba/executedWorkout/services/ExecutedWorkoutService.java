package com.api.maromba.executedWorkout.services;

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
import org.springframework.stereotype.Service;

import com.api.maromba.executedWorkout.dtos.ExecutedDivisionDTO;
import com.api.maromba.executedWorkout.dtos.ExecutedExerciseDTO;
import com.api.maromba.executedWorkout.dtos.ExecutedRepetitionDTO;
import com.api.maromba.executedWorkout.dtos.ExecutedWorkoutDTO;
import com.api.maromba.executedWorkout.exceptions.ResponseNotFoundException;
import com.api.maromba.executedWorkout.models.ExecutedDivisionModel;
import com.api.maromba.executedWorkout.models.ExecutedExerciseModel;
import com.api.maromba.executedWorkout.models.ExecutedRepetitionModel;
import com.api.maromba.executedWorkout.models.ExecutedWorkoutModel;
import com.api.maromba.executedWorkout.repositories.ExecutedWorkoutRepository;

@Service
public class ExecutedWorkoutService {

	@Autowired
	ExecutedWorkoutRepository executedWorkoutRepository;

	@Transactional
	public ExecutedWorkoutDTO save(ExecutedWorkoutDTO workoutDTO) {
		var workoutModel = convertDTOToModel(workoutDTO);
		return convertModelToDTO(executedWorkoutRepository.save(workoutModel));
	}

	@Transactional
	public ExecutedWorkoutDTO update(UUID id, ExecutedWorkoutDTO workoutDTO) {
		ExecutedWorkoutModel existingWorkout = executedWorkoutRepository.findById(id)
				.orElseThrow(() -> new ResponseNotFoundException("No executed workout found."));

		UUID originalId = existingWorkout.getId();
		BeanUtils.copyProperties(workoutDTO, existingWorkout, "id", "divisions");
		existingWorkout.setId(originalId);

		processWorkoutDivisions(existingWorkout, workoutDTO.getDivisions());

		return convertModelToDTO(executedWorkoutRepository.save(existingWorkout));
	}

	private void processWorkoutDivisions(ExecutedWorkoutModel existingWorkout, List<ExecutedDivisionDTO> divisionDTOs) {

		Map<UUID, ExecutedDivisionModel> existingDivisionsMap = existingWorkout.getDivisions().stream()
				.collect(Collectors.toMap(ExecutedDivisionModel::getId, Function.identity()));

		List<ExecutedDivisionModel> updatedDivisions = IntStream.range(0, divisionDTOs.size()).mapToObj(i -> {
			ExecutedDivisionDTO divisionDTO = divisionDTOs.get(i);
			ExecutedDivisionModel divisionModel;

			if (divisionDTO.getId() != null && existingDivisionsMap.containsKey(divisionDTO.getId())) {
				divisionModel = existingDivisionsMap.get(divisionDTO.getId());
				BeanUtils.copyProperties(divisionDTO, divisionModel, "id", "exercises");
				processExercises(divisionModel, divisionDTO.getExecutedExercises());
			} else {
				divisionModel = new ExecutedDivisionModel();
				BeanUtils.copyProperties(divisionDTO, divisionModel, "id");
				divisionModel.setExecutedExercises(new ArrayList<>());
				processExercises(divisionModel, divisionDTO.getExecutedExercises());
				divisionModel.setExecutedWorkout(existingWorkout);
			}
			return divisionModel;
		}).collect(Collectors.toList());

		existingWorkout.getDivisions().clear();
		existingWorkout.getDivisions().addAll(updatedDivisions);
	}

	private void processExercises(ExecutedDivisionModel divisionModel, List<ExecutedExerciseDTO> exerciseDTOs) {

		Map<UUID, ExecutedExerciseModel> existingExercisesMap = divisionModel.getExecutedExercises().stream()
				.collect(Collectors.toMap(ExecutedExerciseModel::getId, Function.identity()));

		List<ExecutedExerciseModel> updatedExercises = IntStream.range(0, exerciseDTOs.size()).mapToObj(i -> {
			ExecutedExerciseDTO exerciseDTO = exerciseDTOs.get(i);
			ExecutedExerciseModel exerciseModel;

			if (exerciseDTO.getId() != null && existingExercisesMap.containsKey(exerciseDTO.getId())) {
				exerciseModel = existingExercisesMap.get(exerciseDTO.getId());
				BeanUtils.copyProperties(exerciseDTO, exerciseModel, "id");
				processRepetitions(exerciseModel, exerciseDTO.getExecutedRepetitions());
			} else {
				exerciseModel = new ExecutedExerciseModel();
				BeanUtils.copyProperties(exerciseDTO, exerciseModel, "id");
				exerciseModel.setExecutedRepetitions(new ArrayList<ExecutedRepetitionModel>());
				processRepetitions(exerciseModel, exerciseDTO.getExecutedRepetitions());
				exerciseModel.setExecutedDivision(divisionModel);
			}
			return exerciseModel;
		}).collect(Collectors.toList());

		divisionModel.getExecutedExercises().clear();
		divisionModel.getExecutedExercises().addAll(updatedExercises);
	}

	private void processRepetitions(ExecutedExerciseModel exerciseModel, List<ExecutedRepetitionDTO> repetitionsDTO) {

		Map<UUID, ExecutedRepetitionModel> existingRepetitionsMap = exerciseModel.getExecutedRepetitions().stream()
				.collect(Collectors.toMap(ExecutedRepetitionModel::getId, Function.identity()));

		List<ExecutedRepetitionModel> updatedRepetitions = IntStream.range(0, repetitionsDTO.size()).mapToObj(i -> {
			ExecutedRepetitionDTO repetitionDTO = repetitionsDTO.get(i);
			ExecutedRepetitionModel repetitionModel;

			if (repetitionDTO.getId() != null && existingRepetitionsMap.containsKey(repetitionDTO.getId())) {
				repetitionModel = existingRepetitionsMap.get(repetitionDTO.getId());
				BeanUtils.copyProperties(repetitionDTO, repetitionModel, "id");
			} else {
				repetitionModel = new ExecutedRepetitionModel();
				BeanUtils.copyProperties(repetitionDTO, repetitionModel, "id");
				repetitionModel.setExecutedExercise(exerciseModel);
			}
			return repetitionModel;
		}).collect(Collectors.toList());

		exerciseModel.getExecutedRepetitions().clear();
		exerciseModel.getExecutedRepetitions().addAll(updatedRepetitions);
	}

	public ExecutedWorkoutDTO getById(UUID id) {
		ExecutedWorkoutModel workout = executedWorkoutRepository.findById(id)
				.orElseThrow(() -> new ResponseNotFoundException("No executed workout found."));

		return convertModelToDTO(workout);
	}

	@Transactional
	public void delete(UUID id) {
		ExecutedWorkoutModel workout = executedWorkoutRepository.findById(id)
				.orElseThrow(() -> new ResponseNotFoundException("No executed workout found."));

		executedWorkoutRepository.delete(workout);
	}

	private ExecutedWorkoutDTO convertModelToDTO(ExecutedWorkoutModel workout) {
		ExecutedWorkoutDTO dto = new ExecutedWorkoutDTO();
		BeanUtils.copyProperties(workout, dto);

		dto.setDivisions(
				Optional.ofNullable(workout.getDivisions()).orElseGet(ArrayList::new).stream().map(division -> {
					ExecutedDivisionDTO divisionDTO = new ExecutedDivisionDTO();
					BeanUtils.copyProperties(division, divisionDTO);
					divisionDTO.setExecutedExercises(Optional.ofNullable(division.getExecutedExercises())
							.orElseGet(ArrayList::new).stream().map(exercise -> {
								ExecutedExerciseDTO exerciseDTO = new ExecutedExerciseDTO();
								BeanUtils.copyProperties(exercise, exerciseDTO);
								exerciseDTO
										.setExecutedRepetitions(Optional.ofNullable(exercise.getExecutedRepetitions())
												.orElseGet(ArrayList::new).stream().map(repetition -> {
													ExecutedRepetitionDTO repetitionDTO = new ExecutedRepetitionDTO();
													BeanUtils.copyProperties(repetition, repetitionDTO);
													return repetitionDTO;
												}).toList());
								return exerciseDTO;
							}).toList());
					return divisionDTO;
				}).toList());

		return dto;
	}

	private ExecutedWorkoutModel convertDTOToModel(ExecutedWorkoutDTO workoutDTO) {
		ExecutedWorkoutModel workoutModel = new ExecutedWorkoutModel();
		BeanUtils.copyProperties(workoutDTO, workoutModel);

		List<ExecutedDivisionModel> divisions = workoutDTO.getDivisions().stream().map(divisionDTO -> {
			ExecutedDivisionModel divisionModel = new ExecutedDivisionModel();
			BeanUtils.copyProperties(divisionDTO, divisionModel);
			divisionModel.setExecutedWorkout(workoutModel);

			List<ExecutedExerciseModel> exercises = divisionDTO.getExecutedExercises().stream().map(exerciseDTO -> {
				ExecutedExerciseModel exerciseModel = new ExecutedExerciseModel();
				BeanUtils.copyProperties(exerciseDTO, exerciseModel);
				exerciseModel.setExecutedDivision(divisionModel);

				List<ExecutedRepetitionModel> repetitions = exerciseDTO.getExecutedRepetitions().stream()
						.map(repetitionDTO -> {
							ExecutedRepetitionModel repetitionModel = new ExecutedRepetitionModel();
							BeanUtils.copyProperties(repetitionDTO, repetitionModel);
							repetitionModel.setExecutedExercise(exerciseModel);
							return repetitionModel;
						}).collect(Collectors.toList());

				exerciseModel.setExecutedRepetitions(repetitions);
				return exerciseModel;
			}).collect(Collectors.toList());

			divisionModel.setExecutedExercises(exercises);
			return divisionModel;
		}).collect(Collectors.toList());

		workoutModel.setDivisions(divisions);
		return workoutModel;
	}

}
