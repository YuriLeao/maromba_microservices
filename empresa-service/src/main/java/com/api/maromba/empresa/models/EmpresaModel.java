package com.api.maromba.empresa.models;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "tb_empresa")
@Data
public class EmpresaModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(nullable = false, unique = true)
	private String nome;
	@Column(nullable = false)
	private String cnpj;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String inscricaoEstadual;
	@Column(nullable = false)
	private String telefone;

}
