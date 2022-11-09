package com.api.maromba.exercicioTreino.services;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.exercicioTreino.models.ExercicioTreinoModel;
import com.api.maromba.exercicioTreino.repositories.ExercicioTreinoRepository;

@Service
public class ExercicioTreinoService {
	
	@Autowired
	ExercicioTreinoRepository exercicioTreinoRepository;
	
	@Transactional
	public ExercicioTreinoModel salvar(ExercicioTreinoModel exercicioTreinoModel) {
		return exercicioTreinoRepository.save(exercicioTreinoModel);
		
	}
	
	public Optional<ExercicioTreinoModel> findById(UUID id) {
		return exercicioTreinoRepository.findById(id);
	}

	@Transactional
	public void deletar(ExercicioTreinoModel exercicioTreinoModel) {
		exercicioTreinoRepository.deleteById(exercicioTreinoModel.getId());
	}

	public Page<ExercicioTreinoModel> findAll(Pageable pageable) {
		return exercicioTreinoRepository.findAll(pageable);
	}

}
