package com.api.maromba.exercise;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.api.maromba.exercise.dtos.MuscleGroupDTO;
import com.api.maromba.exercise.models.MuscleGroupModel;
import com.api.maromba.exercise.repositories.MuscleGroupRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class MuscleGroupControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private MuscleGroupRepository muscleGroupRepository;
	
	@Test
	public void getAll() throws Exception {
		var muscleGroupDTO = new MuscleGroupDTO("P", "Peito");

		var muscleGroup = new MuscleGroupModel();
		BeanUtils.copyProperties(muscleGroupDTO, muscleGroup);
		List<MuscleGroupModel> list = new ArrayList<MuscleGroupModel>();
		list.add(muscleGroup);

		when(muscleGroupRepository.findAll()).thenReturn(list);

		mockMvc.perform(get("/muscleGroup-service/getAll").contentType("application/json")).andExpect(status().isOk());
	}

}
