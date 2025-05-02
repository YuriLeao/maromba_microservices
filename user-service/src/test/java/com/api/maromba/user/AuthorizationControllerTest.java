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

import com.api.maromba.user.dtos.AuthorizationDTO;
import com.api.maromba.user.models.AuthorizationModel;
import com.api.maromba.user.repositories.AuthorizationRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthorizationRepository authorizationRepository;

	@Test
	public void getAll() throws Exception {
		var authorizationDTO = new AuthorizationDTO("AL", "Aluno");

		var authorization = new AuthorizationModel();
		BeanUtils.copyProperties(authorizationDTO, authorization);
		List<AuthorizationModel> list = new ArrayList<AuthorizationModel>();
		list.add(authorization);

		when(authorizationRepository.findAll()).thenReturn(list);

		mockMvc.perform(get("/authorization-service/getAll").contentType("application/json"))
				.andExpect(status().isOk());
	}

}
