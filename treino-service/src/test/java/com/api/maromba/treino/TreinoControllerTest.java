package com.api.maromba.treino;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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

import com.api.maromba.treino.dtos.TreinoDto;
import com.api.maromba.treino.models.TreinoModel;
import com.api.maromba.treino.repositories.TreinoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class TreinoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private TreinoRepository treinoRepository;

	@Test
	public void incluir() throws Exception {
		List<UUID> exercicios = new ArrayList<UUID>();
		exercicios.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		
		List<LocalDate> diasRealizados = new ArrayList<LocalDate>();
		diasRealizados.add(LocalDate.now());
		
		var treinoDto = new TreinoDto(null, List.of(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		var treino = new TreinoModel();
		BeanUtils.copyProperties(treinoDto, treino);
		when(treinoRepository.save(treino)).thenReturn(treino);

		mockMvc.perform(post("/treino-service/incluir").contentType("application/json")
				.content(objectMapper.writeValueAsString(treinoDto))).andExpect(status().isCreated());
	}

	@Test
	public void alterar() throws Exception {
		List<UUID> exercicios = new ArrayList<UUID>();
		exercicios.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		
		List<LocalDate> diasRealizados = new ArrayList<LocalDate>();
		diasRealizados.add(LocalDate.now());
		
		var treinoDto = new TreinoDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), List.of(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		var treino = new TreinoModel();
		BeanUtils.copyProperties(treinoDto, treino);
		when(treinoRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(treino));
		when(treinoRepository.save(treino)).thenReturn(treino);

		mockMvc.perform(
				put("/treino-service/alterar/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
						.contentType("application/json").content(objectMapper.writeValueAsString(treinoDto)))
				.andExpect(status().isCreated());
	}

	@Test
	public void obterTodos() throws Exception {
		List<UUID> exercicios = new ArrayList<UUID>();
		exercicios.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		
		List<LocalDate> diasRealizados = new ArrayList<LocalDate>();
		diasRealizados.add(LocalDate.now());
		
		var treinoDto = new TreinoDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), List.of(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		var treino = new TreinoModel();
		BeanUtils.copyProperties(treinoDto, treino);
		List<TreinoModel> lista = new ArrayList<TreinoModel>();
		lista.add(treino);
		when(treinoRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id"))))
				.thenReturn(new PageImpl<TreinoModel>(lista));

		mockMvc.perform(get("/treino-service/obterTodos").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void obterById() throws Exception {
		List<UUID> exercicios = new ArrayList<UUID>();
		exercicios.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		
		List<LocalDate> diasRealizados = new ArrayList<LocalDate>();
		diasRealizados.add(LocalDate.now());
		
		var treinoDto = new TreinoDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), List.of(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		var treino = new TreinoModel();
		BeanUtils.copyProperties(treinoDto, treino);
		when(treinoRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(treino));

		mockMvc.perform(
				get("/treino-service/obterById/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
						.contentType("application/json"))
				.andExpect(status().isOk());
	}

	@Test
	public void deletar() throws Exception {
		List<UUID> exercicios = new ArrayList<UUID>();
		exercicios.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		
		List<LocalDate> diasRealizados = new ArrayList<LocalDate>();
		diasRealizados.add(LocalDate.now());
		
		var treinoDto = new TreinoDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), List.of(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		var treino = new TreinoModel();
		BeanUtils.copyProperties(treinoDto, treino);
		when(treinoRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(treino)).thenReturn(null);

		mockMvc.perform(
				delete("/treino-service/deletar/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
						.contentType("application/json"))
				.andExpect(status().isOk());
	}

}
