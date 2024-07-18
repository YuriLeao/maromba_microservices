package com.api.maromba.exercise.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

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

import com.api.maromba.exercise.dtos.ExerciseDTO;
import com.api.maromba.exercise.exceptions.ResponseConflictException;
import com.api.maromba.exercise.exceptions.ResponseNotFoundException;
import com.api.maromba.exercise.models.ExerciseModel;
import com.api.maromba.exercise.services.ExerciseService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Exercise Service API endpoint")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/exercise-service")
public class ExerciseController {
	
	@Autowired
	private ExerciseService exerciseService;
	
	@Operation(summary = "Save a new exercise.")
	@PostMapping("save")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> save(@RequestBody @Valid ExerciseDTO exerciseDTO){
		if(exerciseService.existsByName(exerciseDTO.getName())){
			throw new ResponseConflictException("Exercise already exists.");
		}
		var exerciseModel = new ExerciseModel();
		BeanUtils.copyProperties(exerciseDTO, exerciseModel);
		exerciseService.save(exerciseModel);
		return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created.");
	}
	
	@Operation(summary = "Gets all exercises.")
	@GetMapping("getAll")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<ExerciseDTO>> getAll(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<ExerciseModel> exercisePages = exerciseService.findAll(pageable);
		if(exercisePages.isEmpty()) {
			throw new ResponseNotFoundException("No exercise found.");
		}
		List<ExerciseDTO> exercisesDTO = new ArrayList<ExerciseDTO>();
		for (ExerciseModel exercise : exercisePages) {
			ExerciseDTO exerciseDTO = new ExerciseDTO();
			BeanUtils.copyProperties(exercise, exerciseDTO);
			exercisesDTO.add(exerciseDTO);
		}
		Page<ExerciseDTO> exerciseDTOPages = new PageImpl<ExerciseDTO>(exercisesDTO);
		return ResponseEntity.status(HttpStatus.OK).body(exerciseDTOPages);
	}
	
	@Operation(summary = "Get a exercise.")
	@GetMapping("getById/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<ExerciseDTO> getById(@PathVariable(value = "id") UUID id){
		Optional<ExerciseModel> exerciseModelOptional = exerciseService.findById(id);
		if(!exerciseModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No exercise found.");
		}
		ExerciseDTO exerciseDTO = new ExerciseDTO();
		BeanUtils.copyProperties(exerciseModelOptional.get(), exerciseDTO);
		return ResponseEntity.status(HttpStatus.OK).body(exerciseDTO);
	}
	
	@Operation(summary = "Delete a exercise.")
	@DeleteMapping("delete/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id){
		Optional<ExerciseModel> exerciseModelOptional = exerciseService.findById(id);
		if(!exerciseModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No exercise found.");
		}
		exerciseService.delete(exerciseModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Exercise deleted successfully.");
	}
	
	@Operation(summary = "Update a exercise.")
	@PutMapping("update/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> update(@PathVariable(value = "id") UUID id, @RequestBody ExerciseDTO exerciseDTO){
		Optional<ExerciseModel> exerciseModelOptional = exerciseService.findById(id);
		if(!exerciseModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No exercise found.");
		}
		
		var exerciseModel = new ExerciseModel();
		UUID idemp = exerciseModelOptional.get().getId();
		BeanUtils.copyProperties(exerciseDTO, exerciseModelOptional.get());
		exerciseModelOptional.get().setId(idemp);
		exerciseModel = exerciseService.save(exerciseModelOptional.get());
		BeanUtils.copyProperties(exerciseModel, exerciseDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(exerciseDTO);
	}
}	
