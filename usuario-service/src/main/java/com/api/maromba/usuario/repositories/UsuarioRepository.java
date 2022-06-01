package com.api.maromba.usuario.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.usuario.models.UsuarioModel;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID>{

	boolean existsByUsuario(String usuario);

	Optional<UsuarioModel> findByUsuarioAndSenha(String usuario, String senha);

	void deleteByUsuarioAndSenha(String usuario, String senha);

}
