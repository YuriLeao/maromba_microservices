package com.api.maromba.gateway.exception;

public class RoleException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String descricao;

	public RoleException(String descricao) {
		super();
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
