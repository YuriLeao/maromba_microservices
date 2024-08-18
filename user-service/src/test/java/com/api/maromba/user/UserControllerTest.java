package com.api.maromba.user;

import static org.mockito.ArgumentMatchers.any;
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

import com.api.maromba.user.dtos.AuthorizationDTO;
import com.api.maromba.user.dtos.CompanyDTO;
import com.api.maromba.user.dtos.GenderDTO;
import com.api.maromba.user.dtos.UserDTO;
import com.api.maromba.user.models.AuthorizationModel;
import com.api.maromba.user.models.GenderModel;
import com.api.maromba.user.models.UserModel;
import com.api.maromba.user.repositories.AuthorizationRepository;
import com.api.maromba.user.repositories.GenderRepository;
import com.api.maromba.user.repositories.UserRepository;
import com.api.maromba.user.util.Encrypt;
import com.api.maromba.user.util.JwtUtil;
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

	@MockBean
	private GenderRepository genderRepository;

	@MockBean
	private AuthorizationRepository authorizationRepository;

	@Autowired
	private Encrypt encrypt;

	@Autowired
	private JwtUtil jwtUtil;

	@Test
	public void save() throws Exception {
		var userDTO = new UserDTO(null, "tt@gmail.com", "teste", "09826915977", new GenderDTO("M", "Monstro"),
				"99999999", 72.0, new AuthorizationDTO("A", "Admin"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				new CompanyDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste", "99999999999999",
						"tt@gmail.com", "1234", "99999999"),
				java.time.LocalDate.now(), null);

		var user = new UserModel();
		BeanUtils.copyProperties(userDTO, user);
		user.setPassword(encrypt.encryptPassword("tt@gmail.com", "123#456@"));
		user.setGender(new GenderModel());
		BeanUtils.copyProperties(userDTO.getGender(), user.getGender());
		user.setAuthorization(new AuthorizationModel());
		BeanUtils.copyProperties(userDTO.getAuthorization(), user.getAuthorization());

		when(userRepository.existsByEmail("tt@gmail.com")).thenReturn(false);
		when(userRepository.save(user)).thenReturn(user);

		mockMvc.perform(post("/user-service/save").contentType("application/json")
				.content(objectMapper.writeValueAsString(userDTO))).andExpect(status().isCreated());
	}

	@Test
	public void update() throws Exception {
		var userDTO = new UserDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "tt@gmail.com", "teste",
				"09826915977", new GenderDTO("M", "Monstro"), "99999999", 72.0, new AuthorizationDTO("A", "Admin"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				new CompanyDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste", "99999999999999",
						"tt@gmail.com", "1234", "99999999"),
				java.time.LocalDate.now(), null);
		var user = new UserModel();
		BeanUtils.copyProperties(userDTO, user);
		user.setPassword(encrypt.encryptPassword("tt@gmail.com", "teste"));
		user.setGender(new GenderModel());
		BeanUtils.copyProperties(userDTO.getGender(), user.getGender());
		user.setAuthorization(new AuthorizationModel());
		BeanUtils.copyProperties(userDTO.getAuthorization(), user.getAuthorization());

		when(userRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(user));
		when(userRepository.save(any(UserModel.class))).thenReturn(user);

		mockMvc.perform(put("/user-service/update/6abc9768-d3c7-47e0-845e-241a084ab34a").contentType("application/json")
				.content(objectMapper.writeValueAsString(userDTO))).andExpect(status().isCreated());
	}

	@Test
	public void login() throws Exception {
		var userDTO = new UserDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "tt@gmail.com", "teste",
				"09826915977", new GenderDTO("M", "Monstro"), "99999999", 72.0, new AuthorizationDTO("A", "Admin"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				new CompanyDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste", "99999999999999",
						"tt@gmail.com", "1234", "99999999"),
				java.time.LocalDate.now(), null);
		var user = new UserModel();
		BeanUtils.copyProperties(userDTO, user);
		user.setPassword(encrypt.encryptPassword("tt@gmail.com", "teste"));
		user.setGender(new GenderModel());
		BeanUtils.copyProperties(userDTO.getGender(), user.getGender());
		user.setAuthorization(new AuthorizationModel());
		BeanUtils.copyProperties(userDTO.getAuthorization(), user.getAuthorization());

		when(userRepository.findByEmailAndPassword("tt@gmail.com", encrypt.encryptPassword("tt@gmail.com", "teste")))
				.thenReturn(Optional.of(user));

		mockMvc.perform(get("/user-service/login/tt@gmail.com/teste").contentType("application/json"))
				.andExpect(status().isOk());
	}

	@Test
	public void getAll() throws Exception {
		var userDTO = new UserDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "tt@gmail.com", "teste",
				"09826915977", new GenderDTO("M", "Monstro"), "99999999", 72.0, new AuthorizationDTO("A", "Admin"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				new CompanyDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste", "99999999999999",
						"tt@gmail.com", "1234", "99999999"),
				java.time.LocalDate.now(), null);
		var user = new UserModel();
		BeanUtils.copyProperties(userDTO, user);
		user.setGender(new GenderModel());
		BeanUtils.copyProperties(userDTO.getGender(), user.getGender());
		user.setAuthorization(new AuthorizationModel());
		BeanUtils.copyProperties(userDTO.getAuthorization(), user.getAuthorization());
		List<UserModel> list = new ArrayList<UserModel>();
		list.add(user);

		when(userRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id"))))
				.thenReturn(new PageImpl<UserModel>(list));

		mockMvc.perform(get("/user-service/getAll").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void getByCompanyIdAuthorization() throws Exception {
		var userDTO = new UserDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "tt@gmail.com", "teste",
				"09826915977", new GenderDTO("M", "Monstro"), "99999999", 72.0, new AuthorizationDTO("A", "Admin"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				new CompanyDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste", "99999999999999",
						"tt@gmail.com", "1234", "99999999"),
				java.time.LocalDate.now(), null);
		var user = new UserModel();
		BeanUtils.copyProperties(userDTO, user);
		user.setGender(new GenderModel());
		BeanUtils.copyProperties(userDTO.getGender(), user.getGender());
		user.setAuthorization(new AuthorizationModel());
		BeanUtils.copyProperties(userDTO.getAuthorization(), user.getAuthorization());
		List<UserModel> list = new ArrayList<UserModel>();
		list.add(user);

		List<AuthorizationModel> authorizations = new ArrayList<AuthorizationModel>();
		AuthorizationModel authorizationModelAdmin = new AuthorizationModel();
		authorizationModelAdmin.setId("A");
		authorizationModelAdmin.setDescription("Admin");
		AuthorizationModel authorizationModelCompany = new AuthorizationModel();
		authorizationModelCompany.setId("E");
		authorizationModelCompany.setDescription("Empresa");
		AuthorizationModel authorizationModelTeacher = new AuthorizationModel();
		authorizationModelTeacher.setId("P");
		authorizationModelTeacher.setDescription("Professor");
		AuthorizationModel authorizationModelStudent = new AuthorizationModel();
		authorizationModelStudent.setId("AL");
		authorizationModelStudent.setDescription("AL");
		authorizations.add(authorizationModelAdmin);
		authorizations.add(authorizationModelCompany);
		authorizations.add(authorizationModelTeacher);
		authorizations.add(authorizationModelStudent);

		when(userRepository.findByCompanyIdAndInAuthorization(
				PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id")), userDTO.getId(), authorizations))
				.thenReturn(new PageImpl<UserModel>(list));

		var token = jwtUtil.generateToken(user, "/user-service/login");

		mockMvc.perform(get("/user-service/getByCompanyIdAuthorization/6abc9768-d3c7-47e0-845e-241a084ab34a")
				.header("Authorization", "Bearer " + token).contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void getByNameLike() throws Exception {
		var userDTO = new UserDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "tt@gmail.com", "teste",
				"09826915977", new GenderDTO("M", "Monstro"), "99999999", 72.0, new AuthorizationDTO("A", "Admin"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				new CompanyDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste", "99999999999999",
						"tt@gmail.com", "1234", "99999999"),
				java.time.LocalDate.now(), null);
		var user = new UserModel();
		BeanUtils.copyProperties(userDTO, user);
		user.setGender(new GenderModel());
		BeanUtils.copyProperties(userDTO.getGender(), user.getGender());
		user.setAuthorization(new AuthorizationModel());
		BeanUtils.copyProperties(userDTO.getAuthorization(), user.getAuthorization());
		List<UserModel> list = new ArrayList<UserModel>();
		list.add(user);

		when(userRepository.findByNameLike(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id")),
				userDTO.getName())).thenReturn(new PageImpl<UserModel>(list));

		mockMvc.perform(get("/user-service/getByNameLike/teste").contentType("application/json"))
				.andExpect(status().isOk());
	}

	@Test
	public void delete() throws Exception {
		var userDTO = new UserDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "tt@gmail.com", "teste",
				"09826915977", new GenderDTO("M", "Monstro"), "99999999", 72.0, new AuthorizationDTO("A", "Admin"),
				UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),
				new CompanyDTO(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste", "99999999999999",
						"tt@gmail.com", "1234", "99999999"),
				java.time.LocalDate.now(), null);
		var user = new UserModel();
		BeanUtils.copyProperties(userDTO, user);
		user.setGender(new GenderModel());
		BeanUtils.copyProperties(userDTO.getGender(), user.getGender());
		user.setAuthorization(new AuthorizationModel());
		BeanUtils.copyProperties(userDTO.getAuthorization(), user.getAuthorization());

		when(userRepository.findById(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a")))
				.thenReturn(Optional.of(user)).thenReturn(null);

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.delete("/user-service/delete/6abc9768-d3c7-47e0-845e-241a084ab34a").contentType("application/json"))
				.andExpect(status().isOk());
	}

	@Test
	public void getAllGenders() throws Exception {
		var genderDTO = new GenderDTO("M", "Monstro");

		var gender = new GenderModel();
		BeanUtils.copyProperties(genderDTO, gender);
		List<GenderModel> list = new ArrayList<GenderModel>();
		list.add(gender);

		when(genderRepository.findAll()).thenReturn(list);

		mockMvc.perform(get("/user-service/getAllGenders").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void getAllAuthorizations() throws Exception {
		var authorizationDTO = new AuthorizationDTO("AL", "Aluno");

		var authorization = new AuthorizationModel();
		BeanUtils.copyProperties(authorizationDTO, authorization);
		List<AuthorizationModel> list = new ArrayList<AuthorizationModel>();
		list.add(authorization);

		when(authorizationRepository.findAll()).thenReturn(list);

		mockMvc.perform(get("/user-service/getAllAuthorizations").contentType("application/json"))
				.andExpect(status().isOk());
	}

}
