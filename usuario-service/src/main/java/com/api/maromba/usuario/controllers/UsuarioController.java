package com.api.maromba.usuario.controllers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.api.maromba.usuario.dtos.UsuarioDto;
import com.api.maromba.usuario.exception.ResponseConflictException;
import com.api.maromba.usuario.exception.ResponseNotFoundException;
import com.api.maromba.usuario.models.UsuarioModel;
import com.api.maromba.usuario.proxy.EmpresaProxy;
import com.api.maromba.usuario.services.UsuarioService;
import com.api.maromba.usuario.util.JwtUtil;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Usuario Service API")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/usuario-service")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EmpresaProxy empresaProxy;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Salva um novo usuario.")
	@PostMapping("incluir")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> salvar(@RequestBody @Valid UsuarioDto usuarioDto) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		if(usuarioService.existsByEmail(usuarioDto.getEmail())){
			throw new ResponseConflictException("Usuário já existente.");
		}
		var usuarioModel = new UsuarioModel();
		BeanUtils.copyProperties(usuarioDto, usuarioModel);
		usuarioService.salvar(usuarioModel);
		return ResponseEntity.status(HttpStatus.CREATED).body("Criado com sucesso.");
	}
	
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Obtém todos os usuários.")
	@GetMapping("obterTodos")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Page<UsuarioDto>> obterTodos(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
		Page<UsuarioModel> usuarioPages = usuarioService.findAll(pageable);
		if(usuarioPages.isEmpty()) {
			throw new ResponseNotFoundException("Nenhum usário encontrado.");
		}
		List<UsuarioDto> usuarioDtos = new ArrayList<UsuarioDto>();
		for (UsuarioModel usuarioModel : usuarioPages) {
			UsuarioDto usuarioDto = new UsuarioDto();
			BeanUtils.copyProperties(usuarioModel, usuarioDto);
			usuarioDto.setSenha(null);
			usuarioDtos.add(usuarioDto);
		}
		Page<UsuarioDto> usuarioDtoPages = new PageImpl<UsuarioDto>(usuarioDtos);
		return ResponseEntity.status(HttpStatus.OK).body(usuarioDtoPages);
	}
	
	@Operation(summary = "Faz login.")
	@GetMapping("login/{email}/{senha}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<UsuarioDto> login(@PathVariable(value = "email") String email, @PathVariable(value = "senha") String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		Optional<UsuarioModel> usuarioModelOptional = usuarioService.findByEmailAndSenha(email, senha);
		if(!usuarioModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Usuário ou senha inválidos.");
		}
		UsuarioDto usuarioDto = new UsuarioDto();
		BeanUtils.copyProperties(usuarioModelOptional.get(), usuarioDto);
		try {
			var response = empresaProxy.obterById(usuarioDto.getEmpresaId());
			LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap<String, Object>)response.getBody();
			usuarioDto.setEmpresaNome(linkedHashMap.get("nome").toString());
		} catch (Exception e) {
			logger.error("Erro na obtenção do nome da empresa.");
		}
		usuarioDto.setToken(jwtUtil.generateToken(usuarioModelOptional.get(), "/usuario-service/login"));
		usuarioDto.setSenha(null);
		return ResponseEntity.status(HttpStatus.OK).body(usuarioDto);
	}
	
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Obtém uma lista de usuários atráves de parte do nome.")
	@GetMapping("obterByNomeLike/{nome}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<List<UsuarioDto>> obterByNomeLike(@PathVariable(value = "nome") String nome){
		List<UsuarioModel> usuarioModels = usuarioService.findByNomeLike(nome);
		if(usuarioModels == null || usuarioModels.isEmpty()) {
			throw new ResponseNotFoundException("Usuário não encontrado.");
		}
		List<UsuarioDto> usuarioDtos = new ArrayList<UsuarioDto>();
		for (UsuarioModel usuarioModel : usuarioModels) {
			UsuarioDto usuarioDto = new UsuarioDto();
			BeanUtils.copyProperties(usuarioModel, usuarioDto);
			usuarioDto.setSenha(null);
			usuarioDtos.add(usuarioDto);
		}
		return ResponseEntity.status(HttpStatus.OK).body(usuarioDtos);
	}
	
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Deleta um usuário.")
	@DeleteMapping("deletar/{email}/{senha}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<Object> deletar(@PathVariable(value = "email") String email, @PathVariable(value = "senha") String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		Optional<UsuarioModel> usuarioModelOptional = usuarioService.findByEmailAndSenha(email, senha);
		if(!usuarioModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Usuário não encontrado.");
		}
		usuarioService.deletar(usuarioModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado com sucesso.");
	}
	
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Altera um usuario.")
	@PutMapping("alterar/{email}/{senha}")
	@Retry(name = "default")
	@CircuitBreaker(name = "default")
	public ResponseEntity<UsuarioDto> alterar(@PathVariable(value = "email") String email, @PathVariable(value = "senha") String senha,
			@RequestBody UsuarioDto usuarioDto) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		Optional<UsuarioModel> usuarioModelOptional = usuarioService.findByEmailAndSenha(email, senha);
		if(!usuarioModelOptional.isPresent()) {
			throw new ResponseNotFoundException("Usuário não encontrado.");
		}
		var usuarioModel = new UsuarioModel();
		UUID id = usuarioModelOptional.get().getId();
		BeanUtils.copyProperties(usuarioDto, usuarioModelOptional.get());
		usuarioModelOptional.get().setId(id);
		usuarioModel = usuarioService.salvar(usuarioModelOptional.get());
		BeanUtils.copyProperties(usuarioModel, usuarioDto);
		usuarioDto.setSenha(null);
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDto);
	}
}	
