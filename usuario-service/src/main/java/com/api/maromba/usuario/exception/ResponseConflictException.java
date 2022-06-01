package com.api.maromba.usuario.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResponseConflictException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ResponseConflictException(String exception) {	
		super(exception) ;
	}

}
