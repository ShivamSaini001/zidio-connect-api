package com.zidio.connect.service;

import java.util.List;

import com.zidio.connect.config.security.jwt.dto.JwtLoginRequest;
import com.zidio.connect.config.security.jwt.dto.JwtLoginResponse;
import com.zidio.connect.dto.OtpResponseDto;
import com.zidio.connect.dto.RegistrationRequestDTO;
import com.zidio.connect.dto.UserDto;

public interface UserService {

	OtpResponseDto otpToEmail(String email);
	UserDto createUser(RegistrationRequestDTO userRequestDto);
	JwtLoginResponse loginUser(JwtLoginRequest loginRequest);
	UserDto deleteUserByEmail(String email);
	UserDto deleteUserById(Long id);
	UserDto getUserById(Long id);
	UserDto getUserByEmail(String email);
	boolean isUserExists(String email);
	List<UserDto> getAllUsers();
}
