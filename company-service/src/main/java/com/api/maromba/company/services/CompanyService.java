package com.api.maromba.company.services;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.company.models.CompanyModel;
import com.api.maromba.company.repositories.CompanyRepository;

@Service
public class CompanyService {
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Transactional
	public CompanyModel save(CompanyModel empresaModel) {
		return companyRepository.save(empresaModel);
		
	}
	
	public boolean exists(String nome) {
		return companyRepository.existsByName(nome);
	}
	
	public Optional<CompanyModel> findById(UUID id) {
		return companyRepository.findById(id);
	}

	public Optional<CompanyModel> findByName(String name) {
		return companyRepository.findByName(name);
	}

	@Transactional
	public void delete(CompanyModel companyModel) {
		companyRepository.deleteById(companyModel.getId());
	}

	public Page<CompanyModel> findAll(Pageable pageable) {
		return companyRepository.findAll(pageable);
	}

}
