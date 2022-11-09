package com.api.maromba.exercicioTreino.controllers;

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

import com.api.maromba.exercicioTreino.dtos.ExercicioTreinoDto;
import com.api.maromba.exercicioTreino.exceptions.ResponseConflictException;
import com.api.maromba.exercicioTreino.exceptions.ResponseNotFoundException;
import com.api.maromba.exercicioTreino.models.ExercicioTreinoModel;
import com.api.maromba.exercicioTreino.services.ExercicioTreinoService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Exercicio Treino Service API endpoint")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/exercicioTreino-service")
public class ExercicioTreinoController {
	
	@Autowired
	private ExercicioTreinoService exercicioTreinoService;
	
	@Operation(summary = "Salva uma novo exercicio treino.")
	@PostMapping("incluir")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> salvar(@RequestBody @Valid ExercicioTreinoDto exercicioTreinoDto){
		var exercicioTreinoModel = new ExercicioTreinoModel();
		BeanUtils.copyProperties(exercicioTreinoDto, exercicioTreinoModel);
		exercicioTreinoService.salvar(exercicioTreinoModel);
		return ResponseEntity.status(HttpStatus.CREATED).body("Criado com sucesso.");
	}
	
	@Operation(summary = "Obtém todas os exercicios treinos.")
	@GetMapping
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<ExercicioTreinoDto>> obterTodos(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<ExercicioTreinoModel> exercicioTreinoPages = exercicioTreinoService.findAll(pageable);
		if(exercicioTreinoPages.isEmpty()) {
			throw new ResponseNotFoundException("Nenhum exercicio treino encontrado.");
		}
		List<ExercicioTreinoDto> exerciciosTreinosDto = new ArrayList<ExercicioTreinoDto>();
		for (ExercicioTreinoModel exercicio : exercicioTreinoPages) {
			ExercicioTreinoDto exercicioTreinoDto = new ExercicioTreinoDto();
			BeanUtils.copyProperties(exercicio, exercicioTreinoDto);
			exerciciosTreinosDto.add(exercicioTreinoDto);
		}
		Page<ExercicioTreinoDto> exercicioDtoPages = new PageImpl<ExercicioTreinoDto>(exerciciosTreinosDto);
		return ResponseEntity.status(HttpStatus.OK).body(exercicioDtoPages);
	}
	
	@Operation(summary = "Obtém um exercicio treino.")
	@GetMapping("obterById/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<ExercicioTreinoDto> obterById(@PathVariable(value = "id") UUID id){
		Optional<ExercicioTreinoModel> exercicioTreinoModelOptional = exercicioTreinoService.findById(id);
		if(!exercicioTreinoModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Exercicio treino não encontrado.");
		}
		ExercicioTreinoDto exercicioTreinoDto = new ExercicioTreinoDto();
		BeanUtils.copyProperties(exercicioTreinoModelOptional.get(), exercicioTreinoDto);
		return ResponseEntity.status(HttpStatus.OK).body(exercicioTreinoDto);
	}
	
	@Operation(summary = "Deleta um exercicio treino.")
	@DeleteMapping("deletar/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> deletar(@PathVariable(value = "id") UUID id){
		Optional<ExercicioTreinoModel> exercicioTreinoModelOptional = exercicioTreinoService.findById(id);
		if(!exercicioTreinoModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Exercicio treino não encontrado.");
		}
		exercicioTreinoService.deletar(exercicioTreinoModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Exercicio treino deletado com sucesso.");
	}
	
	@Operation(summary = "Altera uma exercicio treino.")
	@PutMapping("alterar/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> alterar(@PathVariable(value = "id") UUID id, @RequestBody ExercicioTreinoDto exercicioTreinoDto){
		Optional<ExercicioTreinoModel> exercicioTreinoModelOptional = exercicioTreinoService.findById(id);
		if(!exercicioTreinoModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Exercicio treino não encontrado.");
		}
		
		var exercicioTreinoModel = new ExercicioTreinoModel();
		UUID idemp = exercicioTreinoModelOptional.get().getId();
		BeanUtils.copyProperties(exercicioTreinoDto, exercicioTreinoModelOptional.get());
		exercicioTreinoModelOptional.get().setId(idemp);
		exercicioTreinoModel = exercicioTreinoService.salvar(exercicioTreinoModelOptional.get());
		BeanUtils.copyProperties(exercicioTreinoModel, exercicioTreinoDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(exercicioTreinoDto);
	}
}	
