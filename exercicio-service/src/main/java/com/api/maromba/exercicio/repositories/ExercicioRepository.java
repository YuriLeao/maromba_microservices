package com.api.maromba.exercicio.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.exercicio.models.ExercicioModel;

public interface ExercicioRepository extends JpaRepository<ExercicioModel, UUID>{
	
	boolean existsByNome(String nome);

	Optional<ExercicioModel> findByNome(String nome);

	void deleteById(UUID id);

}
