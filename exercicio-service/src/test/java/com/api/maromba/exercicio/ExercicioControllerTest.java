package com.api.maromba.exercicio;

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

import com.api.maromba.exercicio.dtos.ExercicioDto;
import com.api.maromba.exercicio.models.ExercicioModel;
import com.api.maromba.exercicio.repositories.ExercicioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ExercicioControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ExercicioRepository exercicioRepository;

	@Test
	public void incluir() throws Exception {
		var exercicioDto = new ExercicioDto(null, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				60, 4, 12, 20, 17, "controlado");
		var exercicio = new ExercicioModel();
		BeanUtils.copyProperties(exercicioDto, exercicio);
		when(exercicioRepository.save(exercicio)).thenReturn(exercicio);

		mockMvc.perform(post("/exercicio-service/incluir").contentType("application/json")
				.content(objectMapper.writeValueAsString(exercicioDto))).andExpect(status().isCreated());
	}

	@Test
	public void alterar() throws Exception {
		var exercicioDto = new ExercicioDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 60, 4, 12, 20, 17, "controlado");
		var exercicio = new ExercicioModel();
		BeanUtils.copyProperties(exercicioDto, exercicio);
		when(exercicioRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(exercicio));
		when(exercicioRepository.save(exercicio)).thenReturn(exercicio);

		mockMvc.perform(
				put("/exercicio-service/alterar/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
						.contentType("application/json").content(objectMapper.writeValueAsString(exercicioDto)))
				.andExpect(status().isCreated());
	}

	@Test
	public void obterTodos() throws Exception {
		var exercicioDto = new ExercicioDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 60, 4, 12, 20, 17, "controlado");
		var exercicio = new ExercicioModel();
		BeanUtils.copyProperties(exercicioDto, exercicio);
		List<ExercicioModel> lista = new ArrayList<ExercicioModel>();
		lista.add(exercicio);
		when(exercicioRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id"))))
				.thenReturn(new PageImpl<ExercicioModel>(lista));

		mockMvc.perform(get("/exercicio-service").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void obterById() throws Exception {
		var exercicioDto = new ExercicioDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 60, 4, 12, 20, 17, "controlado");
		var exercicio = new ExercicioModel();
		BeanUtils.copyProperties(exercicioDto, exercicio);
		when(exercicioRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(exercicio));

		mockMvc.perform(
				get("/exercicio-service/obterById/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
						.contentType("application/json"))
				.andExpect(status().isOk());
	}

	@Test
	public void deletar() throws Exception {
		var exercicioDto = new ExercicioDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 60, 4, 12, 20, 17, "controlado");
		var exercicio = new ExercicioModel();
		BeanUtils.copyProperties(exercicioDto, exercicio);
		when(exercicioRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(exercicio)).thenReturn(null);

		mockMvc.perform(
				delete("/exercicio-service/deletar/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
						.contentType("application/json"))
				.andExpect(status().isOk());
	}

}
