package com.api.maromba.workout.exceptions;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ExceptionResponse implements Serializable{

	private static final long serialVersionUID = 1L;

	private LocalDateTime date;
	private String menssage;
	private String description;
	
	public ExceptionResponse (LocalDateTime date, String menssage, String description) {
		super();
		this.date = date;
		this.menssage = menssage;
		this.description = description;
	}

	public LocalDateTime getData() {
		return date;
	}

	public void setData(LocalDateTime data) {
		this.date = data;
	}

	public String getMensagem() {
		return menssage;
	}

	public void setMensagem(String mensagem) {
		this.menssage = mensagem;
	}

	public String getDescricao() {
		return description;
	}

	public void setDescricaos(String descricao) {
		this.description = descricao;
	}
}
