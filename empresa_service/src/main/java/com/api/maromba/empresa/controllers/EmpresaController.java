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


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/empresa")
public class EmpresaController {
	
	@Autowired
	private EmpresaService empresaService;
	
	@PostMapping("incluir")
	public ResponseEntity<Object> salvar(@RequestBody @Valid EmpresaDto empresaDto){
		if(empresaService.existe(empresaDto.getNome())){
			throw new ResponseConflictException("Empresa já existente.");
		}
		var empresaModel = new EmpresaModel();
		BeanUtils.copyProperties(empresaDto, empresaModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(empresaService.salvar(empresaModel));
	}
	
	@GetMapping
	public ResponseEntity<Page<EmpresaModel>> obterTodos(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<EmpresaModel> usuarioPages = empresaService.findAll(pageable);
		if(usuarioPages.isEmpty()) {
			throw new ResponseNotFoundException("Nenhuma empresa encontrada.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(usuarioPages);
	}
	
	@GetMapping("obterById/{id}")
	public ResponseEntity<Object> obterById(@PathVariable(value = "id") UUID id){
		Optional<EmpresaModel> empresaModelOptional = empresaService.findById(id);
		if(!empresaModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Empresa não encontrada.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(empresaModelOptional.get());
	}
	
	@DeleteMapping("deletar/{id}")
	public ResponseEntity<Object> deletar(@PathVariable(value = "id") UUID id){
		Optional<EmpresaModel> empresaModelOptional = empresaService.findById(id);
		if(!empresaModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Empresa não encontrada.");
		}
		empresaService.deletar(empresaModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Empresa deletada com sucesso.");
	}
	
	@PutMapping("alterar/{id}")
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
