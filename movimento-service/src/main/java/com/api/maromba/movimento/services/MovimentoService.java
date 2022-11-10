package com.api.maromba.movimento.services;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.movimento.models.MovimentoModel;
import com.api.maromba.movimento.repositories.MovimentoRepository;

@Service
public class MovimentoService {
	
	@Autowired
	MovimentoRepository movimentoRepository;
	
	@Transactional
	public MovimentoModel salvar(MovimentoModel movimentoModel) {
		return movimentoRepository.save(movimentoModel);
		
	}
	
	public boolean existe(String nome) {
		return movimentoRepository.existsByNome(nome);
	}
	
	public Optional<MovimentoModel> findById(UUID id) {
		return movimentoRepository.findById(id);
	}

	public Optional<MovimentoModel> findByNome(String nome) {
		return movimentoRepository.findByNome(nome);
	}

	@Transactional
	public void deletar(MovimentoModel movimentoModel) {
		movimentoRepository.deleteById(movimentoModel.getId());
	}

	public Page<MovimentoModel> findAll(Pageable pageable) {
		return movimentoRepository.findAll(pageable);
	}

}
