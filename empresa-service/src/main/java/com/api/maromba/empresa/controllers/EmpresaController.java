package com.api.maromba.empresa.controllers;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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
		return ResponseEntity.status(HttpStatus.CREATED).body(empresaService.salvar(empresaModel));
	}
	
	@Operation(summary = "Obtém todas as empresas.")
	@GetMapping
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<EmpresaModel>> obter(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<EmpresaModel> empresaPages = empresaService.findAll(pageable);
		if(empresaPages.isEmpty()) {
			throw new ResponseNotFoundException("Nenhuma empresa encontrada.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(empresaPages);
	}
	
	@Operation(summary = "Obtém uma empresa.")
	@GetMapping("obterById/{id}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> obterById(@PathVariable(value = "id") UUID id){
		Optional<EmpresaModel> empresaModelOptional = empresaService.findById(id);
		if(!empresaModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Empresa não encontrada.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(empresaModelOptional.get());
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
		BeanUtils.copyProperties(empresaDto, empresaModel);
		empresaModel.setId(empresaModelOptional.get().getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(empresaService.salvar(empresaModel));
	}
}	
