package com.api.maromba.company.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.company.dtos.CompanyDTO;
import com.api.maromba.company.exceptions.ResponseConflictException;
import com.api.maromba.company.exceptions.ResponseNotFoundException;
import com.api.maromba.company.models.CompanyModel;
import com.api.maromba.company.repositories.CompanyRepository;

@Service
public class CompanyService {

	@Autowired
	CompanyRepository companyRepository;

	@Transactional
	public CompanyDTO save(CompanyDTO companyDTO) {
		if (companyRepository.existsByName(companyDTO.getName())) {
			throw new ResponseConflictException("Company already exists.");
		}
		var companyModel = convertDTOToModel(companyDTO);
		return convertModelToDTO(companyRepository.save(companyModel));

	}

	@Transactional
	public CompanyDTO update(UUID id, CompanyDTO companyDTO) {
		CompanyModel companyModel = companyRepository.findById(id)
				.orElseThrow(() -> new ResponseNotFoundException("No company found."));

		UUID idemp = companyModel.getId();
		companyModel = convertDTOToModel(companyDTO);
		companyModel.setId(idemp);
		companyModel = companyRepository.save(companyModel);
		return convertModelToDTO(companyModel);
	}

	public CompanyDTO getById(UUID id) {
		CompanyModel companyModel = companyRepository.findById(id)
				.orElseThrow(() -> new ResponseNotFoundException("No company found."));

		return convertModelToDTO(companyModel);
	}

	@Transactional
	public void delete(UUID id) {
		CompanyModel companyModel = companyRepository.findById(id)
				.orElseThrow(() -> new ResponseNotFoundException("No company found."));

		companyRepository.delete(companyModel);
	}

	public Page<CompanyDTO> getAll(Pageable pageable) {
		Page<CompanyModel> companyPages = companyRepository.findAll(pageable);
		if (companyPages.isEmpty()) {
			throw new ResponseNotFoundException("No company found.");
		}

		List<CompanyDTO> companysDTO = new ArrayList<CompanyDTO>();
		for (CompanyModel company : companyPages) {
			CompanyDTO companyDTO = convertModelToDTO(company);
			companysDTO.add(companyDTO);
		}
		return new PageImpl<CompanyDTO>(companysDTO);
	}

	private CompanyModel convertDTOToModel(CompanyDTO companyDTO) {
		var companyModel = new CompanyModel();
		BeanUtils.copyProperties(companyDTO, companyModel);
		return companyModel;
	}

	private CompanyDTO convertModelToDTO(CompanyModel company) {
		CompanyDTO companyDTO = new CompanyDTO();
		BeanUtils.copyProperties(company, companyDTO);
		return companyDTO;
	}

}
