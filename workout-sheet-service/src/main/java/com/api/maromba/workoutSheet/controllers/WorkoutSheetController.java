package com.api.maromba.workoutSheet.controllers;

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

import com.api.maromba.workoutSheet.dtos.WorkoutSheetDTO;
import com.api.maromba.workoutSheet.services.WorkoutSheetService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Workout Sheet Service API endpoint")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/workout-sheet-service")
public class WorkoutSheetController {
	
	@Autowired
	private WorkoutSheetService workoutSheetService;
	
	@Operation(summary = "Save a new workout sheet.")
	@PostMapping("save")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<String> save(@RequestBody @Valid WorkoutSheetDTO workoutSheetDTO){
		workoutSheetService.save(workoutSheetDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created.");
	}
	
	@Operation(summary = "Get all the workout sheets.")
	@GetMapping("getAll")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<WorkoutSheetDTO>> getAll(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<WorkoutSheetDTO> workoutSheetDTOPage = workoutSheetService.getAll(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(workoutSheetDTOPage);
	}
	
	@Operation(summary = "Get a workout sheet.")
	@GetMapping("getById/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<WorkoutSheetDTO> getById(@PathVariable(value = "id") UUID id){
		WorkoutSheetDTO workoutSheetDTO = workoutSheetService.getById(id);
		return ResponseEntity.status(HttpStatus.OK).body(workoutSheetDTO);
	}
	
	@Operation(summary = "Delete a workout sheet.")
	@DeleteMapping("delete/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<String> delete(@PathVariable(value = "id") UUID id){
		workoutSheetService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body("Workout sheet deleted successfully.");
	}
	
	@Operation(summary = "Update a workoutSheet.")
	@PutMapping("update/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<WorkoutSheetDTO> update(@PathVariable(value = "id") UUID id, @RequestBody WorkoutSheetDTO workoutSheetDTO){
		workoutSheetDTO = workoutSheetService.update(id, workoutSheetDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(workoutSheetDTO);
	}
}	
