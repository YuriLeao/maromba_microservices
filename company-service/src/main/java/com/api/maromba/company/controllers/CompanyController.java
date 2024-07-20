package com.api.maromba.company.controllers;

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

import com.api.maromba.company.dtos.CompanyDTO;
import com.api.maromba.company.services.CompanyService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Company Service API endpoint")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/company-service")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	@Operation(summary = "Save a new company.")
	@PostMapping("save")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<String> save(@RequestBody @Valid CompanyDTO companyDTO){
		companyService.save(companyDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created.");
	}
	
	@Operation(summary = "Get all the companys.")
	@GetMapping("getAll")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<CompanyDTO>> getAll(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<CompanyDTO> companysDTOPages = companyService.getAll(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(companysDTOPages);
	}
	
	@Operation(summary = "Get a company.")
	@GetMapping("getById/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<CompanyDTO> getById(@PathVariable(value = "id") UUID id){
		CompanyDTO companyDTO = companyService.getById(id);
		return ResponseEntity.status(HttpStatus.OK).body(companyDTO);
	}
	
	@Operation(summary = "Delete a company.")
	@DeleteMapping("delete/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<String> delete(@PathVariable(value = "id") UUID id){
		companyService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body("Company deleted successfully.");
	}
	
	@Operation(summary = "Update a company.")
	@PutMapping("update/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<CompanyDTO> update(@PathVariable(value = "id") UUID id, @RequestBody CompanyDTO companyDTO){
		companyDTO = companyService.update(id, companyDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(companyDTO);
	}
	
	
}	
