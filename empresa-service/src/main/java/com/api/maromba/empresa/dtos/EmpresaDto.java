package com.api.maromba.empresa.dtos;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

public class EmpresaDto {

	private UUID id;
	@NotBlank
	private String nome;
	@NotBlank
	private String cnpj;
	@NotBlank
	private String email;
	@NotBlank
	private String inscricaoEstadual;
	@NotBlank
	private String telefone;

	public EmpresaDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmpresaDto(String nome, String cnpj, String email, String inscricaoEstadual, String telefone) {
		super();
		this.nome = nome;
		this.cnpj = cnpj;
		this.email = email;
		this.inscricaoEstadual = inscricaoEstadual;
		this.telefone = telefone;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}
}
