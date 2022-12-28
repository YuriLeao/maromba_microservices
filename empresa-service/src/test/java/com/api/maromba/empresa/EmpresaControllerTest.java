package com.api.maromba.empresa;

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

import com.api.maromba.empresa.dtos.EmpresaDto;
import com.api.maromba.empresa.models.EmpresaModel;
import com.api.maromba.empresa.repositories.EmpresaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class EmpresaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private EmpresaRepository empresaRepository;

	@Test
	public void incluir() throws Exception {
		var empresaDto = new EmpresaDto(null, "teste", "99999999999999", "tt@gmail.com", "1234", "99999999");
		var empresa = new EmpresaModel();
		BeanUtils.copyProperties(empresaDto, empresa);
		when(empresaRepository.existsByNome("teste")).thenReturn(false);
		when(empresaRepository.save(empresa)).thenReturn(empresa);

		mockMvc.perform(post("/empresa-service/incluir").contentType("application/json")
				.content(objectMapper.writeValueAsString(empresaDto))).andExpect(status().isCreated());
	}

	@Test
	public void alterar() throws Exception {
		var empresaDto = new EmpresaDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"99999999999999", "tt@gmail.com", "1234", "99999999");
		var empresa = new EmpresaModel();
		BeanUtils.copyProperties(empresaDto, empresa);
		when(empresaRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(empresa));
		when(empresaRepository.save(empresa)).thenReturn(empresa);

		mockMvc.perform(put("/empresa-service/alterar/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json").content(objectMapper.writeValueAsString(empresaDto)))
				.andExpect(status().isCreated());
	}

	@Test
	public void obterTodos() throws Exception {
		var empresaDto = new EmpresaDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"99999999999999", "tt@gmail.com", "1234", "99999999");
		var empresa = new EmpresaModel();
		BeanUtils.copyProperties(empresaDto, empresa);
		List<EmpresaModel> lista = new ArrayList<EmpresaModel>();
		lista.add(empresa);
		when(empresaRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id"))))
				.thenReturn(new PageImpl<EmpresaModel>(lista));

		mockMvc.perform(get("/empresa-service/obterTodos").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void obterById() throws Exception {
		var empresaDto = new EmpresaDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"99999999999999", "tt@gmail.com", "1234", "99999999");
		var empresa = new EmpresaModel();
		BeanUtils.copyProperties(empresaDto, empresa);
		when(empresaRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(empresa));

		mockMvc.perform(get("/empresa-service/obterById/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void deletar() throws Exception {
		var empresaDto = new EmpresaDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"99999999999999", "tt@gmail.com", "1234", "99999999");
		var empresa = new EmpresaModel();
		BeanUtils.copyProperties(empresaDto, empresa);
		when(empresaRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(empresa)).thenReturn(null);

		mockMvc.perform(delete("/empresa-service/deletar/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json")).andExpect(status().isOk());
	}

}
