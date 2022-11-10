package com.api.maromba.exercicio.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.exercicio.models.ExercicioModel;

public interface ExercicioRepository extends JpaRepository<ExercicioModel, UUID>{

	void deleteById(UUID id);

}
