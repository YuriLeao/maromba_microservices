package com.api.maromba.empresa.controllers;

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

import com.api.maromba.empresa.dtos.EmpresaDto;
import com.api.maromba.empresa.exceptions.ResponseConflictException;
import com.api.maromba.empresa.exceptions.ResponseNotFoundException;
import com.api.maromba.empresa.models.EmpresaModel;
import com.api.maromba.empresa.services.EmpresaService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Empresa Service API endpoint")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/empresa-service")
public class EmpresaController {
	
	@Autowired
	private EmpresaService empresaService;
	
	@Operation(summary = "Salva uma nova empresa.")
	@PostMapping("incluir")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> salvar(@RequestBody @Valid EmpresaDto empresaDto){
		if(empresaService.existe(empresaDto.getNome())){
			throw new ResponseConflictException("Empresa já existente.");
		}
		var empresaModel = new EmpresaModel();
		BeanUtils.copyProperties(empresaDto, empresaModel);
		empresaService.salvar(empresaModel);
		return ResponseEntity.status(HttpStatus.CREATED).body("Criado com sucesso.");
	}
	
	@Operation(summary = "Obtém todas as empresas.")
	@GetMapping("obterTodos")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<EmpresaDto>> obterTodos(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<EmpresaModel> empresaPages = empresaService.findAll(pageable);
		if(empresaPages.isEmpty()) {
			throw new ResponseNotFoundException("Nenhuma empresa encontrada.");
		}
		List<EmpresaDto> empresasDto = new ArrayList<EmpresaDto>();
		for (EmpresaModel empresa : empresaPages) {
			EmpresaDto empresaDto = new EmpresaDto();
			BeanUtils.copyProperties(empresa, empresaDto);
			empresasDto.add(empresaDto);
		}
		Page<EmpresaDto> empresaDtoPages = new PageImpl<EmpresaDto>(empresasDto);
		return ResponseEntity.status(HttpStatus.OK).body(empresaDtoPages);
	}
	
	@Operation(summary = "Obtém uma empresa.")
	@GetMapping("obterById/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<EmpresaDto> obterById(@PathVariable(value = "id") UUID id){
		Optional<EmpresaModel> empresaModelOptional = empresaService.findById(id);
		if(!empresaModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Empresa não encontrada.");
		}
		EmpresaDto empresaDto = new EmpresaDto();
		BeanUtils.copyProperties(empresaModelOptional.get(), empresaDto);
		return ResponseEntity.status(HttpStatus.OK).body(empresaDto);
	}
	
	@Operation(summary = "Deleta uma empresa.")
	@DeleteMapping("deletar/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> deletar(@PathVariable(value = "id") UUID id){
		Optional<EmpresaModel> empresaModelOptional = empresaService.findById(id);
		if(!empresaModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Empresa não encontrada.");
		}
		empresaService.deletar(empresaModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Empresa deletada com sucesso.");
	}
	
	@Operation(summary = "Altera uma empresa.")
	@PutMapping("alterar/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> alterar(@PathVariable(value = "id") UUID id, @RequestBody EmpresaDto empresaDto){
		Optional<EmpresaModel> empresaModelOptional = empresaService.findById(id);
		if(!empresaModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Empresa não encontrada.");
		}
		
		var empresaModel = new EmpresaModel();
		UUID idemp = empresaModelOptional.get().getId();
		BeanUtils.copyProperties(empresaDto, empresaModelOptional.get());
		empresaModelOptional.get().setId(idemp);
		empresaModel = empresaService.salvar(empresaModelOptional.get());
		BeanUtils.copyProperties(empresaModel, empresaDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(empresaDto);
	}
}	
