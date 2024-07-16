package com.api.maromba.user.controllers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.maromba.user.dtos.UserDTO;
import com.api.maromba.user.exception.ResponseConflictException;
import com.api.maromba.user.exception.ResponseNotFoundException;
import com.api.maromba.user.models.UserModel;
import com.api.maromba.user.proxy.CompanyProxy;
import com.api.maromba.user.services.UserService;
import com.api.maromba.user.util.JwtUtil;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Service API")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user-service")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CompanyProxy companyProxy;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Save a new user.")
	@PostMapping("save")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> save(@RequestBody @Valid UserDTO userDTO) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		if(userService.existsByEmail(userDTO.getEmail())){
			throw new ResponseConflictException("User already exists.");
		}
		var userModel = new UserModel();
		BeanUtils.copyProperties(userDTO, userModel);
		userService.save(userModel);
		return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created.");
	}
	
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Gets all users.")
	@GetMapping("getAll")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<UserDTO>> getAll(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<UserModel> userPages = userService.findAll(pageable);
		if(userPages.isEmpty()) {
			throw new ResponseNotFoundException("No users found.");
		}
		List<UserDTO> usersDTO = new ArrayList<UserDTO>();
		for (UserModel userModel : userPages) {
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(userModel, userDTO);
			userDTO.setPassword(null);
			usersDTO.add(userDTO);
		}
		Page<UserDTO> userDTOPages = new PageImpl<UserDTO>(usersDTO);
		return ResponseEntity.status(HttpStatus.OK).body(userDTOPages);
	}
	
	@Operation(summary = "Login.")
	@GetMapping("login/{email}/{password}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<UserDTO> login(@PathVariable(value = "email") String email, @PathVariable(value = "password") String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		Optional<UserModel> userModelOptional = userService.findByEmailAndPassword(email, password);
		if(!userModelOptional.isPresent()) {
			throw new ResponseNotFoundException("User or password invalid.");
		}
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userModelOptional.get(), userDTO);
		try {
			var response = companyProxy.getById(userDTO.getCompanyId());
			LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap<String, Object>)response.getBody();
			userDTO.setCompanyName(linkedHashMap.get("name").toString());
		} catch (Exception e) {
			logger.error("Error getting company name.");
		}
		userDTO.setToken(jwtUtil.generateToken(userModelOptional.get(), "/user-service/login"));
		userDTO.setPassword(null);
		return ResponseEntity.status(HttpStatus.OK).body(userDTO);
	}
	
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Get a list of users with part of their name.")
	@GetMapping("getByNameLike/{name}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<List<UserDTO>> getByNameLike(@PathVariable(value = "name") String name){
		List<UserModel> userModels = userService.findByNameLike(name);
		if(userModels == null || userModels.isEmpty()) {
			throw new ResponseNotFoundException("No users found.");
		}
		List<UserDTO> userDTOs = new ArrayList<UserDTO>();
		for (UserModel userModel : userModels) {
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(userModel, userDTO);
			userDTO.setPassword(null);
			userDTOs.add(userDTO);
		}
		return ResponseEntity.status(HttpStatus.OK).body(userDTOs);
	}
	
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Delete a user.")
	@DeleteMapping("delete/{email}/{password}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> delete(@PathVariable(value = "email") String email, @PathVariable(value = "password") String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		Optional<UserModel> userModelOptional = userService.findByEmailAndPassword(email, password);
		if(!userModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No users found.");
		}
		userService.delete(userModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
	}
	
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Update a user.")
	@PutMapping("update/{email}/{password}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<UserDTO> update(@PathVariable(value = "email") String email, @PathVariable(value = "password") String password,
			@RequestBody UserDTO userDTO) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		Optional<UserModel> userModelOptional = userService.findByEmailAndPassword(email, password);
		if(!userModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No users found.");
		}
		var userModel = new UserModel();
		UUID id = userModelOptional.get().getId();
		BeanUtils.copyProperties(userDTO, userModelOptional.get());
		userModelOptional.get().setId(id);
		userModel = userService.save(userModelOptional.get());
		BeanUtils.copyProperties(userModel, userDTO);
		userDTO.setPassword(null);
		return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
	}
}	
