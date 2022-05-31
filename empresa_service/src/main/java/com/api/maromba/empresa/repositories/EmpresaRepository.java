package com.api.maromba.empresa.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.empresa.models.EmpresaModel;

public interface EmpresaRepository extends JpaRepository<EmpresaModel, UUID>{
	
	boolean existsByNome(String nome);

	Optional<EmpresaModel> findByNome(String nome);

	void deleteById(UUID id);

}
