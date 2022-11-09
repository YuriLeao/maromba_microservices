package com.api.maromba.exercicio.services;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.exercicio.models.ExercicioModel;
import com.api.maromba.exercicio.repositories.ExercicioRepository;

@Service
public class ExercicioService {
	
	@Autowired
	ExercicioRepository exercicioRepository;
	
	@Transactional
	public ExercicioModel salvar(ExercicioModel exercicioModel) {
		return exercicioRepository.save(exercicioModel);
		
	}
	
	public boolean existe(String nome) {
		return exercicioRepository.existsByNome(nome);
	}
	
	public Optional<ExercicioModel> findById(UUID id) {
		return exercicioRepository.findById(id);
	}

	public Optional<ExercicioModel> findByNome(String nome) {
		return exercicioRepository.findByNome(nome);
	}

	@Transactional
	public void deletar(ExercicioModel exercicioModel) {
		exercicioRepository.deleteById(exercicioModel.getId());
	}

	public Page<ExercicioModel> findAll(Pageable pageable) {
		return exercicioRepository.findAll(pageable);
	}

}
