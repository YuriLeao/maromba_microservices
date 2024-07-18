package com.api.maromba.exercise;

import static org.mockito.Mockito.when;
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

import com.api.maromba.exercise.dtos.ExerciseDTO;
import com.api.maromba.exercise.models.ExerciseModel;
import com.api.maromba.exercise.repositories.ExerciseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ExerciseControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ExerciseRepository exerciseRepository;

	@Test
	public void save() throws Exception {
		var exerciseDTO = new ExerciseDTO(null, "teste",
				"video");
		var exercise = new ExerciseModel();
		BeanUtils.copyProperties(exerciseDTO, exercise);
		when(exerciseRepository.existsByName("teste")).thenReturn(false);
		when(exerciseRepository.save(exercise)).thenReturn(exercise);

		mockMvc.perform(post("/exercise-service/save").contentType("application/json")
				.content(objectMapper.writeValueAsString(exerciseDTO))).andExpect(status().isCreated());
	}

	@Test
	public void update() throws Exception {
		var exerciseDTO = new ExerciseDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"video");
		var exercise = new ExerciseModel();
		BeanUtils.copyProperties(exerciseDTO, exercise);
		when(exerciseRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(exercise));
		when(exerciseRepository.save(exercise)).thenReturn(exercise);

		mockMvc.perform(put("/exercise-service/update/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json").content(objectMapper.writeValueAsString(exerciseDTO)))
				.andExpect(status().isCreated());
	}

	@Test
	public void getAll() throws Exception {
		var exerciseDTO = new ExerciseDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"video");
		var exercise = new ExerciseModel();
		BeanUtils.copyProperties(exerciseDTO, exercise);
		List<ExerciseModel> list = new ArrayList<ExerciseModel>();
		list.add(exercise);
		when(exerciseRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id"))))
				.thenReturn(new PageImpl<ExerciseModel>(list));

		mockMvc.perform(get("/exercise-service/getAll").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void getById() throws Exception {
		var exerciseDTO = new ExerciseDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"video");
		var exercise = new ExerciseModel();
		BeanUtils.copyProperties(exerciseDTO, exercise);
		when(exerciseRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(exercise));

		mockMvc.perform(get("/exercise-service/getById/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void delete() throws Exception {
		var exerciseDTO = new ExerciseDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste",
				"video");
		var exercise = new ExerciseModel();
		BeanUtils.copyProperties(exerciseDTO, exercise);
		when(exerciseRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(exercise)).thenReturn(null);

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/exercise-service/delete/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json")).andExpect(status().isOk());
	}

}
