package com.api.maromba.treino.controllers;

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

import com.api.maromba.treino.dtos.TreinoDto;
import com.api.maromba.treino.exceptions.ResponseConflictException;
import com.api.maromba.treino.exceptions.ResponseNotFoundException;
import com.api.maromba.treino.models.TreinoModel;
import com.api.maromba.treino.services.TreinoService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Treino Service API endpoint")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/treino-service")
public class TreinoController {
	
	@Autowired
	private TreinoService treinoService;
	
	@Operation(summary = "Salva uma novo treino .")
	@PostMapping("incluir")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> salvar(@RequestBody @Valid TreinoDto treinoDto){
		var treinoModel = new TreinoModel();
		BeanUtils.copyProperties(treinoDto, treinoModel);
		treinoService.salvar(treinoModel);
		return ResponseEntity.status(HttpStatus.CREATED).body("Criado com sucesso.");
	}
	
	@Operation(summary = "Obtém tods os treinos.")
	@GetMapping
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<TreinoDto>> obterTodos(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<TreinoModel> treinoPages = treinoService.findAll(pageable);
		if(treinoPages.isEmpty()) {
			throw new ResponseNotFoundException("Nenhum treino encontrado.");
		}
		List<TreinoDto> treinosDto = new ArrayList<TreinoDto>();
		for (TreinoModel treino : treinoPages) {
			TreinoDto treinoDto = new TreinoDto();
			BeanUtils.copyProperties(treino, treinoDto);
			treinosDto.add(treinoDto);
		}
		Page<TreinoDto> treinoDtoPages = new PageImpl<TreinoDto>(treinosDto);
		return ResponseEntity.status(HttpStatus.OK).body(treinoDtoPages);
	}
	
	@Operation(summary = "Obtém um treino .")
	@GetMapping("obterById/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<TreinoDto> obterById(@PathVariable(value = "id") UUID id){
		Optional<TreinoModel> treinoModelOptional = treinoService.findById(id);
		if(!treinoModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Treino não encontrado.");
		}
		TreinoDto treinoDto = new TreinoDto();
		BeanUtils.copyProperties(treinoModelOptional.get(), treinoDto);
		return ResponseEntity.status(HttpStatus.OK).body(treinoDto);
	}
	
	@Operation(summary = "Deleta um treino .")
	@DeleteMapping("deletar/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> deletar(@PathVariable(value = "id") UUID id){
		Optional<TreinoModel> treinoModelOptional = treinoService.findById(id);
		if(!treinoModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Treino não encontrado.");
		}
		treinoService.deletar(treinoModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Treino deletado com sucesso.");
	}
	
	@Operation(summary = "Altera uma treino .")
	@PutMapping("alterar/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> alterar(@PathVariable(value = "id") UUID id, @RequestBody TreinoDto treinoDto){
		Optional<TreinoModel> treinoModelOptional = treinoService.findById(id);
		if(!treinoModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Treino não encontrado.");
		}
		
		var treinoModel = new TreinoModel();
		UUID idemp = treinoModelOptional.get().getId();
		BeanUtils.copyProperties(treinoDto, treinoModelOptional.get());
		treinoModelOptional.get().setId(idemp);
		treinoModel = treinoService.salvar(treinoModelOptional.get());
		BeanUtils.copyProperties(treinoModel, treinoDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(treinoDto);
	}
}	
