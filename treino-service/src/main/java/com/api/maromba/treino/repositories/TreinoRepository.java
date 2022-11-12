package com.api.maromba.treino.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.treino.models.TreinoModel;

public interface TreinoRepository extends JpaRepository<TreinoModel, UUID>{

	void deleteById(UUID id);

}
