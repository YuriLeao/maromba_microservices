package com.api.maromba.movimento.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.movimento.models.MovimentoModel;

public interface MovimentoRepository extends JpaRepository<MovimentoModel, UUID>{
	
	boolean existsByNome(String nome);

	Optional<MovimentoModel> findByNome(String nome);

	void deleteById(UUID id);

}
