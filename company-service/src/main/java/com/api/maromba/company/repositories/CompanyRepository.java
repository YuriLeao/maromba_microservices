package com.api.maromba.company.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.company.models.CompanyModel;

public interface CompanyRepository extends JpaRepository<CompanyModel, UUID>{
	
	boolean existsByName(String name);

	Optional<CompanyModel> findByName(String name);

	void deleteById(UUID id);

}
