package com.zidio.connect.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zidio.connect.config.security.jwt.dto.JwtLoginRequest;
import com.zidio.connect.config.security.jwt.dto.JwtLoginResponse;
import com.zidio.connect.dto.OtpResponseDto;
import com.zidio.connect.dto.RegistrationRequestDTO;
import com.zidio.connect.service.UserService;

import jakarta.persistence.EntityExistsException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	// ************ User Registration process Start *******************
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
	public ResponseEntity<String> registerUser(@RequestBody RegistrationRequestDTO registrationRequestDto) {
		userService.createUser(registrationRequestDto);
		return ResponseEntity.ok("Registration Successfully...");
	}
	// *********** User Registration Process End ****************

	// ************ Forget Password Start *******************

	// -------------------

	// ************ Forget Password End *********************

	// ************ User Login process Start *******************

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody JwtLoginRequest loginRequest) {
		JwtLoginResponse loginResponse = userService.loginUser(loginRequest);
		return ResponseEntity.ok(loginResponse);
	}

	// ************ User Login Process End *********************

}
