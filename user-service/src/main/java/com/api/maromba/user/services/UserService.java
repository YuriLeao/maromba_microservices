package com.api.maromba.user.services;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

import com.api.maromba.user.controllers.UserController;
import com.api.maromba.user.dtos.UserDTO;
import com.api.maromba.user.exception.ResponseConflictException;
import com.api.maromba.user.exception.ResponseNotFoundException;
import com.api.maromba.user.models.UserModel;
import com.api.maromba.user.proxy.CompanyProxy;
import com.api.maromba.user.repositories.UserRepository;
import com.api.maromba.user.util.Encrypt;
import com.api.maromba.user.util.JwtUtil;

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

	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Transactional
	public UserDTO save(UserDTO userDTO) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		if (userRepository.existsByEmail(userDTO.getEmail())) {
			throw new ResponseConflictException("User already exists.");
		}
		var userModel = convertDTOToModel(userDTO);
		userModel.setPassword(criptor.encryptPassword(userModel.getEmail(), userModel.getPassword()));
		return convertModelToDTO(userRepository.save(userModel));
	}
	
	@Transactional
	public UserDTO update(UUID id, UserDTO userDTO) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Optional<UserModel> userModelOptional = userRepository.findById(id);
		if (!userModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No users found.");
		}

		UUID idemp = userModelOptional.get().getId();
		var userModel = convertDTOToModel(userDTO);
		userModel.setId(idemp);
		return convertModelToDTO(userRepository.save(userModel));
	}
	
	public UserDTO login(String email, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		password = criptor.encryptPassword(email, password);
		Optional<UserModel> userModelOptional = userRepository.findByEmailAndPassword(email, password);
		if (!userModelOptional.isPresent()) {
			throw new ResponseNotFoundException("User or password invalid.");
		}

		UserDTO userDTO = convertModelToDTO(userModelOptional.get());
		userDTO.setToken(jwtUtil.generateToken(userModelOptional.get(), "/user-service/login"));

		try {
			var response = companyProxy.getById(userDTO.getCompanyId());
			LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap<String, Object>) response.getBody();
			userDTO.setCompanyName(linkedHashMap.get("name").toString());
		} catch (Exception e) {
			logger.error("Error getting company name.");
		}
		
		return userDTO;
	}

	@Transactional
	public void delete(UUID id) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		Optional<UserModel> userModelOptional = userRepository.findById(id);
		if (!userModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No user found.");
		}
		
		userRepository.delete(userModelOptional.get());
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
	
	private UserModel convertDTOToModel(UserDTO userDTO) {
		var userModel = new UserModel();
		BeanUtils.copyProperties(userDTO, userModel);
		return userModel;
	}

	private UserDTO convertModelToDTO(UserModel userModel) {
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userModel, userDTO);
		userDTO.setPassword(null);
		return userDTO;
	}

}
