package com.zidio.connect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zidio.connect.dto.OtpResponseDto;
import com.zidio.connect.dto.RegistrationRequestDTO;
import com.zidio.connect.dto.UserDto;
import com.zidio.connect.service.UserService;

import jakarta.persistence.EntityExistsException;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/email/register-otp")
	public ResponseEntity<?> sendOtp(@RequestParam("email") String userEmail) {
		// It is used in send otp and re-send otp. so, no need to add extra code.
		// If email already exists.
		if (userService.isUserExists(userEmail)) {
			throw new EntityExistsException("User with email " + userEmail + " already exists!!");
		}
		OtpResponseDto otpResponseDto = userService.otpToEmail(userEmail);
		return ResponseEntity.ok(otpResponseDto);
	}

	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(RegistrationRequestDTO registrationRequestDto) {
		UserDto userResponseDto = userService.createUser(registrationRequestDto);
		return ResponseEntity.ok(userResponseDto);
	}

	@DeleteMapping("/delete/id/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
		UserDto deletedUser = userService.deleteUserById(userId);
		return ResponseEntity.ok(deletedUser);
	}

	@DeleteMapping("/delete/email/{emailId}")
	public ResponseEntity<?> deleteUser(@PathVariable("emailId") String email) {
		UserDto deletedUser = userService.deleteUserByEmail(email);
		return ResponseEntity.ok(deletedUser);
	}

	@GetMapping("/get/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable("userId") Long id) {
		UserDto user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/get/email/{emailId}")
	public ResponseEntity<?> getUserByEmail(@PathVariable("emailId") String email) {
		UserDto user = userService.getUserByEmail(email);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/get")
	ResponseEntity<?> getAllUsers() {
		List<UserDto> allUsers = userService.getAllUsers();
		return ResponseEntity.ok(allUsers);
	}
}
