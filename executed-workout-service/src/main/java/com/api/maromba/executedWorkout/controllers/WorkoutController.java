package com.api.maromba.executedWorkout.controllers;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.maromba.executedWorkout.dtos.ExecutedWorkoutDTO;
import com.api.maromba.executedWorkout.services.ExecutedWorkoutService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Workout Service API endpoint")
@RestController
@RequestMapping("/executed-workout-service")
public class WorkoutController {
	
	@Autowired
	private ExecutedWorkoutService workoutService;
	
	@Operation(summary = "Save a new workout.")
	@PostMapping("save")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<String> save(@RequestBody @Valid ExecutedWorkoutDTO workoutSheetDTO){
		workoutService.save(workoutSheetDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created.");
	}
	
	@Operation(summary = "Get a workout.")
	@GetMapping("getById/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<ExecutedWorkoutDTO> getById(@PathVariable(value = "id") UUID id){
		ExecutedWorkoutDTO workoutSheetDTO = workoutService.getById(id);
		return ResponseEntity.status(HttpStatus.OK).body(workoutSheetDTO);
	}
	
	@Operation(summary = "Delete a workout.")
	@DeleteMapping("delete/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<String> delete(@PathVariable(value = "id") UUID id){
		workoutService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body("Workout deleted successfully.");
	}
	
	@Operation(summary = "Update a workoutSheet.")
	@PutMapping("update/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<ExecutedWorkoutDTO> update(@PathVariable(value = "id") UUID id, @RequestBody ExecutedWorkoutDTO workoutSheetDTO){
		workoutSheetDTO = workoutService.update(id, workoutSheetDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(workoutSheetDTO);
	}
}	
