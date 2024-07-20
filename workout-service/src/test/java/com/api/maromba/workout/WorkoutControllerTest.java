package com.api.maromba.workout;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
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

import com.api.maromba.workout.dtos.WorkoutDTO;
import com.api.maromba.workout.dtos.WorkoutItemDTO;
import com.api.maromba.workout.models.WorkoutItemModel;
import com.api.maromba.workout.models.WorkoutModel;
import com.api.maromba.workout.repositories.WorkoutRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkoutControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private WorkoutRepository workoutRepository;

	@Test
	public void save() throws Exception {

		WorkoutItemDTO itemDTO = new WorkoutItemDTO(null, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 12,
				4, new BigDecimal(90.00), "dropset", new BigDecimal(90.00), 150);
		List<WorkoutItemDTO> itemsDTO = new ArrayList<WorkoutItemDTO>();
		itemsDTO.add(itemDTO);

		var workoutDTO = new WorkoutDTO(null, "treino a", LocalDate.now(), itemsDTO);
		var workout = new WorkoutModel();
		BeanUtils.copyProperties(workoutDTO, workout);
		when(workoutRepository.save(workout)).thenReturn(workout);

		mockMvc.perform(post("/workout-service/save").contentType("application/json")
				.content(objectMapper.writeValueAsString(workoutDTO))).andExpect(status().isCreated());
	}

	@Test
	public void update() throws Exception {

		WorkoutItemDTO itemDTO = new WorkoutItemDTO(null, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 12,
				4, new BigDecimal(90.00), "dropset", new BigDecimal(90.00), 150);
		List<WorkoutItemDTO> itemsDTO = new ArrayList<WorkoutItemDTO>();
		itemsDTO.add(itemDTO);

		var workoutDTO = new WorkoutDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "treino a",
				LocalDate.now(), itemsDTO);
		var workout = new WorkoutModel();
		BeanUtils.copyProperties(workoutDTO, workout);

		workout.setWorkoutItems(new ArrayList<WorkoutItemModel>());
		WorkoutItemModel workoutItemModel = new WorkoutItemModel();
		for (WorkoutItemDTO workoutItemDTO : workoutDTO.getWorkoutItems()) {
			workoutItemModel = new WorkoutItemModel();
			BeanUtils.copyProperties(workoutItemDTO, workoutItemModel);

			workout.getWorkoutItems().add(workoutItemModel);
		}

		when(workoutRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(workout));
		when(workoutRepository.save(workout)).thenReturn(workout);

		mockMvc.perform(put("/workout-service/update/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json").content(objectMapper.writeValueAsString(workoutDTO)))
				.andExpect(status().isCreated());
	}

	@Test
	public void getAll() throws Exception {
		WorkoutItemDTO itemDTO = new WorkoutItemDTO(null, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 12,
				4, new BigDecimal(90.00), "dropset", new BigDecimal(90.00), 150);
		List<WorkoutItemDTO> itemsDTO = new ArrayList<WorkoutItemDTO>();
		itemsDTO.add(itemDTO);

		var workoutDTO = new WorkoutDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "treino a",
				LocalDate.now(), itemsDTO);
		var workout = new WorkoutModel();
		BeanUtils.copyProperties(workoutDTO, workout);

		workout.setWorkoutItems(new ArrayList<WorkoutItemModel>());
		WorkoutItemModel workoutItemModel = new WorkoutItemModel();
		for (WorkoutItemDTO workoutItemDTO : workoutDTO.getWorkoutItems()) {
			workoutItemModel = new WorkoutItemModel();
			BeanUtils.copyProperties(workoutItemDTO, workoutItemModel);

			workout.getWorkoutItems().add(workoutItemModel);
		}
		List<WorkoutModel> list = new ArrayList<WorkoutModel>();
		list.add(workout);
		when(workoutRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id"))))
				.thenReturn(new PageImpl<WorkoutModel>(list));

		mockMvc.perform(get("/workout-service/getAll").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void getById() throws Exception {
		WorkoutItemDTO itemDTO = new WorkoutItemDTO(null, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 12,
				4, new BigDecimal(90.00), "dropset", new BigDecimal(90.00), 150);
		List<WorkoutItemDTO> itemsDTO = new ArrayList<WorkoutItemDTO>();
		itemsDTO.add(itemDTO);

		var workoutDTO = new WorkoutDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "treino a",
				LocalDate.now(), itemsDTO);
		var workout = new WorkoutModel();
		BeanUtils.copyProperties(workoutDTO, workout);

		workout.setWorkoutItems(new ArrayList<WorkoutItemModel>());
		WorkoutItemModel workoutItemModel = new WorkoutItemModel();
		for (WorkoutItemDTO workoutItemDTO : workoutDTO.getWorkoutItems()) {
			workoutItemModel = new WorkoutItemModel();
			BeanUtils.copyProperties(workoutItemDTO, workoutItemModel);

			workout.getWorkoutItems().add(workoutItemModel);
		}

		when(workoutRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(workout));

		mockMvc.perform(get("/workout-service/getById/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void delete() throws Exception {
		WorkoutItemDTO itemDTO = new WorkoutItemDTO(null, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 12,
				4, new BigDecimal(90.00), "dropset", new BigDecimal(90.00), 150);
		List<WorkoutItemDTO> itemsDTO = new ArrayList<WorkoutItemDTO>();
		itemsDTO.add(itemDTO);

		var workoutDTO = new WorkoutDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "treino a",
				LocalDate.now(), itemsDTO);
		var workout = new WorkoutModel();
		BeanUtils.copyProperties(workoutDTO, workout);
		when(workoutRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(workout)).thenReturn(null);

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.delete("/workout-service/delete/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json")).andExpect(status().isOk());
	}

}
