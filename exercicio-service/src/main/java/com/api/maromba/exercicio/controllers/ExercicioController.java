package com.api.maromba.exercicio.controllers;

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

import com.api.maromba.exercicio.dtos.ExercicioDto;
import com.api.maromba.exercicio.exceptions.ResponseConflictException;
import com.api.maromba.exercicio.exceptions.ResponseNotFoundException;
import com.api.maromba.exercicio.models.ExercicioModel;
import com.api.maromba.exercicio.services.ExercicioService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Exercicio Service API endpoint")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/exercicio-service")
public class ExercicioController {
	
	@Autowired
	private ExercicioService exercicioService;
	
	@Operation(summary = "Salva uma novo exercicio .")
	@PostMapping("incluir")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> salvar(@RequestBody @Valid ExercicioDto exercicioDto){
		var exercicioModel = new ExercicioModel();
		BeanUtils.copyProperties(exercicioDto, exercicioModel);
		exercicioService.salvar(exercicioModel);
		return ResponseEntity.status(HttpStatus.CREATED).body("Criado com sucesso.");
	}
	
	@Operation(summary = "Obtém todas os exercicios .")
	@GetMapping
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<ExercicioDto>> obterTodos(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<ExercicioModel> exercicioPages = exercicioService.findAll(pageable);
		if(exercicioPages.isEmpty()) {
			throw new ResponseNotFoundException("Nenhum exercicio encontrado.");
		}
		List<ExercicioDto> exerciciosDto = new ArrayList<ExercicioDto>();
		for (ExercicioModel exercicio : exercicioPages) {
			ExercicioDto exercicioDto = new ExercicioDto();
			BeanUtils.copyProperties(exercicio, exercicioDto);
			exerciciosDto.add(exercicioDto);
		}
		Page<ExercicioDto> exercicioDtoPages = new PageImpl<ExercicioDto>(exerciciosDto);
		return ResponseEntity.status(HttpStatus.OK).body(exercicioDtoPages);
	}
	
	@Operation(summary = "Obtém um exercicio .")
	@GetMapping("obterById/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<ExercicioDto> obterById(@PathVariable(value = "id") UUID id){
		Optional<ExercicioModel> exercicioModelOptional = exercicioService.findById(id);
		if(!exercicioModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Exercicio não encontrado.");
		}
		ExercicioDto exercicioDto = new ExercicioDto();
		BeanUtils.copyProperties(exercicioModelOptional.get(), exercicioDto);
		return ResponseEntity.status(HttpStatus.OK).body(exercicioDto);
	}
	
	@Operation(summary = "Deleta um exercicio .")
	@DeleteMapping("deletar/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> deletar(@PathVariable(value = "id") UUID id){
		Optional<ExercicioModel> exercicioModelOptional = exercicioService.findById(id);
		if(!exercicioModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Exercicio não encontrado.");
		}
		exercicioService.deletar(exercicioModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Exercicio deletado com sucesso.");
	}
	
	@Operation(summary = "Altera uma exercicio .")
	@PutMapping("alterar/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> alterar(@PathVariable(value = "id") UUID id, @RequestBody ExercicioDto exercicioDto){
		Optional<ExercicioModel> exercicioModelOptional = exercicioService.findById(id);
		if(!exercicioModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Exercicio não encontrado.");
		}
		
		var exercicioModel = new ExercicioModel();
		UUID idemp = exercicioModelOptional.get().getId();
		BeanUtils.copyProperties(exercicioDto, exercicioModelOptional.get());
		exercicioModelOptional.get().setId(idemp);
		exercicioModel = exercicioService.salvar(exercicioModelOptional.get());
		BeanUtils.copyProperties(exercicioModel, exercicioDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(exercicioDto);
	}
}	
