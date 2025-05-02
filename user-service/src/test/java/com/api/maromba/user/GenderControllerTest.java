package com.api.maromba.user;

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

import com.api.maromba.user.dtos.GenderDTO;
import com.api.maromba.user.models.GenderModel;
import com.api.maromba.user.repositories.GenderRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class GenderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GenderRepository genderRepository;

	@Test
	public void getAll() throws Exception {
		var genderDTO = new GenderDTO("M", "Monstro");

		var gender = new GenderModel();
		BeanUtils.copyProperties(genderDTO, gender);
		List<GenderModel> list = new ArrayList<GenderModel>();
		list.add(gender);

		when(genderRepository.findAll()).thenReturn(list);

		mockMvc.perform(get("/gender-service/getAll").contentType("application/json")).andExpect(status().isOk());
	}

}
