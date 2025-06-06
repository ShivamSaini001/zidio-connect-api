package com.zidio.connect.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zidio.connect.dto.OtpResponseDto;
import com.zidio.connect.dto.RegistrationRequestDTO;
import com.zidio.connect.dto.UserDto;
import com.zidio.connect.entities.AdminProfile;
import com.zidio.connect.entities.RecruiterProfile;
import com.zidio.connect.entities.Role;
import com.zidio.connect.entities.StudentProfile;
import com.zidio.connect.entities.TeacherProfile;
import com.zidio.connect.entities.User;
import com.zidio.connect.enums.RoleTypeEnum;
import com.zidio.connect.repository.AdminProfileRepository;
import com.zidio.connect.repository.RecruiterProfileRepository;
import com.zidio.connect.repository.StudentProfileRepository;
import com.zidio.connect.repository.TeacherProfileRepository;
import com.zidio.connect.repository.UserRepository;
import com.zidio.connect.service.OTPService;
import com.zidio.connect.service.RoleService;
import com.zidio.connect.service.UserService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private OTPService otpService;

	@Autowired
	private AdminProfileRepository adminProfileRepo;

	@Autowired
	private RecruiterProfileRepository recruiterProfileRepo;

	@Autowired
	private TeacherProfileRepository teacherProfileRepo;

	@Autowired
	private StudentProfileRepository studentProfileRepos;

	@Override
	public UserDto createUser(RegistrationRequestDTO registrationRequestDto) {
		if (registrationRequestDto != null) {
			// If user already exists.
			userRepo.findAll().stream().forEach((user) -> {
				if (user.getEmail().equals(registrationRequestDto.getEmail())) {
					throw new EntityExistsException(
							"User with email '" + registrationRequestDto.getEmail() + "' already exists!!");
				}
			});

			// Verify OTP
			String otp = registrationRequestDto.getVerificationCode();

			boolean verifyOtp = otpService.verifyOtp(registrationRequestDto.getEmail(), otp);
			if (verifyOtp) {
				User newUser = modelMapper.map(registrationRequestDto, User.class);
				newUser.setCreatedAt(LocalDateTime.now());
				newUser.setUpdatedAt(LocalDateTime.now());

				// Assign profile type to user.
				Role role = roleService.getRoleByName("ROLE_" + registrationRequestDto.getSelectedRole().toUpperCase());
				newUser.setUserType(role);

				// Assign roles to user.
				List<Role> userRoles = this.getUserRoles(role);
				newUser.setRole(userRoles);

				// Save User into DB.
				User createdUser = userRepo.save(newUser);

				// Creating user profile.
				RoleTypeEnum roleType = RoleTypeEnum.valueOf(role.getName().replace("ROLE_", ""));
				String firstName = registrationRequestDto.getFirstName();
				String lastName = registrationRequestDto.getLastName();

				switch (roleType) {
				case ADMIN:
					AdminProfile adminProfile = new AdminProfile(firstName, lastName, createdUser);
					createdUser.setAdminProfile(adminProfileRepo.save(adminProfile));
					break;

				case RECRUITER:
					RecruiterProfile recruiterProfile = new RecruiterProfile(firstName, lastName, createdUser);
					createdUser.setRecruiterProfile(recruiterProfileRepo.save(recruiterProfile));
					break;
				case TEACHER:
					TeacherProfile teacherProfile = new TeacherProfile(firstName, lastName, createdUser);
					createdUser.setTeacherProfile(teacherProfileRepo.save(teacherProfile));
					break;
				case STUDENT:
					StudentProfile studentProfile = new StudentProfile(firstName, lastName, createdUser);
					createdUser.setStudentProfile(studentProfileRepos.save(studentProfile));
					break;
				}

				// Update User into DB.
				createdUser = userRepo.save(createdUser);

				UserDto userDto = modelMapper.map(createdUser, UserDto.class);

				// clear user
				otpService.clearUserDetails(createdUser.getEmail());
				return userDto;
			} else {
				throw new EntityNotFoundException("Please verify your email before registration!!");
			}
		}
		return null;
	}

	public List<Role> getUserRoles(Role role) {
		String roleName = role.getName();
		List<Role> allRoles = roleService.getAllRoles();
		List<Role> assignRoles = new ArrayList<>();

		if (roleName.equals("ROLE_" + RoleTypeEnum.ADMIN.toString())) {
			allRoles.stream().forEach((r) -> {
				if (r.getName().equals("ROLE_" + RoleTypeEnum.ADMIN.toString())
						|| r.getName().equals("ROLE_" + RoleTypeEnum.RECRUITER.toString())
						|| r.getName().equals("ROLE_" + RoleTypeEnum.TEACHER.toString())
						|| r.getName().equals("ROLE_" + RoleTypeEnum.STUDENT.toString())) {
					assignRoles.add(r);
				}
			});
		} else if (roleName.equals("ROLE_" + RoleTypeEnum.RECRUITER.toString())) {
			allRoles.stream().forEach((r) -> {
				if (r.getName().equals("ROLE_" + RoleTypeEnum.RECRUITER.toString())
						|| r.getName().equals("ROLE_" + RoleTypeEnum.TEACHER.toString())
						|| r.getName().equals("ROLE_" + RoleTypeEnum.STUDENT.toString())) {
					assignRoles.add(r);
				}
			});
		} else if (roleName.equals("ROLE_" + RoleTypeEnum.TEACHER.toString())) {
			allRoles.stream().forEach((r) -> {
				if (r.getName().equals("ROLE_" + RoleTypeEnum.TEACHER.toString())
						|| r.getName().equals("ROLE_" + RoleTypeEnum.STUDENT.toString())) {
					assignRoles.add(r);
				}
			});
		} else if (roleName.equals("ROLE_" + RoleTypeEnum.STUDENT.toString())) {
			allRoles.stream().forEach((r) -> {
				if (r.getName().equals("ROLE_" + RoleTypeEnum.STUDENT.toString())) {
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
	public UserDto deleteUserByEmail(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		userRepo.delete(user);
		UserDto responseDTO = modelMapper.map(user, UserDto.class);
		return responseDTO;
	}

	@Override
	public UserDto deleteUserById(Long id) {
		User user = userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		userRepo.delete(user);
		UserDto responseDTO = modelMapper.map(user, UserDto.class);
		return responseDTO;
	}

	@Override
	public UserDto getUserById(Long id) {
		User user = userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		UserDto responseDTO = modelMapper.map(user, UserDto.class);
		return responseDTO;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		UserDto responseDTO = modelMapper.map(user, UserDto.class);
		return responseDTO;
	}

	@Override
	public boolean isUserExists(String email) {
		return userRepo.findByEmail(email).isPresent();
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<UserDto> users = userRepo.findAll().stream().map((user) -> {
			UserDto responseDTO = modelMapper.map(user, UserDto.class);
			return responseDTO;
		}).collect(Collectors.toList());
		return users;
	}

}
