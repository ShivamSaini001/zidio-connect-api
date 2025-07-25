package com.zidio.connect.config.security.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class JwtValidationException extends RuntimeException {

	private String message;

	public JwtValidationException(String message){
		this.message = message;
	}
}
