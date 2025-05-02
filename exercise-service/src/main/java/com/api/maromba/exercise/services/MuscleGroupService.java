package com.api.maromba.exercise.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.maromba.exercise.dtos.MuscleGroupDTO;
import com.api.maromba.exercise.exceptions.ResponseNotFoundException;
import com.api.maromba.exercise.models.MuscleGroupModel;
import com.api.maromba.exercise.repositories.MuscleGroupRepository;

@Service
public class MuscleGroupService {
	
	@Autowired
	MuscleGroupRepository muscleGroupRepository;
	
	public List<MuscleGroupDTO> getAll() {
		List<MuscleGroupModel> muscleGroupsModel = muscleGroupRepository.findAll();
		if (muscleGroupsModel == null || muscleGroupsModel.isEmpty()) {
			throw new ResponseNotFoundException("No muscle group found.");
		}

		List<MuscleGroupDTO> muscleGroupsDTO = new ArrayList<MuscleGroupDTO>();
		for (MuscleGroupModel genderrModel : muscleGroupsModel) {
			MuscleGroupDTO genderDTO = convertMuscleGroupModelToDTO(genderrModel);
			muscleGroupsDTO.add(genderDTO);
		}

		return muscleGroupsDTO;
	}

	
	private MuscleGroupDTO convertMuscleGroupModelToDTO(MuscleGroupModel muscleGroup) {
		MuscleGroupDTO muscleGroupDTO = new MuscleGroupDTO();
		BeanUtils.copyProperties(muscleGroup, muscleGroupDTO);
		return muscleGroupDTO;
	}

}
