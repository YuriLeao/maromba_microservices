package com.api.maromba.user.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.maromba.user.dtos.AuthorizationDTO;
import com.api.maromba.user.services.AuthorizationService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authorization Service API")
@RestController
@RequestMapping("/authorization-service")
public class AuthorizationController {

	@Autowired
	private AuthorizationService authorizationService;

	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Gets all authorizations.")
	@GetMapping("getAll")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<List<AuthorizationDTO>> getAll() {
		List<AuthorizationDTO> authorizationsDTO = authorizationService.getAll();
		return ResponseEntity.status(HttpStatus.OK).body(authorizationsDTO);
	}

}
