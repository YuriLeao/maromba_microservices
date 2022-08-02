package com.api.maromba.usuario.dtos;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UsuarioDto {

	private UUID id;
	@NotBlank
	private String usuario;
	@NotBlank
	private String senha;
	@NotBlank
	private String email;
	@NotBlank
	private String genero;
	@NotBlank
	private String telefone;
	@DecimalMin("30.00")
	private Double peso;
	@NotNull
	private UUID empresaId;
	private String empresaNome;
	@NotNull
	private LocalDate dataNascimento;
	private String token;

	public UsuarioDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UsuarioDto(@NotBlank String usuario, @NotBlank String senha, @NotBlank String email, @NotBlank String genero,
			@NotBlank String telefone, @DecimalMin("30.00") Double peso, @NotNull UUID empresaId,
			@NotNull LocalDate dataNascimento) {
		super();
		this.usuario = usuario;
		this.senha = senha;
		this.email = email;
		this.genero = genero;
		this.telefone = telefone;
		this.peso = peso;
		this.empresaId = empresaId;
		this.dataNascimento = dataNascimento;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate data_nascimento) {
		this.dataNascimento = data_nascimento;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public UUID getEmpresaId() {
		return empresaId;
	}

	public void setEmpresaId(UUID empresaId) {
		this.empresaId = empresaId;
	}

	public String getEmpresaNome() {
		return empresaNome;
	}

	public void setEmpresaNome(String empresaNome) {
		this.empresaNome = empresaNome;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
