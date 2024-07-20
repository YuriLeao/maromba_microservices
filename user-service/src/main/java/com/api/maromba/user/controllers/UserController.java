package com.api.maromba.user.controllers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.api.maromba.user.services.UserService;

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

	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Save a new user.")
	@PostMapping("save")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<String> save(@RequestBody @Valid UserDTO userDTO)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		userService.save(userDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created.");
	}

	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Gets all users.")
	@GetMapping("getAll")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<UserDTO>> getAll(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		Page<UserDTO> usersDTOPage = userService.getAll(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(usersDTOPage);
	}

	@Operation(summary = "Login.")
	@GetMapping("login/{email}/{password}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<UserDTO> login(@PathVariable(value = "email") String email,
			@PathVariable(value = "password") String password)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		UserDTO userDTO = userService.login(email, password);
		return ResponseEntity.status(HttpStatus.OK).body(userDTO);
	}

	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Get a list of users with part of their name.")
	@GetMapping("getByNameLike/{name}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<UserDTO>> getByNameLike(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
			@PathVariable(value = "name") String name) {
		Page<UserDTO> usersDTOPage = userService.getByNameLike(pageable, name);
		return ResponseEntity.status(HttpStatus.OK).body(usersDTOPage);
	}

	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Delete a user.")
	@DeleteMapping("delete/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<String> delete(@PathVariable(value = "id") UUID id)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		userService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
	}

	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Update a user.")
	@PutMapping("update/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<UserDTO> update(@PathVariable(value = "id") UUID id, @RequestBody UserDTO userDTO)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		userDTO = userService.update(id, userDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
	}

}
