package com.api.maromba.grupoMuscular;

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

import com.api.maromba.grupoMuscular.dtos.GrupoMuscularDto;
import com.api.maromba.grupoMuscular.models.GrupoMuscularModel;
import com.api.maromba.grupoMuscular.repositories.GrupoMuscularRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class GrupoMuscularControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private GrupoMuscularRepository grupoMuscularRepository;

	@Test
	public void incluir() throws Exception {
		List<UUID> exercicios = new ArrayList<UUID>();
		exercicios.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		
		List<LocalDate> diasRealizados = new ArrayList<LocalDate>();
		diasRealizados.add(LocalDate.now());
		
		var grupoMuscularDto = new GrupoMuscularDto(null, List.of(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")),
				"peito", diasRealizados);
		var grupoMuscular = new GrupoMuscularModel();
		BeanUtils.copyProperties(grupoMuscularDto, grupoMuscular);
		when(grupoMuscularRepository.save(grupoMuscular)).thenReturn(grupoMuscular);

		mockMvc.perform(post("/grupo-muscular-service/incluir").contentType("application/json")
				.content(objectMapper.writeValueAsString(grupoMuscularDto))).andExpect(status().isCreated());
	}

	@Test
	public void alterar() throws Exception {
		List<UUID> exercicios = new ArrayList<UUID>();
		exercicios.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		
		List<LocalDate> diasRealizados = new ArrayList<LocalDate>();
		diasRealizados.add(LocalDate.now());
		
		var grupoMuscularDto = new GrupoMuscularDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), List.of(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")),
				"peito", diasRealizados);
		var grupoMuscular = new GrupoMuscularModel();
		BeanUtils.copyProperties(grupoMuscularDto, grupoMuscular);
		when(grupoMuscularRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(grupoMuscular));
		when(grupoMuscularRepository.save(grupoMuscular)).thenReturn(grupoMuscular);

		mockMvc.perform(
				put("/grupo-muscular-service/alterar/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
						.contentType("application/json").content(objectMapper.writeValueAsString(grupoMuscularDto)))
				.andExpect(status().isCreated());
	}

	@Test
	public void obterTodos() throws Exception {
		List<UUID> exercicios = new ArrayList<UUID>();
		exercicios.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		
		List<LocalDate> diasRealizados = new ArrayList<LocalDate>();
		diasRealizados.add(LocalDate.now());
		
		var grupoMuscularDto = new GrupoMuscularDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), List.of(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")),
				"peito", diasRealizados);
		var grupoMuscular = new GrupoMuscularModel();
		BeanUtils.copyProperties(grupoMuscularDto, grupoMuscular);
		List<GrupoMuscularModel> lista = new ArrayList<GrupoMuscularModel>();
		lista.add(grupoMuscular);
		when(grupoMuscularRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id"))))
				.thenReturn(new PageImpl<GrupoMuscularModel>(lista));

		mockMvc.perform(get("/grupo-muscular-service/obterTodos").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void obterById() throws Exception {
		List<UUID> exercicios = new ArrayList<UUID>();
		exercicios.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		
		List<LocalDate> diasRealizados = new ArrayList<LocalDate>();
		diasRealizados.add(LocalDate.now());
		
		var grupoMuscularDto = new GrupoMuscularDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), List.of(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")),
				"peito", diasRealizados);
		var grupoMuscular = new GrupoMuscularModel();
		BeanUtils.copyProperties(grupoMuscularDto, grupoMuscular);
		when(grupoMuscularRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(grupoMuscular));

		mockMvc.perform(
				get("/grupo-muscular-service/obterById/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
						.contentType("application/json"))
				.andExpect(status().isOk());
	}

	@Test
	public void deletar() throws Exception {
		List<UUID> exercicios = new ArrayList<UUID>();
		exercicios.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));
		
		List<LocalDate> diasRealizados = new ArrayList<LocalDate>();
		diasRealizados.add(LocalDate.now());
		
		var grupoMuscularDto = new GrupoMuscularDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), List.of(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")),
				"peito", diasRealizados);
		var grupoMuscular = new GrupoMuscularModel();
		BeanUtils.copyProperties(grupoMuscularDto, grupoMuscular);
		when(grupoMuscularRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(grupoMuscular)).thenReturn(null);

		mockMvc.perform(
				delete("/grupo-muscular-service/deletar/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
						.contentType("application/json"))
				.andExpect(status().isOk());
	}

}
