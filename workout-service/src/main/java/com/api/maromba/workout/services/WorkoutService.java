package com.api.maromba.workout.services;

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

import com.api.maromba.workout.dtos.WorkoutDTO;
import com.api.maromba.workout.dtos.WorkoutItemDTO;
import com.api.maromba.workout.exceptions.ResponseNotFoundException;
import com.api.maromba.workout.models.WorkoutItemModel;
import com.api.maromba.workout.models.WorkoutModel;
import com.api.maromba.workout.repositories.WorkoutRepository;

@Service
public class WorkoutService {
	
	@Autowired
	WorkoutRepository workoutRepository;
	
	@Transactional
	public WorkoutDTO save(WorkoutDTO workoutDTO) {
		var workoutModel = convertDTOToModel(workoutDTO);
		
        for (WorkoutItemModel item : workoutModel.getWorkoutItems()) {
            item.setWorkout(workoutModel);
        }
		
		return convertModelToDTO(workoutRepository.save(workoutModel));
		
	}
	
	@Transactional
	public WorkoutDTO update(UUID id, WorkoutDTO workoutDTO) {
		Optional<WorkoutModel> workoutModelOptional = workoutRepository.findById(id);
		if (!workoutModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No workout found.");
		}

		UUID idemp = workoutModelOptional.get().getId();
		BeanUtils.copyProperties(workoutDTO, workoutModelOptional.get());
		workoutModelOptional.get().setId(idemp);

		workoutModelOptional.get().getWorkoutItems().removeIf(item -> !workoutDTO.getWorkoutItems().contains(item));

		WorkoutItemModel workoutItemModel = new WorkoutItemModel();
		for (WorkoutItemDTO workoutItemDTO : workoutDTO.getWorkoutItems()) {
			BeanUtils.copyProperties(workoutItemDTO, workoutItemModel);
			workoutItemModel.setWorkout(workoutModelOptional.get());

			if (workoutModelOptional.get().getWorkoutItems().contains(workoutItemModel)) {
				WorkoutItemModel existingItem = workoutModelOptional.get().getWorkoutItems()
						.get(workoutModelOptional.get().getWorkoutItems().indexOf(workoutItemModel));
				BeanUtils.copyProperties(workoutItemModel, existingItem);
			} else {
				workoutModelOptional.get().getWorkoutItems().add(workoutItemModel);
			}
		}

		return convertModelToDTO(workoutRepository.save(workoutModelOptional.get()));
	}
	
	public WorkoutDTO getById(UUID id) {
		Optional<WorkoutModel> workoutModelOptional = workoutRepository.findById(id);
		if (!workoutModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No workout found.");
		}

		return convertModelToDTO(workoutModelOptional.get());
	}

	@Transactional
	public void delete(UUID id) {
		Optional<WorkoutModel> workoutModelOptional = workoutRepository.findById(id);
		if (!workoutModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No workout found.");
		}
		workoutRepository.delete(workoutModelOptional.get());
	}

	public Page<WorkoutDTO> getAll(Pageable pageable) {
		Page<WorkoutModel> workoutPages = workoutRepository.findAll(pageable);
		if (workoutPages.isEmpty()) {
			throw new ResponseNotFoundException("No workouts found.");
		}

		List<WorkoutDTO> workoutsDTO = new ArrayList<WorkoutDTO>();
		WorkoutDTO workoutDTO = new WorkoutDTO();
		for (WorkoutModel workout : workoutPages) {
			workoutDTO = convertModelToDTO(workout);

			workoutsDTO.add(workoutDTO);
		}

		return new PageImpl<WorkoutDTO>(workoutsDTO);
	}
	
	private WorkoutDTO convertModelToDTO(WorkoutModel workout) {
		WorkoutDTO workoutDTO;
		WorkoutItemDTO workoutItemDTO;
		workoutDTO = new WorkoutDTO();
		BeanUtils.copyProperties(workout, workoutDTO);
		workoutDTO.setWorkoutItems(new ArrayList<WorkoutItemDTO>());
		if (workout.getWorkoutItems() != null) {

			for (WorkoutItemModel workoutItemModel : workout.getWorkoutItems()) {
				workoutItemDTO = new WorkoutItemDTO();
				BeanUtils.copyProperties(workoutItemModel, workoutItemDTO);
				workoutDTO.getWorkoutItems().add(workoutItemDTO);
			}
		}
		return workoutDTO;
	}

	private WorkoutModel convertDTOToModel(WorkoutDTO workoutDTO) {
		var workoutModel = new WorkoutModel();
		BeanUtils.copyProperties(workoutDTO, workoutModel);

		WorkoutItemModel workoutItemModel = new WorkoutItemModel();
		workoutModel.setWorkoutItems(new ArrayList<WorkoutItemModel>());
		for (WorkoutItemDTO workoutItemDTO : workoutDTO.getWorkoutItems()) {
			BeanUtils.copyProperties(workoutItemDTO, workoutItemModel);
			workoutModel.getWorkoutItems().add(workoutItemModel);
		}
		return workoutModel;
	}

}
