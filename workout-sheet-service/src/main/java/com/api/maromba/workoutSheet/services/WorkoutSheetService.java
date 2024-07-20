package com.api.maromba.workoutSheet.services;

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

import com.api.maromba.workoutSheet.dtos.WorkoutSheetDTO;
import com.api.maromba.workoutSheet.exceptions.ResponseNotFoundException;
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
		Optional<WorkoutSheetModel> workoutSheetModelOptional = workoutSheetRepository.findById(id);
		if(!workoutSheetModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No workout sheet found.");
		}
		
		UUID idemp = workoutSheetModelOptional.get().getId();
		var workoutSheetModel = convertDTOToModel(workoutSheetDTO);
		workoutSheetModel.setId(idemp);
		return convertModelToDTO(workoutSheetRepository.save(workoutSheetModel));
	}
	
	public WorkoutSheetDTO getById(UUID id) {
		Optional<WorkoutSheetModel> workoutSheetModelOptional = workoutSheetRepository.findById(id);
		if(!workoutSheetModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No workout sheet found.");
		}

		return convertModelToDTO(workoutSheetModelOptional.get());
	}

	@Transactional
	public void delete(UUID id) {
		Optional<WorkoutSheetModel> workoutSheetModelOptional = workoutSheetRepository.findById(id);
		if(!workoutSheetModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No workout sheet found.");
		}
		
		workoutSheetRepository.delete(workoutSheetModelOptional.get());
	}

	public Page<WorkoutSheetDTO> getAll(Pageable pageable) {
		Page<WorkoutSheetModel> workoutSheetPages = workoutSheetRepository.findAll(pageable);
		if(workoutSheetPages.isEmpty()) {
			throw new ResponseNotFoundException("No workout sheet found.");
		}
		List<WorkoutSheetDTO> workoutSheetsDTO = new ArrayList<WorkoutSheetDTO>();
		for (WorkoutSheetModel workoutSheet : workoutSheetPages) {
			var workoutSheetDTO = convertModelToDTO(workoutSheet);
			workoutSheetsDTO.add(workoutSheetDTO);
		}
		return new PageImpl<WorkoutSheetDTO>(workoutSheetsDTO);
	}
	
	private WorkoutSheetModel convertDTOToModel(WorkoutSheetDTO workoutSheetDTO) {
		var workoutSheetModel = new WorkoutSheetModel();
		BeanUtils.copyProperties(workoutSheetDTO, workoutSheetModel);
		return workoutSheetModel;
	}

	private WorkoutSheetDTO convertModelToDTO(WorkoutSheetModel workoutSheetModel) {
		WorkoutSheetDTO workoutSheetDTO = new WorkoutSheetDTO();
		BeanUtils.copyProperties(workoutSheetModel, workoutSheetDTO);
		return workoutSheetDTO;
	}

}
