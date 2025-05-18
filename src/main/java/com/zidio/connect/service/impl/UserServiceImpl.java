package com.zidio.connect.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zidio.connect.dto.OtpResponseDto;
import com.zidio.connect.dto.UserRequestDTO;
import com.zidio.connect.dto.UserResponseDTO;
import com.zidio.connect.entities.Role;
import com.zidio.connect.entities.User;
import com.zidio.connect.enums.RoleType;
import com.zidio.connect.repository.UserRepository;
import com.zidio.connect.service.OTPService;
import com.zidio.connect.service.RoleService;
import com.zidio.connect.service.UserService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	RoleService roleService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	OTPService otpService;

	@Override
	public UserResponseDTO createUser(UserRequestDTO userRequestDto) {
		if (userRequestDto != null) {
			// If user already exists.
			userRepo.findAll().stream().forEach((user) -> {
				if (user.getEmail().equals(userRequestDto.getEmail())) {
					throw new EntityExistsException(
							"User with email '" + userRequestDto.getEmail() + "' already exists!!");
				}
			});

			// Verify OTP
			String otp = userRequestDto.getVerificationCode();

			boolean verifyOtp = otpService.verifyOtp(userRequestDto.getEmail(), otp);
			if (verifyOtp) {
				// else
				User newUser = modelMapper.map(userRequestDto, User.class);
				newUser.setCreatedAt(LocalDateTime.now());
				newUser.setUpdatedAt(LocalDateTime.now());

				// Assign profile type to user.
				Role role = roleService.getRoleByName("ROLE_" + userRequestDto.getSelectedRole());
				newUser.setProfileType(role);

				// Assign roles to user.
				List<Role> userRoles = this.getUserRoles(role);
				newUser.setRole(userRoles);

				// Save User into DB.
				User createdUser = userRepo.save(newUser);

				UserResponseDTO userResponseDto = modelMapper.map(createdUser, UserResponseDTO.class);
				userResponseDto.setRoleName(createdUser.getProfileType().getName());

				// clear user
				otpService.clearUserDetails(createdUser.getEmail());
				return userResponseDto;
			}
		}
		return null;
	}

	public List<Role> getUserRoles(Role role) {
		String roleName = role.getName();
		List<Role> allRoles = roleService.getAllRoles();
		List<Role> assignRoles = new ArrayList<>();

		if (roleName.equals("ROLE_" + RoleType.ADMIN.toString())) {
			allRoles.stream().forEach((r) -> {
				if (r.getName().equals("ROLE_" + RoleType.ADMIN.toString())
						|| r.getName().equals("ROLE_" + RoleType.RECRUITER.toString())
						|| r.getName().equals("ROLE_" + RoleType.TEACHER.toString())
						|| r.getName().equals("ROLE_" + RoleType.STUDENT.toString())) {
					assignRoles.add(r);
				}
			});
		} else if (roleName.equals("ROLE_" + RoleType.RECRUITER.toString())) {
			allRoles.stream().forEach((r) -> {
				if (r.getName().equals("ROLE_" + RoleType.RECRUITER.toString())
						|| r.getName().equals("ROLE_" + RoleType.TEACHER.toString())
						|| r.getName().equals("ROLE_" + RoleType.STUDENT.toString())) {
					assignRoles.add(r);
				}
			});
		} else if (roleName.equals("ROLE_" + RoleType.TEACHER.toString())) {
			allRoles.stream().forEach((r) -> {
				if (r.getName().equals("ROLE_" + RoleType.TEACHER.toString())
						|| r.getName().equals("ROLE_" + RoleType.STUDENT.toString())) {
					assignRoles.add(r);
				}
			});
		} else if (roleName.equals("ROLE_" + RoleType.STUDENT.toString())) {
			allRoles.stream().forEach((r) -> {
				if (r.getName().equals("ROLE_" + RoleType.STUDENT.toString())) {
					assignRoles.add(r);
				}
			});
		}
		return assignRoles;
	}

	@Override
	public OtpResponseDto otpToEmail(String email) {
		return otpService.otpToEmail(email, 6);
	}

	@Override
	public UserResponseDTO deleteUserByEmail(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		if (user != null) {
			userRepo.delete(user);
			UserResponseDTO responseDTO = modelMapper.map(user, UserResponseDTO.class);
			responseDTO.setRoleName(user.getProfileType().getName());
			return responseDTO;
		}
		return null;
	}
	
	@Override
	public UserResponseDTO deleteUserById(Long id) {
		User user = userRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		if (user != null) {
			userRepo.delete(user);
			UserResponseDTO responseDTO = modelMapper.map(user, UserResponseDTO.class);
			responseDTO.setRoleName(user.getProfileType().getName());
			return responseDTO;
		}
		return null;
	}

//	@Override
//	public UserResponseDTO updateUser(User updatedUser) {
//
//		if (updatedUser != null) {
//			// Get user from db.
//			User user = this.getUserById(updatedUser.getUserId());
//			// Update user details.
//			user.setName(updatedUser.getName());
//			user.setEmail(updatedUser.getEmail());
//			user.setProfileImageUrl(updatedUser.getProfileImageUrl());
//			user.setUpdatedAt(LocalDateTime.now());
//			userRepo.save(user);
//			return user;
//		}
//		return null;
//	}

	@Override
	public UserResponseDTO getUserById(Long id) {
		User user = userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		UserResponseDTO responseDTO = modelMapper.map(user, UserResponseDTO.class);
		responseDTO.setRoleName(user.getProfileType().getName());
		return responseDTO;
	}

	@Override
	public UserResponseDTO getUserByEmail(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		UserResponseDTO responseDTO = modelMapper.map(user, UserResponseDTO.class);
		responseDTO.setRoleName(user.getProfileType().getName());
		return responseDTO;
	}

	@Override
	public boolean isUserExists(String email) {
		return userRepo.findByEmail(email).isPresent();
	}

	@Override
	public List<UserResponseDTO> getAllUsers() {
		List<UserResponseDTO> users = userRepo.findAll().stream().map((user) -> {
			UserResponseDTO responseDTO = modelMapper.map(user, UserResponseDTO.class);
			responseDTO.setRoleName(user.getProfileType().getName());
			return responseDTO;
		}).collect(Collectors.toList());

		return users;
	}

}
