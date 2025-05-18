package com.zidio.connect.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OtpException extends RuntimeException {

	private String message;
	private boolean success = false;
	private long timestamp = System.currentTimeMillis();
	private int status;

	public OtpException(String message, int status) {
		this.message = message;
		this.status = status;
	}
}