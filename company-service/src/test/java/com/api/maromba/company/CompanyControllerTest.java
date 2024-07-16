package com.api.maromba.company;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import com.api.maromba.company.dtos.CompanyDTO;
import com.api.maromba.company.models.CompanyModel;
import com.api.maromba.company.repositories.CompanyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CompanyRepository companyRepository;

	@Test
	public void save() throws Exception {
		var companyDTO = new CompanyDTO(null, "teste", "99999999999999", "tt@gmail.com", "1234", "99999999");
		var company = new CompanyModel();
		BeanUtils.copyProperties(companyDTO, company);
		when(companyRepository.existsByName("teste")).thenReturn(false);
		when(companyRepository.save(company)).thenReturn(company);

		mockMvc.perform(post("/company-service/save").contentType("application/json")
				.content(objectMapper.writeValueAsString(companyDTO))).andExpect(status().isCreated());
	}

	@Test
	public void update() throws Exception {
		var companyDTO = new CompanyDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"99999999999999", "tt@gmail.com", "1234", "99999999");
		var company = new CompanyModel();
		BeanUtils.copyProperties(companyDTO, company);
		when(companyRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(company));
		when(companyRepository.save(company)).thenReturn(company);

		mockMvc.perform(put("/company-service/update/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json").content(objectMapper.writeValueAsString(companyDTO)))
				.andExpect(status().isCreated());
	}

	@Test
	public void getAll() throws Exception {
		var companyDTO = new CompanyDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"99999999999999", "tt@gmail.com", "1234", "99999999");
		var company = new CompanyModel();
		BeanUtils.copyProperties(companyDTO, company);
		List<CompanyModel> list = new ArrayList<CompanyModel>();
		list.add(company);
		when(companyRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id"))))
				.thenReturn(new PageImpl<CompanyModel>(list));

		mockMvc.perform(get("/company-service/getAll").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void getById() throws Exception {
		var companyDTO = new CompanyDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"99999999999999", "tt@gmail.com", "1234", "99999999");
		var company = new CompanyModel();
		BeanUtils.copyProperties(companyDTO, company);
		when(companyRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(company));

		mockMvc.perform(get("/company-service/getById/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void delet() throws Exception {
		var companyDTO = new CompanyDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"99999999999999", "tt@gmail.com", "1234", "99999999");
		var company = new CompanyModel();
		BeanUtils.copyProperties(companyDTO, company);
		when(companyRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(company)).thenReturn(null);

		mockMvc.perform(delete("/company-service/delete/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json")).andExpect(status().isOk());
	}

}
