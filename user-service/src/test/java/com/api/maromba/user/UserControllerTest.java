package com.api.maromba.user;

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

import com.api.maromba.user.dtos.UserDto;
import com.api.maromba.user.models.UserModel;
import com.api.maromba.user.repositories.UserRepository;
import com.api.maromba.user.util.Encrypt;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private Encrypt encrypt;

	@Test
	public void include() throws Exception {
		var userDto = new UserDto(null,"tt@gmail.com", "teste", "teste", "M", "99999999", 72.0,
				new ArrayList<String>() {
					private static final long serialVersionUID = 1L;

					{
						add("admin");
					}
				}, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "company", java.time.LocalDate.now(), null);

		var user = new UserModel();
		BeanUtils.copyProperties(userDto, user);
		user.setPassword(encrypt.encryptPassword("tt@gmail.com", "teste"));
		when(userRepository.existsByEmail("tt@gmail.com")).thenReturn(false);
		when(userRepository.save(user)).thenReturn(user);

		mockMvc.perform(post("/user-service/include").contentType("application/json")
				.content(objectMapper.writeValueAsString(userDto))).andExpect(status().isCreated());
	}

	@Test
	public void update() throws Exception {
		var userDto = new UserDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "tt@gmail.com", "teste", "teste", "M", "99999999", 72.0, new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("admin");
			}
		}, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "company", java.time.LocalDate.now(), null);
		var user = new UserModel();
		BeanUtils.copyProperties(userDto, user);
		user.setPassword(encrypt.encryptPassword("tt@gmail.com", "teste"));
		when(userRepository.findByEmailAndPassword("tt@gmail.com", encrypt.encryptPassword("tt@gmail.com", "teste")))
				.thenReturn(Optional.of(user));
		when(userRepository.save(user)).thenReturn(user);

		mockMvc.perform(put("/user-service/update/tt@gmail.com/teste").contentType("application/json")
				.content(objectMapper.writeValueAsString(userDto))).andExpect(status().isCreated());
	}

	@Test
	public void login() throws Exception {
		var userDto = new UserDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste", "teste", "teste", "M", "99999999", 72.0, new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("admin");
			}
		}, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "company", java.time.LocalDate.now(), null);
		var user = new UserModel();
		BeanUtils.copyProperties(userDto, user);
		user.setPassword(encrypt.encryptPassword("tt@gmail.com", "teste"));
		when(userRepository.findByEmailAndPassword("tt@gmail.com", encrypt.encryptPassword("tt@gmail.com", "teste")))
				.thenReturn(Optional.of(user));

		mockMvc.perform(get("/user-service/login/tt@gmail.com/teste").contentType("application/json"))
				.andExpect(status().isOk());
	}

	@Test
	public void getAll() throws Exception {
		var userDto = new UserDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "tt@gmail.com", "teste", "teste", "M", "99999999", 72.0, new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("admin");
			}
		}, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "company", java.time.LocalDate.now(), null);
		var user = new UserModel();
		BeanUtils.copyProperties(userDto, user);
		List<UserModel> list = new ArrayList<UserModel>();
		list.add(user);
		when(userRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id"))))
				.thenReturn(new PageImpl<UserModel>(list));

		mockMvc.perform(get("/user-service/getAll").contentType("application/json")).andExpect(status().isOk());
	}
	
	@Test
	public void getByNameLike() throws Exception {
		var userDto = new UserDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "tt@gmail.com", "teste", "teste", "M", "99999999", 72.0, new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("admin");
			}
		}, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "company", java.time.LocalDate.now(), null);
		var user = new UserModel();
		BeanUtils.copyProperties(userDto, user);
		List<UserModel> list = new ArrayList<UserModel>();
		list.add(user);
		when(userRepository.findByNameLike(userDto.getName()))
				.thenReturn(list);

		mockMvc.perform(get("/user-service/getByNameLike/teste").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void delet() throws Exception {
		var userDto = new UserDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "tt@gmail.com", "teste", "teste", "M", "99999999", 72.0, new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("admin");
			}
		}, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "company", java.time.LocalDate.now(), null);
		var user = new UserModel();
		BeanUtils.copyProperties(userDto, user);
		when(userRepository.findByEmailAndPassword("tt@gmail.com", encrypt.encryptPassword("tt@gmail.com", "teste")))
				.thenReturn(Optional.of(user)).thenReturn(null);

		mockMvc.perform(delete("/user-service/delete/tt@gmail.com/teste").contentType("application/json"))
				.andExpect(status().isOk());
	}

}
