package com.zidio.connect.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.angus.mail.smtp.SMTPSendFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {

		Map<String, Object> responseMessage = new LinkedHashMap<>();
		responseMessage.put("Message: ", ex.getMessage());
		responseMessage.put("Timestamp: ", LocalDateTime.now());
		responseMessage.put("Status: ", HttpStatus.NOT_FOUND);
		responseMessage.put("Success: ", false);

		return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EntityExistsException.class)
	public ResponseEntity<?> handleEntityExistsException(EntityExistsException ex) {

		Map<String, Object> responseMessage = new LinkedHashMap<>();
		responseMessage.put("Message: ", ex.getMessage());
		responseMessage.put("Timestamp: ", LocalDateTime.now());
		responseMessage.put("Status: ", HttpStatus.BAD_REQUEST);
		responseMessage.put("Success: ", false);

		return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<?> handleMessagingException(MessagingException ex) {
		Map<String, Object> responseMessage = new LinkedHashMap<>();
		responseMessage.put("Message: ", ex.getMessage());
		responseMessage.put("Timestamp: ", LocalDateTime.now());
		responseMessage.put("Status: ", HttpStatus.BAD_REQUEST);
		responseMessage.put("Success: ", false);

		return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(OtpException.class)
	public ResponseEntity<?> handleOtpException(OtpException ex) {
		Map<String, Object> responseMessage = new LinkedHashMap<>();
		responseMessage.put("Message: ", ex.getMessage());
		responseMessage.put("Timestamp: ", ex.getTimestamp());
		responseMessage.put("Status: ", ex.getStatus());
		responseMessage.put("Success: ", false);

		return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SMTPSendFailedException.class)
	public ResponseEntity<?> handleSMTPSendFailedException(SMTPSendFailedException ex) {
		Map<String, Object> responseMessage = new LinkedHashMap<>();
		responseMessage.put("Message: ", ex.getMessage());
		responseMessage.put("Timestamp: ", LocalDateTime.now());
		responseMessage.put("Status: ", 500);
		responseMessage.put("Success: ", false);

		return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
	}

}
