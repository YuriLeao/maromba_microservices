package com.api.maromba.usuario.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.maromba.usuario.models.UsuarioModel;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID>{

	boolean existsByEmail(String email);

	Optional<UsuarioModel> findByEmailAndSenha(String email, String senha);

	void deleteByEmailAndSenha(String email, String senha);
	
	@Query("SELECT u FROM UsuarioModel u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%',:nome,'%'))")
	List<UsuarioModel> findByNomeLike(@Param("nome")  String nome);

}
