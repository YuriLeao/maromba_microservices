package com.api.maromba.usuario.services;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.usuario.models.UsuarioModel;
import com.api.maromba.usuario.repositories.UsuarioRepository;
import com.api.maromba.usuario.util.Criptor;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private Criptor criptor;
	
	@Transactional
	public UsuarioModel salvar(UsuarioModel usuarioModel) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		usuarioModel.setSenha(criptor.criptografarSenha(usuarioModel.getEmail(), usuarioModel.getSenha()));
		return usuarioRepository.save(usuarioModel);
	}
	
	public Boolean existsByEmail(String email) {
		return usuarioRepository.existsByEmail(email);
	}

	public Optional<UsuarioModel> findByEmailAndSenha(String email, String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		senha = criptor.criptografarSenha(email, senha);
		return usuarioRepository.findByEmailAndSenha(email, senha);
	}

	@Transactional
	public void deletar(UsuarioModel usuarioModel) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		usuarioModel.setSenha(criptor.criptografarSenha(usuarioModel.getEmail(), usuarioModel.getSenha()));
		usuarioRepository.deleteByEmailAndSenha(usuarioModel.getEmail(), usuarioModel.getSenha());
	}

	public Page<UsuarioModel> findAll(Pageable pageable) {
		return usuarioRepository.findAll(pageable);
	}
	
	public List<UsuarioModel> findByNomeLike(String nome) {
		return usuarioRepository.findByNomeLike(nome);
	}

}
