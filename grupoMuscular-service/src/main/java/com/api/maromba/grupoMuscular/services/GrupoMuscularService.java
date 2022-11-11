package com.api.maromba.grupoMuscular.services;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.grupoMuscular.models.GrupoMuscularModel;
import com.api.maromba.grupoMuscular.repositories.GrupoMuscularRepository;

@Service
public class GrupoMuscularService {
	
	@Autowired
	GrupoMuscularRepository grupoMuscularRepository;
	
	@Transactional
	public GrupoMuscularModel salvar(GrupoMuscularModel grupoMuscularModel) {
		return grupoMuscularRepository.save(grupoMuscularModel);
		
	}
	
	public Optional<GrupoMuscularModel> findById(UUID id) {
		return grupoMuscularRepository.findById(id);
	}

	@Transactional
	public void deletar(GrupoMuscularModel grupoMuscularModel) {
		grupoMuscularRepository.deleteById(grupoMuscularModel.getId());
	}

	public Page<GrupoMuscularModel> findAll(Pageable pageable) {
		return grupoMuscularRepository.findAll(pageable);
	}

}
