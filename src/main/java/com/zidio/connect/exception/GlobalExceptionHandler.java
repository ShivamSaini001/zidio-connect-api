package com.zidio.connect.exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.angus.mail.smtp.SMTPSendFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({ EntityNotFoundException.class, UsernameNotFoundException.class })
	public ResponseEntity<?> handleEntityNotFoundException(Exception ex) {

		Map<String, Object> responseMessage = new LinkedHashMap<>();
		responseMessage.put("message", ex.getMessage());
		responseMessage.put("timestamp", LocalDateTime.now());
		responseMessage.put("status", HttpStatus.NOT_FOUND);
		responseMessage.put("success", false);

		return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EntityExistsException.class)
	public ResponseEntity<?> handleEntityExistsException(EntityExistsException ex) {

		Map<String, Object> responseMessage = new LinkedHashMap<>();
		responseMessage.put("message", ex.getMessage());
		responseMessage.put("timestamp", LocalDateTime.now());
		responseMessage.put("status", HttpStatus.BAD_REQUEST);
		responseMessage.put("success", false);

		return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<?> handleMessagingException(MessagingException ex) {
		Map<String, Object> responseMessage = new LinkedHashMap<>();
		responseMessage.put("message", ex.getMessage());
		responseMessage.put("timestamp", LocalDateTime.now());
		responseMessage.put("status", HttpStatus.BAD_REQUEST);
		responseMessage.put("success", false);

		return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(OtpException.class)
	public ResponseEntity<?> handleOtpException(OtpException ex) {
		Map<String, Object> responseMessage = new LinkedHashMap<>();
		responseMessage.put("message", ex.getMessage());
		responseMessage.put("timestamp", ex.getTimestamp());
		responseMessage.put("status", ex.getStatus());
		responseMessage.put("success", false);

		return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SMTPSendFailedException.class)
	public ResponseEntity<?> handleSMTPSendFailedException(SMTPSendFailedException ex) {
		Map<String, Object> responseMessage = new LinkedHashMap<>();
		responseMessage.put("message", ex.getMessage());
		responseMessage.put("timestamp", LocalDateTime.now());
		responseMessage.put("status", 500);
		responseMessage.put("success", false);

		return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
		Map<String, Object> responseMessage = new LinkedHashMap<>();
		responseMessage.put("message", ex.getMessage());
		responseMessage.put("timestamp", LocalDateTime.now());
		responseMessage.put("status", HttpStatus.BAD_REQUEST.value());
		responseMessage.put("success", false);
		return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("message", ex.getMessage());
		body.put("timestamp", ex.getTimestamp());
		body.put("status", ex.getHttpStatus().value());
		return new ResponseEntity<>(body, ex.getHttpStatus());
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<Map<String, Object>> handleIOException(IOException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("message", ex.getMessage());
		body.put("timestamp", LocalDateTime.now());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("message", ex.getMessage());
		body.put("timestamp", LocalDateTime.now());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

}
