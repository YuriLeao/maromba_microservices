package com.api.maromba.user.services;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.user.dtos.AuthorizationDTO;
import com.api.maromba.user.dtos.GenderDTO;
import com.api.maromba.user.dtos.UserDTO;
import com.api.maromba.user.exception.ResponseConflictException;
import com.api.maromba.user.exception.ResponseNotFoundException;
import com.api.maromba.user.models.AuthorizationModel;
import com.api.maromba.user.models.GenderModel;
import com.api.maromba.user.models.UserModel;
import com.api.maromba.user.proxy.CompanyProxy;
import com.api.maromba.user.repositories.UserRepository;
import com.api.maromba.user.util.Encrypt;
import com.api.maromba.user.util.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Encrypt criptor;

	@Autowired
	private CompanyProxy companyProxy;

	@Autowired
	private JwtUtil jwtUtil;

	private Logger logger = LoggerFactory.getLogger(UserService.class);

	@Transactional
	public UserDTO save(UserDTO userDTO) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		if (userRepository.existsByEmail(userDTO.getEmail())) {
			throw new ResponseConflictException("User already exists.");
		}
		var userModel = convertDTOToModel(userDTO);
		userModel.setPassword(criptor.encryptPassword(userModel.getEmail(), "123#456@"));
		return convertModelToDTO(userRepository.save(userModel));
	}

	@Transactional
	public UserDTO update(UUID id, UserDTO userDTO) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		UserModel userModel = userRepository.findById(id)
				.orElseThrow(() -> new ResponseNotFoundException("No users found."));

		UUID idemp = userModel.getId();
		var userModelResponse = convertDTOToModel(userDTO);
		userModelResponse.setId(idemp);
		userModelResponse.setPassword(userModel.getPassword());
		return convertModelToDTO(userRepository.save(userModelResponse));
	}

	public UserDTO login(String email, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		password = criptor.encryptPassword(email, password);
		UserModel userModel = userRepository.findByEmailAndPassword(email, password)
				.orElseThrow(() -> new ResponseNotFoundException("User or password invalid."));

		UserDTO userDTO = convertModelToDTO(userModel);
		userDTO.setToken(jwtUtil.generateToken(userModel, "/user-service/login"));

		try {
			var response = companyProxy.getById(userDTO.getCompanyId());
			userDTO.setCompany(response.getBody());
		} catch (Exception e) {
			logger.error("Error getting company name.");
		}

		return userDTO;
	}

	@Transactional
	public void delete(UUID id) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		UserModel userModel = userRepository.findById(id)
				.orElseThrow(() -> new ResponseNotFoundException("No users found."));

		userRepository.delete(userModel);
	}

	public Page<UserDTO> getAll(Pageable pageable) {
		Page<UserModel> userPages = userRepository.findAll(pageable);
		if (userPages.isEmpty()) {
			throw new ResponseNotFoundException("No users found.");
		}

		List<UserDTO> usersDTO = new ArrayList<UserDTO>();
		for (UserModel userModel : userPages) {
			UserDTO userDTO = convertModelToDTO(userModel);
			usersDTO.add(userDTO);
		}

		return new PageImpl<UserDTO>(usersDTO);
	}

	public Page<UserDTO> getByCompanyIdAndInAuthorization(Pageable pageable, UUID companyId, String token) {
		List<AuthorizationModel> authorizations = authorizationValidation(token);

		Optional<AuthorizationModel> adminOptional = authorizations.stream().filter(auth -> "A".equals(auth.getId())).findFirst();
		Page<UserModel> userModels;
		
		if(adminOptional.isPresent()) {
			userModels = userRepository.findAll(pageable);
		} else {
			userModels = userRepository.findByCompanyIdAndInAuthorization(pageable, companyId,
					authorizations);
		}

		if (userModels == null || userModels.isEmpty()) {
			throw new ResponseNotFoundException("No users found.");
		}

		List<UserDTO> usersDTO = new ArrayList<UserDTO>();
		for (UserModel userModel : userModels) {
			UserDTO userDTO = convertModelToDTO(userModel);
			try {
				var response = companyProxy.getById(userDTO.getCompanyId());
				userDTO.setCompany(response.getBody());
			} catch (Exception e) {
				logger.error("Error getting company name.");
			}
			usersDTO.add(userDTO);
		}

		return new PageImpl<UserDTO>(usersDTO);
	}

	private List<AuthorizationModel> authorizationValidation(String token) {
		DecodedJWT decodedJWT = jwtUtil.validateToken(token.replace("Bearer ", ""), "/user-service/login");
		var authorizationId = decodedJWT.getClaim("authorization").asString();
		List<AuthorizationModel> authorizations = new ArrayList<AuthorizationModel>();
		AuthorizationModel authorizationModelAdmin = new AuthorizationModel();
		authorizationModelAdmin.setId("A");
		authorizationModelAdmin.setDescription("Admin");
		AuthorizationModel authorizationModelCompany = new AuthorizationModel();
		authorizationModelCompany.setId("E");
		authorizationModelCompany.setDescription("Empresa");
		AuthorizationModel authorizationModelTeacher= new AuthorizationModel();
		authorizationModelTeacher.setId("P");
		authorizationModelTeacher.setDescription("Professor");
		AuthorizationModel authorizationModelStudent = new AuthorizationModel();
		authorizationModelStudent.setId("AL");
		authorizationModelStudent.setDescription("AL");
		switch (authorizationId) {
		case "A": {
			authorizations.add(authorizationModelAdmin);
			authorizations.add(authorizationModelCompany);
			authorizations.add(authorizationModelTeacher);
			authorizations.add(authorizationModelStudent);
			break;
		}
		case "E": {
			authorizations.add(authorizationModelCompany);
			authorizations.add(authorizationModelTeacher);
			authorizations.add(authorizationModelStudent);
			break;
		}
		case "P": {
			authorizations.add(authorizationModelTeacher);
			authorizations.add(authorizationModelStudent);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + authorizationId);
		}
		return authorizations;
	}

	public Page<UserDTO> getByNameLike(Pageable pageable, String name) {
		Page<UserModel> userModels = userRepository.findByNameLike(pageable, name);
		if (userModels == null || userModels.isEmpty()) {
			throw new ResponseNotFoundException("No users found.");
		}

		List<UserDTO> usersDTO = new ArrayList<UserDTO>();
		for (UserModel userModel : userModels) {
			UserDTO userDTO = convertModelToDTO(userModel);
			usersDTO.add(userDTO);
		}

		return new PageImpl<UserDTO>(usersDTO);
	}

	private GenderModel convertGenderDTOToModel(GenderDTO genderDTO) {
		var genderModel = new GenderModel();
		BeanUtils.copyProperties(genderDTO, genderModel);
		return genderModel;
	}

	private GenderDTO convertGenderModelToDTO(GenderModel genderModel) {
		var genderDTO = new GenderDTO();
		BeanUtils.copyProperties(genderModel, genderDTO);
		return genderDTO;
	}

	private AuthorizationModel convertAuthorizationDTOToModel(AuthorizationDTO authorizationDTO) {
		var authorizationModel = new AuthorizationModel();
		BeanUtils.copyProperties(authorizationDTO, authorizationModel);
		return authorizationModel;
	}

	private AuthorizationDTO convertAuthorizationModelToDTO(AuthorizationModel authorizationModel) {
		var authorizationDTO = new AuthorizationDTO();
		BeanUtils.copyProperties(authorizationModel, authorizationDTO);
		return authorizationDTO;
	}

	private UserModel convertDTOToModel(UserDTO userDTO) {
		var userModel = new UserModel();
		BeanUtils.copyProperties(userDTO, userModel);
		userModel.setGender(convertGenderDTOToModel(userDTO.getGender()));
		userModel.setAuthorization(convertAuthorizationDTOToModel(userDTO.getAuthorization()));
		return userModel;
	}

	private UserDTO convertModelToDTO(UserModel userModel) {
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userModel, userDTO);
		userDTO.setGender(convertGenderModelToDTO(userModel.getGender()));
		userDTO.setAuthorization(convertAuthorizationModelToDTO(userModel.getAuthorization()));
		return userDTO;
	}

}
