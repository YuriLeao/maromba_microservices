package com.api.maromba.workout.controllers;

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

import com.api.maromba.workout.dtos.WorkoutDTO;
import com.api.maromba.workout.exceptions.ResponseNotFoundException;
import com.api.maromba.workout.models.WorkoutModel;
import com.api.maromba.workout.services.WorkoutService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Workout Service API endpoint")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/workout-service")
public class WorkoutController {
	
	@Autowired
	private WorkoutService workoutService;
	
	@Operation(summary = "Save a new workout.")
	@PostMapping("include")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> include(@RequestBody @Valid WorkoutDTO workoutDTO){
		var workoutModel = new WorkoutModel();
		BeanUtils.copyProperties(workoutDTO, workoutModel);
		workoutService.save(workoutModel);
		return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created.");
	}
	
	@Operation(summary = "Gets all users.")
	@GetMapping("getAll")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<WorkoutDTO>> getAll(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<WorkoutModel> workoutPages = workoutService.findAll(pageable);
		if(workoutPages.isEmpty()) {
			throw new ResponseNotFoundException("No workout found.");
		}
		List<WorkoutDTO> workoutsDTO = new ArrayList<WorkoutDTO>();
		for (WorkoutModel workout : workoutPages) {
			WorkoutDTO workoutDTO = new WorkoutDTO();
			BeanUtils.copyProperties(workout, workoutDTO);
			workoutsDTO.add(workoutDTO);
		}
		Page<WorkoutDTO> workoutDTOPages = new PageImpl<WorkoutDTO>(workoutsDTO);
		return ResponseEntity.status(HttpStatus.OK).body(workoutDTOPages);
	}
	
	@Operation(summary = "Get one workout.")
	@GetMapping("getById/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<WorkoutDTO> getById(@PathVariable(value = "id") UUID id){
		Optional<WorkoutModel> workoutModelOptional = workoutService.findById(id);
		if(!workoutModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No workout found.");
		}
		WorkoutDTO workoutDTO = new WorkoutDTO();
		BeanUtils.copyProperties(workoutModelOptional.get(), workoutDTO);
		return ResponseEntity.status(HttpStatus.OK).body(workoutDTO);
	}
	
	@Operation(summary = "Delete a workout.")
	@DeleteMapping("delete/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id){
		Optional<WorkoutModel> workoutModelOptional = workoutService.findById(id);
		if(!workoutModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No workout found.");
		}
		workoutService.delete(workoutModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Workout deleted successfully.");
	}
	
	@Operation(summary = "Update a workout.")
	@PutMapping("update/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> update(@PathVariable(value = "id") UUID id, @RequestBody WorkoutDTO workoutDTO){
		Optional<WorkoutModel> workoutModelOptional = workoutService.findById(id);
		if(!workoutModelOptional.isPresent()) {
			throw new ResponseNotFoundException("No workout found.");
		}
		
		var workoutModel = new WorkoutModel();
		UUID idemp = workoutModelOptional.get().getId();
		BeanUtils.copyProperties(workoutDTO, workoutModelOptional.get());
		workoutModelOptional.get().setId(idemp);
		workoutModel = workoutService.save(workoutModelOptional.get());
		BeanUtils.copyProperties(workoutModel, workoutDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(workoutDTO);
	}
}	
