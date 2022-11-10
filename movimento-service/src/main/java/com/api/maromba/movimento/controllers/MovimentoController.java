package com.api.maromba.movimento.controllers;

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

import com.api.maromba.movimento.dtos.MovimentoDto;
import com.api.maromba.movimento.exceptions.ResponseConflictException;
import com.api.maromba.movimento.exceptions.ResponseNotFoundException;
import com.api.maromba.movimento.models.MovimentoModel;
import com.api.maromba.movimento.services.MovimentoService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Movimento Service API endpoint")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/movimento-service")
public class MovimentoController {
	
	@Autowired
	private MovimentoService movimentoService;
	
	@Operation(summary = "Salva um novo movimento.")
	@PostMapping("incluir")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> salvar(@RequestBody @Valid MovimentoDto movimentoDto){
		if(movimentoService.existe(movimentoDto.getNome())){
			throw new ResponseConflictException("Movimento já existente.");
		}
		var movimentoModel = new MovimentoModel();
		BeanUtils.copyProperties(movimentoDto, movimentoModel);
		movimentoService.salvar(movimentoModel);
		return ResponseEntity.status(HttpStatus.CREATED).body("Criado com sucesso.");
	}
	
	@Operation(summary = "Obtém todos os movimentos.")
	@GetMapping
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<MovimentoDto>> obterTodos(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<MovimentoModel> movimentoPages = movimentoService.findAll(pageable);
		if(movimentoPages.isEmpty()) {
			throw new ResponseNotFoundException("Nenhum movimento encontrado.");
		}
		List<MovimentoDto> movimentosDto = new ArrayList<MovimentoDto>();
		for (MovimentoModel movimento : movimentoPages) {
			MovimentoDto movimentoDto = new MovimentoDto();
			BeanUtils.copyProperties(movimento, movimentoDto);
			movimentosDto.add(movimentoDto);
		}
		Page<MovimentoDto> movimentoDtoPages = new PageImpl<MovimentoDto>(movimentosDto);
		return ResponseEntity.status(HttpStatus.OK).body(movimentoDtoPages);
	}
	
	@Operation(summary = "Obtém um movimento.")
	@GetMapping("obterById/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<MovimentoDto> obterById(@PathVariable(value = "id") UUID id){
		Optional<MovimentoModel> movimentoModelOptional = movimentoService.findById(id);
		if(!movimentoModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Movimento não encontrado.");
		}
		MovimentoDto movimentoDto = new MovimentoDto();
		BeanUtils.copyProperties(movimentoModelOptional.get(), movimentoDto);
		return ResponseEntity.status(HttpStatus.OK).body(movimentoDto);
	}
	
	@Operation(summary = "Deleta um movimento.")
	@DeleteMapping("deletar/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> deletar(@PathVariable(value = "id") UUID id){
		Optional<MovimentoModel> movimentoModelOptional = movimentoService.findById(id);
		if(!movimentoModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Movimento não encontrado.");
		}
		movimentoService.deletar(movimentoModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Movimento deletado com sucesso.");
	}
	
	@Operation(summary = "Altera um movimento.")
	@PutMapping("alterar/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> alterar(@PathVariable(value = "id") UUID id, @RequestBody MovimentoDto movimentoDto){
		Optional<MovimentoModel> movimentoModelOptional = movimentoService.findById(id);
		if(!movimentoModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Movimento não encontrado.");
		}
		
		var movimentoModel = new MovimentoModel();
		UUID idemp = movimentoModelOptional.get().getId();
		BeanUtils.copyProperties(movimentoDto, movimentoModelOptional.get());
		movimentoModelOptional.get().setId(idemp);
		movimentoModel = movimentoService.salvar(movimentoModelOptional.get());
		BeanUtils.copyProperties(movimentoModel, movimentoDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(movimentoDto);
	}
}	
