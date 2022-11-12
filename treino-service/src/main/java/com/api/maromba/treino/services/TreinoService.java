package com.api.maromba.treino.services;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.treino.models.TreinoModel;
import com.api.maromba.treino.repositories.TreinoRepository;

@Service
public class TreinoService {
	
	@Autowired
	TreinoRepository treinoRepository;
	
	@Transactional
	public TreinoModel salvar(TreinoModel treinoModel) {
		return treinoRepository.save(treinoModel);
		
	}
	
	public Optional<TreinoModel> findById(UUID id) {
		return treinoRepository.findById(id);
	}

	@Transactional
	public void deletar(TreinoModel treinoModel) {
		treinoRepository.deleteById(treinoModel.getId());
	}

	public Page<TreinoModel> findAll(Pageable pageable) {
		return treinoRepository.findAll(pageable);
	}

}
