package com.api.maromba.grupoMuscular.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.grupoMuscular.models.GrupoMuscularModel;

public interface GrupoMuscularRepository extends JpaRepository<GrupoMuscularModel, UUID>{

	void deleteById(UUID id);

}
