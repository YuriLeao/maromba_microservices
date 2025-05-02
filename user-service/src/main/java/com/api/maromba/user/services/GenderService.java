package com.api.maromba.user.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.maromba.user.dtos.GenderDTO;
import com.api.maromba.user.exception.ResponseNotFoundException;
import com.api.maromba.user.models.GenderModel;
import com.api.maromba.user.repositories.GenderRepository;

@Service
public class GenderService {

	@Autowired
	private GenderRepository genderRepository;

		public List<GenderDTO> getAll() {
		List<GenderModel> genderModels = genderRepository.findAll();
		if (genderModels == null || genderModels.isEmpty()) {
			throw new ResponseNotFoundException("No genders found.");
		}

		List<GenderDTO> gendersDTO = new ArrayList<GenderDTO>();
		for (GenderModel genderrModel : genderModels) {
			GenderDTO genderDTO = convertGenderModelToDTO(genderrModel);
			gendersDTO.add(genderDTO);
		}

		return gendersDTO;
	}

	private GenderDTO convertGenderModelToDTO(GenderModel genderModel) {
		var genderDTO = new GenderDTO();
		BeanUtils.copyProperties(genderModel, genderDTO);
		return genderDTO;
	}

}
