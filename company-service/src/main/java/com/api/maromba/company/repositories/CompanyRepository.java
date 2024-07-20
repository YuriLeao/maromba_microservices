package com.api.maromba.company.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.maromba.company.models.CompanyModel;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyModel, UUID>{
	
	boolean existsByName(String name);

}
