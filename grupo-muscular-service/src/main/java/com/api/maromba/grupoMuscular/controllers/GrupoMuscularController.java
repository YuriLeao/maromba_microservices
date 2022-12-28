package com.api.maromba.grupoMuscular.controllers;

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

import com.api.maromba.grupoMuscular.dtos.GrupoMuscularDto;
import com.api.maromba.grupoMuscular.exceptions.ResponseNotFoundException;
import com.api.maromba.grupoMuscular.models.GrupoMuscularModel;
import com.api.maromba.grupoMuscular.services.GrupoMuscularService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Grupo muscular Service API endpoint")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/grupo-muscular-service")
public class GrupoMuscularController {
	
	@Autowired
	private GrupoMuscularService grupoMuscularService;
	
	@Operation(summary = "Salva uma novo grupo muscular .")
	@PostMapping("incluir")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> salvar(@RequestBody @Valid GrupoMuscularDto grupoMuscularDto){
		var grupoMuscularModel = new GrupoMuscularModel();
		BeanUtils.copyProperties(grupoMuscularDto, grupoMuscularModel);
		grupoMuscularService.salvar(grupoMuscularModel);
		return ResponseEntity.status(HttpStatus.CREATED).body("Criado com sucesso.");
	}
	
	@Operation(summary = "Obtém todos os grupos musculares .")
	@GetMapping("obterTodos")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<GrupoMuscularDto>> obterTodos(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<GrupoMuscularModel> grupoMuscularPages = grupoMuscularService.findAll(pageable);
		if(grupoMuscularPages.isEmpty()) {
			throw new ResponseNotFoundException("Nenhum grupo muscular encontrado.");
		}
		List<GrupoMuscularDto> gruposMuscularesDto = new ArrayList<GrupoMuscularDto>();
		for (GrupoMuscularModel grupoMuscular : grupoMuscularPages) {
			GrupoMuscularDto grupoMuscularDto = new GrupoMuscularDto();
			BeanUtils.copyProperties(grupoMuscular, grupoMuscularDto);
			gruposMuscularesDto.add(grupoMuscularDto);
		}
		Page<GrupoMuscularDto> grupoMuscularDtoPages = new PageImpl<GrupoMuscularDto>(gruposMuscularesDto);
		return ResponseEntity.status(HttpStatus.OK).body(grupoMuscularDtoPages);
	}
	
	@Operation(summary = "Obtém um grupoMuscular .")
	@GetMapping("obterById/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<GrupoMuscularDto> obterById(@PathVariable(value = "id") UUID id){
		Optional<GrupoMuscularModel> grupoMuscularModelOptional = grupoMuscularService.findById(id);
		if(!grupoMuscularModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Grupo muscular não encontrado.");
		}
		GrupoMuscularDto grupoMuscularDto = new GrupoMuscularDto();
		BeanUtils.copyProperties(grupoMuscularModelOptional.get(), grupoMuscularDto);
		return ResponseEntity.status(HttpStatus.OK).body(grupoMuscularDto);
	}
	
	@Operation(summary = "Deleta um grupoMuscular .")
	@DeleteMapping("deletar/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> deletar(@PathVariable(value = "id") UUID id){
		Optional<GrupoMuscularModel> grupoMuscularModelOptional = grupoMuscularService.findById(id);
		if(!grupoMuscularModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Grupo muscular não encontrado.");
		}
		grupoMuscularService.deletar(grupoMuscularModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Grupo muscular deletado com sucesso.");
	}
	
	@Operation(summary = "Altera uma grupoMuscular .")
	@PutMapping("alterar/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> alterar(@PathVariable(value = "id") UUID id, @RequestBody GrupoMuscularDto grupoMuscularDto){
		Optional<GrupoMuscularModel> grupoMuscularModelOptional = grupoMuscularService.findById(id);
		if(!grupoMuscularModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Grupo muscular não encontrado.");
		}
		
		var grupoMuscularModel = new GrupoMuscularModel();
		UUID idemp = grupoMuscularModelOptional.get().getId();
		BeanUtils.copyProperties(grupoMuscularDto, grupoMuscularModelOptional.get());
		grupoMuscularModelOptional.get().setId(idemp);
		grupoMuscularModel = grupoMuscularService.salvar(grupoMuscularModelOptional.get());
		BeanUtils.copyProperties(grupoMuscularModel, grupoMuscularDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(grupoMuscularDto);
	}
}	
