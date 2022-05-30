package com.api.maromba.login.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.login.models.UsuarioModel;
import com.api.maromba.login.repositories.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Transactional
	public UsuarioModel salvar(UsuarioModel usuarioModel) {
		return usuarioRepository.save(usuarioModel);
		
	}
	
	public boolean existe(String usuario) {
		return usuarioRepository.existsByUsuario(usuario);
	}

	public Optional<UsuarioModel> findByUsuarioAndSenha(String usuario, String senha) {
		return usuarioRepository.findByUsuarioAndSenha(usuario, senha);
	}

	@Transactional
	public void deletar(UsuarioModel usuarioModel) {
		usuarioRepository.deleteByUsuarioAndSenha(usuarioModel.getUsuario(), usuarioModel.getSenha());
	}

	public Page<UsuarioModel> findAll(Pageable pageable) {
		return usuarioRepository.findAll(pageable);
	}

}
