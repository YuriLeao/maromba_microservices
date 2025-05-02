package com.api.maromba.exercise.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.maromba.exercise.dtos.MuscleGroupDTO;
import com.api.maromba.exercise.services.MuscleGroupService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Muscle Group Service API endpoint")
@RestController
@RequestMapping("/muscleGroup-service")
public class MuscleGroupController {
	
	@Autowired
	private MuscleGroupService muscleGroupService;
	
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Gets all muscle groups.")
	@GetMapping("getAll")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<List<MuscleGroupDTO>> getAll() {
		List<MuscleGroupDTO> muscleGroupDTO = muscleGroupService.getAll();
		return ResponseEntity.status(HttpStatus.OK).body(muscleGroupDTO);
	}
	
}	
