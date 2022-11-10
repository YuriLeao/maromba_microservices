package com.api.maromba.exercicio.models;

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
@Table(name = "tb_exercicio_")
@Data
public class ExercicioModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(nullable = false)
	private UUID idExercicio;
	@Column(nullable = false)
	private Integer tempoDescanso;
	@Column(nullable = false)
	private Integer qtdSeries;
	@Column(nullable = false)
	private Integer qtdRepeticoes;
	@Column(nullable = false)
	private Integer carga;
	@Column(nullable = false)
	private Integer recordCarga;
	@Column(nullable = false)
	private String observacao;

}
