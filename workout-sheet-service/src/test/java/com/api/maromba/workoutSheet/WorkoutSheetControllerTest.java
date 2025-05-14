package com.api.maromba.workoutSheet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.api.maromba.workoutSheet.dtos.WorkoutDivisionDTO;
import com.api.maromba.workoutSheet.dtos.WorkoutExerciseDTO;
import com.api.maromba.workoutSheet.dtos.WorkoutSheetDTO;
import com.api.maromba.workoutSheet.models.WorkoutDivisionModel;
import com.api.maromba.workoutSheet.models.WorkoutExerciseModel;
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

	private WorkoutSheetTestData initializeTestData() {
		// Criação do exercício
		WorkoutExerciseDTO exerciseDTO = new WorkoutExerciseDTO(null,
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 12, 4, "dropset", 90);

		// Criação da divisão com exercício
		WorkoutDivisionDTO divisionDTO = new WorkoutDivisionDTO(null, "A", Collections.singletonList(exerciseDTO));

		// Criação da planilha com divisão
		WorkoutSheetDTO workoutSheetDTO = new WorkoutSheetDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				"treino iniciante", " ", Collections.singletonList(divisionDTO), null);

		// Conversão para Model
		WorkoutSheetModel workoutSheetModel = new WorkoutSheetModel();
		BeanUtils.copyProperties(workoutSheetDTO, workoutSheetModel);

		workoutSheetModel.setDivisions(workoutSheetDTO.getDivisions().stream().map(division -> {
			WorkoutDivisionModel divisionModel = new WorkoutDivisionModel();
			BeanUtils.copyProperties(division, divisionModel);

			divisionModel.setExercises(division.getExercises().stream().map(exercise -> {
				WorkoutExerciseModel exerciseModel = new WorkoutExerciseModel();
				BeanUtils.copyProperties(exercise, exerciseModel);
				return exerciseModel;
			}).collect(Collectors.toList()));

			return divisionModel;
		}).collect(Collectors.toList()));

		return new WorkoutSheetTestData(workoutSheetDTO, workoutSheetModel);
	}

	private static class WorkoutSheetTestData {
		final WorkoutSheetDTO workoutSheetDTO;
		final WorkoutSheetModel workoutSheetModel;

		WorkoutSheetTestData(WorkoutSheetDTO workoutSheetDTO, WorkoutSheetModel workoutSheetModel) {
			this.workoutSheetDTO = workoutSheetDTO;
			this.workoutSheetModel = workoutSheetModel;
		}
	}

	@Test
	public void save() throws Exception {
		WorkoutSheetTestData testData = initializeTestData();

		when(workoutSheetRepository.save(any(WorkoutSheetModel.class))).thenReturn(testData.workoutSheetModel);

		mockMvc.perform(post("/workout-sheet-service/save").contentType("application/json")
				.content(objectMapper.writeValueAsString(testData.workoutSheetDTO))).andExpect(status().isCreated());
	}

	@Test
	public void update() throws Exception {
		WorkoutSheetTestData testData = initializeTestData();

		when(workoutSheetRepository.findById(testData.workoutSheetDTO.getId()))
				.thenReturn(Optional.of(testData.workoutSheetModel));
		when(workoutSheetRepository.save(any(WorkoutSheetModel.class))).thenReturn(testData.workoutSheetModel);

		mockMvc.perform(put("/workout-sheet-service/update/" + testData.workoutSheetDTO.getId())
				.contentType("application/json").content(objectMapper.writeValueAsString(testData.workoutSheetDTO)))
				.andExpect(status().isCreated());
	}

	@Test
    public void getAll() throws Exception {
        WorkoutSheetTestData testData = initializeTestData();
        List<WorkoutSheetModel> list = Collections.singletonList(testData.workoutSheetModel);
        
        when(workoutSheetRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id"))))
            .thenReturn(new PageImpl<>(list));

        mockMvc.perform(get("/workout-sheet-service/getAll")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

	@Test
	public void getById() throws Exception {
		WorkoutSheetTestData testData = initializeTestData();

		when(workoutSheetRepository.findById(testData.workoutSheetDTO.getId()))
				.thenReturn(Optional.of(testData.workoutSheetModel));

		mockMvc.perform(get("/workout-sheet-service/getById/" + testData.workoutSheetDTO.getId())
				.contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void delete() throws Exception {
	    WorkoutSheetTestData testData = initializeTestData();
	    
	    when(workoutSheetRepository.findById(testData.workoutSheetDTO.getId()))
	        .thenReturn(Optional.of(testData.workoutSheetModel));

	    mockMvc.perform(MockMvcRequestBuilders.delete("/workout-sheet-service/delete/" + testData.workoutSheetDTO.getId())
	            .contentType("application/json"))
	            .andExpect(status().isOk());
	}
}
