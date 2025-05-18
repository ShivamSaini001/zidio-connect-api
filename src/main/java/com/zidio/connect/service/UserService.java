package com.zidio.connect.service;

import java.util.List;

import com.zidio.connect.dto.OtpResponseDto;
import com.zidio.connect.dto.UserRequestDTO;
import com.zidio.connect.dto.UserResponseDTO;

public interface UserService {

	OtpResponseDto otpToEmail(String email);
	UserResponseDTO createUser(UserRequestDTO userRequestDto);
	UserResponseDTO deleteUserByEmail(String email);
	UserResponseDTO deleteUserById(Long id);
	UserResponseDTO getUserById(Long id);
	UserResponseDTO getUserByEmail(String email);
	boolean isUserExists(String email);
	List<UserResponseDTO> getAllUsers();
}
