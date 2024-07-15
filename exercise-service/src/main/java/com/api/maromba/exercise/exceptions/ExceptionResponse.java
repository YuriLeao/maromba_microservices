package com.api.maromba.exercise.exceptions;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ExceptionResponse implements Serializable{

	private static final long serialVersionUID = 1L;

	private LocalDateTime date;
	private String message;
	private String description;
	
	public ExceptionResponse (LocalDateTime date, String message, String description) {
		super();
		this.date = date;
		this.message = message;
		this.description = description;
	}

	public LocalDateTime getData() {
		return date;
	}

	public void setData(LocalDateTime data) {
		this.date = data;
	}

	public String getMensagem() {
		return message;
	}

	public void setMensagem(String mensagem) {
		this.message = mensagem;
	}

	public String getDescricao() {
		return description;
	}

	public void setDescricaos(String descricao) {
		this.description = descricao;
	}
}
