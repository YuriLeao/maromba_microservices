package com.api.maromba.movimento.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResponseNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ResponseNotFoundException(String exception) {	
		super(exception) ;
	}
}
