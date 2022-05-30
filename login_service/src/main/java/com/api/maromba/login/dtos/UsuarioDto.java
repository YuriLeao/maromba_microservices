package com.api.maromba.login.dtos;

import java.time.LocalDate;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UsuarioDto {

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
	@NotBlank
	private String empresaId;
	@NotNull
	private LocalDate dataNascimento;
	
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
	public String getEmpresaId() {
		return empresaId;
	}
	public void setEmpresaId(String empresaId) {
		this.empresaId = empresaId;
	}
	
}
