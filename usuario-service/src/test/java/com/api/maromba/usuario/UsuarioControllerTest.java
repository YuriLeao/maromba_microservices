package com.api.maromba.usuario;

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

import com.api.maromba.usuario.dtos.UsuarioDto;
import com.api.maromba.usuario.models.UsuarioModel;
import com.api.maromba.usuario.repositories.UsuarioRepository;
import com.api.maromba.usuario.util.Criptor;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UsuarioRepository usuarioRepository;

	@Autowired
	private Criptor criptor;

	@Test
	public void incluir() throws Exception {
		var usuarioDto = new UsuarioDto(null, "teste", "teste", "tt@gmail.com", "M", "99999999", 72.0,
				new ArrayList<String>() {
					private static final long serialVersionUID = 1L;

					{
						add("admin");
					}
				}, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "empresa", java.time.LocalDate.now(), null);

		var usuario = new UsuarioModel();
		BeanUtils.copyProperties(usuarioDto, usuario);
		usuario.setSenha(criptor.criptografarSenha("teste", "teste"));
		when(usuarioRepository.existsByUsuario("teste")).thenReturn(false);
		when(usuarioRepository.save(usuario)).thenReturn(usuario);

		mockMvc.perform(post("/usuario-service/incluir").contentType("application/json")
				.content(objectMapper.writeValueAsString(usuarioDto))).andExpect(status().isCreated());
	}

	@Test
	public void alterar() throws Exception {
		var usuarioDto = new UsuarioDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste", "teste", "tt@gmail.com", "M", "99999999", 72.0, new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("admin");
			}
		}, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "empresa", java.time.LocalDate.now(), null);
		var usuario = new UsuarioModel();
		BeanUtils.copyProperties(usuarioDto, usuario);
		usuario.setSenha(criptor.criptografarSenha("teste", "teste"));
		when(usuarioRepository.findByUsuarioAndSenha("teste", criptor.criptografarSenha("teste", "teste")))
				.thenReturn(Optional.of(usuario));
		when(usuarioRepository.save(usuario)).thenReturn(usuario);

		mockMvc.perform(put("/usuario-service/alterar/teste/teste").contentType("application/json")
				.content(objectMapper.writeValueAsString(usuarioDto))).andExpect(status().isCreated());
	}

	@Test
	public void login() throws Exception {
		var usuarioDto = new UsuarioDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste", "teste", "tt@gmail.com", "M", "99999999", 72.0, new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("admin");
			}
		}, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "empresa", java.time.LocalDate.now(), null);
		var usuario = new UsuarioModel();
		BeanUtils.copyProperties(usuarioDto, usuario);
		usuario.setSenha(criptor.criptografarSenha("teste", "teste"));
		when(usuarioRepository.findByUsuarioAndSenha("teste", criptor.criptografarSenha("teste", "teste")))
				.thenReturn(Optional.of(usuario));

		mockMvc.perform(get("/usuario-service/login/teste/teste").contentType("application/json"))
				.andExpect(status().isOk());
	}

	@Test
	public void obterTodos() throws Exception {
		var usuarioDto = new UsuarioDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste", "teste", "tt@gmail.com", "M", "99999999", 72.0, new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("admin");
			}
		}, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "empresa", java.time.LocalDate.now(), null);
		var usuario = new UsuarioModel();
		BeanUtils.copyProperties(usuarioDto, usuario);
		List<UsuarioModel> lista = new ArrayList<UsuarioModel>();
		lista.add(usuario);
		when(usuarioRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by(Sort.Direction.ASC, "id"))))
				.thenReturn(new PageImpl<UsuarioModel>(lista));

		mockMvc.perform(get("/usuario-service").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void deletar() throws Exception {
		var usuarioDto = new UsuarioDto(UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "teste", "teste", "tt@gmail.com", "M", "99999999", 72.0, new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("admin");
			}
		}, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"), "empresa", java.time.LocalDate.now(), null);
		var usuario = new UsuarioModel();
		BeanUtils.copyProperties(usuarioDto, usuario);
		when(usuarioRepository.findByUsuarioAndSenha("teste", criptor.criptografarSenha("teste", "teste")))
				.thenReturn(Optional.of(usuario)).thenReturn(null);

		mockMvc.perform(delete("/usuario-service/deletar/teste/teste").contentType("application/json"))
				.andExpect(status().isOk());
	}

}
