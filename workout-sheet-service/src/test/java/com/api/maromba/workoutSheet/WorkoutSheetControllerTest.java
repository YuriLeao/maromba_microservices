package com.api.maromba.workoutSheet;

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

import com.api.maromba.workoutSheet.dtos.WorkoutSheetDTO;
import com.api.maromba.workoutSheet.models.WorkoutSheetModel;
import com.api.maromba.workoutSheet.repositories.WorkoutSheetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkoutSheetControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private WorkoutSheetRepository workoutSheetRepository;

	@Test
	public void save() throws Exception {
		List<UUID> workouts = new ArrayList<UUID>();
		workouts.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));

		var workoutSheetDTO = new WorkoutSheetDTO(null, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				workouts, java.time.LocalDate.now(), true, "dsfs");
		var workoutSheet = new WorkoutSheetModel();
		BeanUtils.copyProperties(workoutSheetDTO, workoutSheet);
		when(workoutSheetRepository.save(workoutSheet)).thenReturn(workoutSheet);

		mockMvc.perform(post("/workout-sheet-service/save").contentType("application/json")
				.content(objectMapper.writeValueAsString(workoutSheetDTO))).andExpect(status().isCreated());
	}

	@Test
	public void update() throws Exception {
		List<UUID> workouts = new ArrayList<UUID>();
		workouts.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));

		var workoutSheetDTO = new WorkoutSheetDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), workouts, java.time.LocalDate.now(), true,
				"dsfs");
		var workoutSheet = new WorkoutSheetModel();
		BeanUtils.copyProperties(workoutSheetDTO, workoutSheet);
		when(workoutSheetRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(workoutSheet));
		when(workoutSheetRepository.save(workoutSheet)).thenReturn(workoutSheet);

		mockMvc.perform(put("/workout-sheet-service/update/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json").content(objectMapper.writeValueAsString(workoutSheetDTO)))
				.andExpect(status().isCreated());
	}

	@Test
	public void getAll() throws Exception {
		List<UUID> workouts = new ArrayList<UUID>();
		workouts.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));

		var workoutSheetDTO = new WorkoutSheetDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), workouts, java.time.LocalDate.now(), true,
				"dsfs");
		var workoutSheet = new WorkoutSheetModel();
		BeanUtils.copyProperties(workoutSheetDTO, workoutSheet);
		List<WorkoutSheetModel> list = new ArrayList<WorkoutSheetModel>();
		list.add(workoutSheet);
		when(workoutSheetRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id"))))
				.thenReturn(new PageImpl<WorkoutSheetModel>(list));

		mockMvc.perform(get("/workout-sheet-service/getAll").contentType("application/json"))
				.andExpect(status().isOk());
	}

	@Test
	public void getById() throws Exception {
		List<UUID> workouts = new ArrayList<UUID>();
		workouts.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));

		var workoutSheetDTO = new WorkoutSheetDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), workouts, java.time.LocalDate.now(), true,
				"dsfs");
		var workoutSheet = new WorkoutSheetModel();
		BeanUtils.copyProperties(workoutSheetDTO, workoutSheet);
		when(workoutSheetRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(workoutSheet));

		mockMvc.perform(get("/workout-sheet-service/getById/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void delete() throws Exception {
		List<UUID> workouts = new ArrayList<UUID>();
		workouts.add(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"));

		var workoutSheetDTO = new WorkoutSheetDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), workouts, java.time.LocalDate.now(), true,
				"dsfs");
		var workoutSheet = new WorkoutSheetModel();
		BeanUtils.copyProperties(workoutSheetDTO, workoutSheet);
		when(workoutSheetRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(workoutSheet)).thenReturn(null);

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.delete("/workout-sheet-service/delete/" + UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"))
				.contentType("application/json")).andExpect(status().isOk());
	}

}
