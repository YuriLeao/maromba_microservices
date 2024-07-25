package com.api.maromba.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String description;

}
