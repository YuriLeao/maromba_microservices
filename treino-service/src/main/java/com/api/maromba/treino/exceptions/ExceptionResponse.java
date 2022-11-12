package com.api.maromba.treino.exceptions;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ExceptionResponse implements Serializable{

	private static final long serialVersionUID = 1L;

	private LocalDateTime data;
	private String mensagem;
	private String descricao;
	
	public ExceptionResponse (LocalDateTime data, String mensagem, String descricao) {
		super();
		this.data = data;
		this.mensagem = mensagem;
		this.descricao = descricao;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricaos(String descricao) {
		this.descricao = descricao;
	}
}
