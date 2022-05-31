package com.api.maromba.login.controllers;

import java.util.Optional;

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

import com.api.maromba.login.configuration.LoginConfiguration;
import com.api.maromba.login.dtos.UsuarioDto;
import com.api.maromba.login.exception.ResponseConflictException;
import com.api.maromba.login.exception.ResponseNotFoundException;
import com.api.maromba.login.models.UsuarioModel;
import com.api.maromba.login.services.UsuarioService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private LoginConfiguration configuration;
	
	@PostMapping("incluir")
	public ResponseEntity<Object> salvar(@RequestBody @Valid UsuarioDto usuarioDto){
		if(usuarioService.existe(usuarioDto.getUsuario())){
			throw new ResponseConflictException("Usuário já existente.");
		}
		var usuarioModel = new UsuarioModel();
		BeanUtils.copyProperties(usuarioDto, usuarioModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(configuration.getAmbiente());//usuarioService.salvar(usuarioModel));
	}
	
	@GetMapping
	public ResponseEntity<Page<UsuarioModel>> obterTodos(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<UsuarioModel> usuarioPages = usuarioService.findAll(pageable);
		if(usuarioPages.isEmpty()) {
			throw new ResponseNotFoundException("Nenhum usário encontrado.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(usuarioPages);
	}
	
	@GetMapping("logar/{usuario}/{senha}")
	public ResponseEntity<Object> logar(@PathVariable(value = "usuario") String usuario, @PathVariable(value = "senha") String senha){
		Optional<UsuarioModel> usuarioModelOptional = usuarioService.findByUsuarioAndSenha(usuario, senha);
		if(!usuarioModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Usuário ou senha inválidos.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(usuarioModelOptional.get());
	}
	
	@DeleteMapping("deletar/{usuario}/{senha}")
	public ResponseEntity<Object> deletar(@PathVariable(value = "usuario") String usuario, @PathVariable(value = "senha") String senha){
		Optional<UsuarioModel> usuarioModelOptional = usuarioService.findByUsuarioAndSenha(usuario, senha);
		if(!usuarioModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Usuário não encontrado.");
		}
		usuarioService.deletar(usuarioModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado com sucesso.");
	}
	
	@PutMapping("alterar/{usuario}/{senha}")
	public ResponseEntity<Object> alterar(@PathVariable(value = "usuario") String usuario, @PathVariable(value = "senha") String senha,
			@RequestBody UsuarioDto usuarioDto){
		Optional<UsuarioModel> usuarioModelOptional = usuarioService.findByUsuarioAndSenha(usuario, senha);
		if(!usuarioModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Usuário não encontrado.");
		}
		
		var usuarioModel = new UsuarioModel();
		BeanUtils.copyProperties(usuarioDto, usuarioModel);
		usuarioModel.setId(usuarioModelOptional.get().getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvar(usuarioModel));
	}
}	
