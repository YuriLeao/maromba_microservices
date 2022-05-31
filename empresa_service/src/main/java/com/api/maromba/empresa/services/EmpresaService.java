package com.api.maromba.empresa.services;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.empresa.models.EmpresaModel;
import com.api.maromba.empresa.repositories.EmpresaRepository;

@Service
public class EmpresaService {
	
	@Autowired
	EmpresaRepository empresaRepository;
	
	@Transactional
	public EmpresaModel salvar(EmpresaModel empresaModel) {
		return empresaRepository.save(empresaModel);
		
	}
	
	public boolean existe(String nome) {
		return empresaRepository.existsByNome(nome);
	}
	
	public Optional<EmpresaModel> findById(UUID id) {
		return empresaRepository.findById(id);
	}

	public Optional<EmpresaModel> findByNome(String nome) {
		return empresaRepository.findByNome(nome);
	}

	@Transactional
	public void deletar(EmpresaModel empresaModel) {
		empresaRepository.deleteById(empresaModel.getId());
	}

	public Page<EmpresaModel> findAll(Pageable pageable) {
		return empresaRepository.findAll(pageable);
	}

}
