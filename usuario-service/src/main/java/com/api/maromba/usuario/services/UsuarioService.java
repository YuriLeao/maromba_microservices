package com.api.maromba.usuario.services;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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
		usuarioModel.setSenha(criptor.criptografarSenha(usuarioModel.getUsuario(), usuarioModel.getSenha()));
		return usuarioRepository.save(usuarioModel);
	}
	
	public boolean existe(String usuario) {
		return usuarioRepository.existsByUsuario(usuario);
	}

	public Optional<UsuarioModel> findByUsuarioAndSenha(String usuario, String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		senha = criptor.criptografarSenha(usuario, senha);
		return usuarioRepository.findByUsuarioAndSenha(usuario, senha);
	}

	@Transactional
	public void deletar(UsuarioModel usuarioModel) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		usuarioModel.setSenha(criptor.criptografarSenha(usuarioModel.getUsuario(), usuarioModel.getSenha()));
		usuarioRepository.deleteByUsuarioAndSenha(usuarioModel.getUsuario(), usuarioModel.getSenha());
	}

	public Page<UsuarioModel> findAll(Pageable pageable) {
		return usuarioRepository.findAll(pageable);
	}

}
