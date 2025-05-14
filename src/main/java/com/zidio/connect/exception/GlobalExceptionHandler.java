package com.zidio.connect.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {

		Map<String, Object> responseMessage = new LinkedHashMap<>();
		responseMessage.put("Message: ", "Entity not found!!");
		responseMessage.put("Details: ", ex.getMessage());
		responseMessage.put("Timestamp: ", LocalDateTime.now());
		responseMessage.put("Status: ", HttpStatus.NOT_FOUND);
		responseMessage.put("Success: ", false);
		
		return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(EntityExistsException.class)
	public ResponseEntity<?> handleEntityExistsException(EntityExistsException ex) {

		Map<String, Object> responseMessage = new LinkedHashMap<>();
		responseMessage.put("Message: ", "Entity already exists!!");
		responseMessage.put("Details: ", ex.getMessage());
		responseMessage.put("Timestamp: ", LocalDateTime.now());
		responseMessage.put("Status: ", HttpStatus.BAD_REQUEST);
		responseMessage.put("Success: ", false);
		
		return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
	}
}
