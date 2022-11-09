package com.api.maromba.exercicioTreino.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.exercicioTreino.models.ExercicioTreinoModel;

public interface ExercicioTreinoRepository extends JpaRepository<ExercicioTreinoModel, UUID>{

	void deleteById(UUID id);

}
