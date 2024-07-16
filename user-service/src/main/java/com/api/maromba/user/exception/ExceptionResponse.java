package com.api.maromba.user.exception;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse implements Serializable{

	private static final long serialVersionUID = 1L;

	private LocalDateTime date;
	private String message;
	private String description;
}
