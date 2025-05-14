package com.api.maromba.executedWorkout;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.api.maromba.executedWorkout.dtos.ExecutedDivisionDTO;
import com.api.maromba.executedWorkout.dtos.ExecutedExerciseDTO;
import com.api.maromba.executedWorkout.dtos.ExecutedRepetitionDTO;
import com.api.maromba.executedWorkout.dtos.ExecutedWorkoutDTO;
import com.api.maromba.executedWorkout.models.ExecutedDivisionModel;
import com.api.maromba.executedWorkout.models.ExecutedExerciseModel;
import com.api.maromba.executedWorkout.models.ExecutedRepetitionModel;
import com.api.maromba.executedWorkout.models.ExecutedWorkoutModel;
import com.api.maromba.executedWorkout.models.enums.ExecutionStatus;
import com.api.maromba.executedWorkout.repositories.ExecutedWorkoutRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ExecutedWorkoutControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ExecutedWorkoutRepository executedWorkoutRepository;

	private ExecutedWorkoutTestData initializeTestData() {
		ExecutedRepetitionDTO repetitionDTO = new ExecutedRepetitionDTO(null, 12, 4, new BigDecimal(40),
				ExecutionStatus.CREATED);

		ExecutedExerciseDTO exerciseDTO = new ExecutedExerciseDTO(null,
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), 12, 4, 90, "dropset", ExecutionStatus.CREATED,
				Collections.singletonList(repetitionDTO));

		ExecutedDivisionDTO divisionDTO = new ExecutedDivisionDTO(null, "A", ExecutionStatus.CREATED,
				Collections.singletonList(exerciseDTO));

		ExecutedWorkoutDTO workoutDTO = new ExecutedWorkoutDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				"treino iniciante", " ", Collections.singletonList(divisionDTO),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), LocalDateTime.now(), LocalDateTime.now(),
				true);

		ExecutedWorkoutModel workoutModel = new ExecutedWorkoutModel();
		BeanUtils.copyProperties(workoutDTO, workoutModel);

		workoutModel.setDivisions(workoutDTO.getDivisions().stream().map(division -> {
			ExecutedDivisionModel divisionModel = new ExecutedDivisionModel();
			BeanUtils.copyProperties(division, divisionModel);

			divisionModel.setExecutedExercises(division.getExecutedExercises().stream().map(exercise -> {
				ExecutedExerciseModel exerciseModel = new ExecutedExerciseModel();
				BeanUtils.copyProperties(exercise, exerciseModel);
				
				exerciseModel.setExecutedRepetitions(exercise.getExecutedRepetitions().stream().map(repetition -> {
					ExecutedRepetitionModel repetitionModel = new ExecutedRepetitionModel();
					BeanUtils.copyProperties(repetition, repetitionModel);
					return repetitionModel;
				}).collect(Collectors.toList()));
				
				return exerciseModel;
			}).collect(Collectors.toList()));

			return divisionModel;
		}).collect(Collectors.toList()));

		return new ExecutedWorkoutTestData(workoutDTO, workoutModel);
	}

	private static class ExecutedWorkoutTestData {
		final ExecutedWorkoutDTO executedWorkoutDTO;
		final ExecutedWorkoutModel executedWorkoutModel;

		ExecutedWorkoutTestData(ExecutedWorkoutDTO executedWorkoutDTO, ExecutedWorkoutModel ExecutedWorkoutModel) {
			this.executedWorkoutDTO = executedWorkoutDTO;
			this.executedWorkoutModel = ExecutedWorkoutModel;
		}
	}

	@Test
	public void save() throws Exception {
		ExecutedWorkoutTestData testData = initializeTestData();

		when(executedWorkoutRepository.save(any(ExecutedWorkoutModel.class))).thenReturn(testData.executedWorkoutModel);

		mockMvc.perform(post("/executed-workout-service/save").contentType("application/json")
				.content(objectMapper.writeValueAsString(testData.executedWorkoutDTO))).andExpect(status().isCreated());
	}

	@Test
	public void update() throws Exception {
		ExecutedWorkoutTestData testData = initializeTestData();

		when(executedWorkoutRepository.findById(testData.executedWorkoutDTO.getId()))
				.thenReturn(Optional.of(testData.executedWorkoutModel));
		when(executedWorkoutRepository.save(any(ExecutedWorkoutModel.class))).thenReturn(testData.executedWorkoutModel);

		mockMvc.perform(put("/executed-workout-service/update/" + testData.executedWorkoutDTO.getId())
				.contentType("application/json").content(objectMapper.writeValueAsString(testData.executedWorkoutDTO)))
				.andExpect(status().isCreated());
	}

	@Test
	public void getById() throws Exception {
		ExecutedWorkoutTestData testData = initializeTestData();

		when(executedWorkoutRepository.findById(testData.executedWorkoutDTO.getId()))
				.thenReturn(Optional.of(testData.executedWorkoutModel));

		mockMvc.perform(get("/executed-workout-service/getById/" + testData.executedWorkoutDTO.getId())
				.contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void delete() throws Exception {
		ExecutedWorkoutTestData testData = initializeTestData();

		when(executedWorkoutRepository.findById(testData.executedWorkoutDTO.getId()))
				.thenReturn(Optional.of(testData.executedWorkoutModel));

		mockMvc.perform(
				MockMvcRequestBuilders.delete("/executed-workout-service/delete/" + testData.executedWorkoutDTO.getId())
						.contentType("application/json"))
				.andExpect(status().isOk());
	}

}
