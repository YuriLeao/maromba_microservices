package com.api.maromba.usuario;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.api.maromba.usuario.models.UsuarioModel;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class UsuarioControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	@Order(1)   
	public void incluir() throws Exception {
		UsuarioModel usuario = new UsuarioModel("teste", "teste", "tt@gmail.com", "M", "99999999", 72.0, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),  java.time.LocalDate.now());
		mockMvc.perform(post("/usuario-service/incluir").contentType("application/json")
	        .content(objectMapper.writeValueAsString(usuario)))
	        .andExpect(status().isCreated());
	}
	
	@Test
	@Order(2)   
	public void alterar() throws Exception {
		UsuarioModel usuario = new UsuarioModel("teste", "teste", "tt@gmail.com", "F", "99999999", 72.0, UUID.fromString("6abc9768-d3c7-47e0-845e-241a084ab34a"),  java.time.LocalDate.now());
		mockMvc.perform(put("/usuario-service/alterar/teste/teste").contentType("application/json")
	        .content(objectMapper.writeValueAsString(usuario)))
	        .andExpect(status().isCreated());
	}
	
	@Test
	@Order(3)
	public void login() throws Exception {
		mockMvc.perform(get("/usuario-service/login/teste/teste").contentType("application/json"))
	        .andExpect(status().isOk());
	}
	
	@Test
	public void obterTodos() throws Exception {
		mockMvc.perform(get("/usuario-service").contentType("application/json"))
	        .andExpect(status().isOk());
	}
	
	@Test
	public void deletar() throws Exception {
		mockMvc.perform(delete("/usuario-service/deletar/teste/teste").contentType("application/json"))
        .andExpect(status().isOk());
	}
	
}
