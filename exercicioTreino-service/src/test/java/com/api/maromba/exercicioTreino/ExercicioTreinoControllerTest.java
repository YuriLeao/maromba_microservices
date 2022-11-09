package com.api.maromba.exercicioTreino;

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

import com.api.maromba.exercicioTreino.dtos.ExercicioTreinoDto;
import com.api.maromba.exercicioTreino.models.ExercicioTreinoModel;
import com.api.maromba.exercicioTreino.repositories.ExercicioTreinoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ExercicioTreinoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ExercicioTreinoRepository exercicioTreinoRepository;

	@Test
	public void incluir() throws Exception {
		var exercicioTreinoDto = new ExercicioTreinoDto(null, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				60, 4, 12, 20, 17, "controlado");
		var exercicioTreino = new ExercicioTreinoModel();
		BeanUtils.copyProperties(exercicioTreinoDto, exercicioTreino);
		when(exercicioTreinoRepository.save(exercicioTreino)).thenReturn(exercicioTreino);

		mockMvc.perform(post("/exercicioTreino-service/incluir").contentType("application/json")
				.content(objectMapper.writeValueAsString(exercicioTreinoDto))).andExpect(status().isCreated());
	}

	@Test
	public void alterar() throws Exception {
		var exercicioTreinoDto = new ExercicioTreinoDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 60, 4, 12, 20, 17, "controlado");
		var exercicioTreino = new ExercicioTreinoModel();
		BeanUtils.copyProperties(exercicioTreinoDto, exercicioTreino);
		when(exercicioTreinoRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(exercicioTreino));
		when(exercicioTreinoRepository.save(exercicioTreino)).thenReturn(exercicioTreino);

		mockMvc.perform(
				put("/exercicioTreino-service/alterar/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
						.contentType("application/json").content(objectMapper.writeValueAsString(exercicioTreinoDto)))
				.andExpect(status().isCreated());
	}

	@Test
	public void obterTodos() throws Exception {
		var exercicioTreinoDto = new ExercicioTreinoDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 60, 4, 12, 20, 17, "controlado");
		var exercicioTreino = new ExercicioTreinoModel();
		BeanUtils.copyProperties(exercicioTreinoDto, exercicioTreino);
		List<ExercicioTreinoModel> lista = new ArrayList<ExercicioTreinoModel>();
		lista.add(exercicioTreino);
		when(exercicioTreinoRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id"))))
				.thenReturn(new PageImpl<ExercicioTreinoModel>(lista));

		mockMvc.perform(get("/exercicioTreino-service").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void obterById() throws Exception {
		var exercicioTreinoDto = new ExercicioTreinoDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 60, 4, 12, 20, 17, "controlado");
		var exercicioTreino = new ExercicioTreinoModel();
		BeanUtils.copyProperties(exercicioTreinoDto, exercicioTreino);
		when(exercicioTreinoRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(exercicioTreino));

		mockMvc.perform(
				get("/exercicioTreino-service/obterById/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
						.contentType("application/json"))
				.andExpect(status().isOk());
	}

	@Test
	public void deletar() throws Exception {
		var exercicioTreinoDto = new ExercicioTreinoDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 60, 4, 12, 20, 17, "controlado");
		var exercicioTreino = new ExercicioTreinoModel();
		BeanUtils.copyProperties(exercicioTreinoDto, exercicioTreino);
		when(exercicioTreinoRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(exercicioTreino)).thenReturn(null);

		mockMvc.perform(
				delete("/exercicioTreino-service/deletar/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
						.contentType("application/json"))
				.andExpect(status().isOk());
	}

}
