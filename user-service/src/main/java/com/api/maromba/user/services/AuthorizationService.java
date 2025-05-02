package com.api.maromba.user.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.maromba.user.dtos.AuthorizationDTO;
import com.api.maromba.user.exception.ResponseNotFoundException;
import com.api.maromba.user.models.AuthorizationModel;
import com.api.maromba.user.repositories.AuthorizationRepository;

@Service
public class AuthorizationService {
	
	@Autowired
	private AuthorizationRepository authorizationRepository;
	
	public List<AuthorizationDTO> getAll() {
		List<AuthorizationModel> authorizationsModel = authorizationRepository.findAll();
		if (authorizationsModel == null || authorizationsModel.isEmpty()) {
			throw new ResponseNotFoundException("No authorizations found.");
		}

		List<AuthorizationDTO> authorizationsDTO = new ArrayList<AuthorizationDTO>();
		for (AuthorizationModel authorizationModel : authorizationsModel) {
			AuthorizationDTO authorizationDTO = convertAuthorizationModelToDTO(authorizationModel);
			authorizationsDTO.add(authorizationDTO);
		}

		return authorizationsDTO;
	}

	private AuthorizationDTO convertAuthorizationModelToDTO(AuthorizationModel authorizationModel) {
		var authorizationDTO = new AuthorizationDTO();
		BeanUtils.copyProperties(authorizationModel, authorizationDTO);
		return authorizationDTO;
	}

}
