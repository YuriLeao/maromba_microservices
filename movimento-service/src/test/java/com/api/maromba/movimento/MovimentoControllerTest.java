package com.api.maromba.movimento;

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

import com.api.maromba.movimento.dtos.MovimentoDto;
import com.api.maromba.movimento.models.MovimentoModel;
import com.api.maromba.movimento.repositories.MovimentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class MovimentoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private MovimentoRepository movimentoRepository;

	@Test
	public void incluir() throws Exception {
		var movimentoDto = new MovimentoDto(null, "teste",
				"video", "peito");
		var movimento = new MovimentoModel();
		BeanUtils.copyProperties(movimentoDto, movimento);
		when(movimentoRepository.existsByNome("teste")).thenReturn(false);
		when(movimentoRepository.save(movimento)).thenReturn(movimento);

		mockMvc.perform(post("/movimento-service/incluir").contentType("application/json")
				.content(objectMapper.writeValueAsString(movimentoDto))).andExpect(status().isCreated());
	}

	@Test
	public void alterar() throws Exception {
		var movimentoDto = new MovimentoDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"video", "peito");
		var movimento = new MovimentoModel();
		BeanUtils.copyProperties(movimentoDto, movimento);
		when(movimentoRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(movimento));
		when(movimentoRepository.save(movimento)).thenReturn(movimento);

		mockMvc.perform(put("/movimento-service/alterar/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json").content(objectMapper.writeValueAsString(movimentoDto)))
				.andExpect(status().isCreated());
	}

	@Test
	public void obterTodos() throws Exception {
		var movimentoDto = new MovimentoDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"video", "peito");
		var movimento = new MovimentoModel();
		BeanUtils.copyProperties(movimentoDto, movimento);
		List<MovimentoModel> lista = new ArrayList<MovimentoModel>();
		lista.add(movimento);
		when(movimentoRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id"))))
				.thenReturn(new PageImpl<MovimentoModel>(lista));

		mockMvc.perform(get("/movimento-service").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void obterById() throws Exception {
		var movimentoDto = new MovimentoDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"video", "peito");
		var movimento = new MovimentoModel();
		BeanUtils.copyProperties(movimentoDto, movimento);
		when(movimentoRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(movimento));

		mockMvc.perform(get("/movimento-service/obterById/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void deletar() throws Exception {
		var movimentoDto = new MovimentoDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"video", "peito");
		var movimento = new MovimentoModel();
		BeanUtils.copyProperties(movimentoDto, movimento);
		when(movimentoRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(movimento)).thenReturn(null);

		mockMvc.perform(delete("/movimento-service/deletar/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json")).andExpect(status().isOk());
	}

}
