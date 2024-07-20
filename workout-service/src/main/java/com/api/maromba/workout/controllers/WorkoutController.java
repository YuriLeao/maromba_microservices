package com.api.maromba.workout.controllers;

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

import com.api.maromba.workout.dtos.WorkoutDTO;
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
	@PostMapping("save")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<String> save(@RequestBody @Valid WorkoutDTO workoutDTO) {
		workoutService.save(workoutDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created.");
	}

	@Operation(summary = "Gets all users.")
	@GetMapping("getAll")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<WorkoutDTO>> getAll(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		Page<WorkoutDTO> workoutsDTOPage = workoutService.getAll(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(workoutsDTOPage);
	}

	@Operation(summary = "Get a workout.")
	@GetMapping("getById/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<WorkoutDTO> getById(@PathVariable(value = "id") UUID id) {
		WorkoutDTO workoutDTO = workoutService.getById(id);
		return ResponseEntity.status(HttpStatus.OK).body(workoutDTO);
	}

	@Operation(summary = "Delete a workout.")
	@DeleteMapping("delete/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<String> delete(@PathVariable(value = "id") UUID id) {
		workoutService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body("Workout deleted successfully.");
	}

	@Operation(summary = "Update a workout.")
	@PutMapping("update/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<WorkoutDTO> update(@PathVariable(value = "id") UUID id, @RequestBody WorkoutDTO workoutDTO) {
		workoutDTO = workoutService.update(id, workoutDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(workoutDTO);
	}
}
