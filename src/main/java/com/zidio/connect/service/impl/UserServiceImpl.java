package com.zidio.connect.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.zidio.connect.entities.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.zidio.connect.config.security.jwt.JwtHelper;
import com.zidio.connect.config.security.jwt.dto.JwtLoginRequest;
import com.zidio.connect.config.security.jwt.dto.JwtLoginResponse;
import com.zidio.connect.dto.AdminProfileDto;
import com.zidio.connect.dto.OtpResponseDto;
import com.zidio.connect.dto.RecruiterProfileDto;
import com.zidio.connect.dto.RegistrationRequestDTO;
import com.zidio.connect.dto.StudentProfileDto;
import com.zidio.connect.dto.TeacherProfileDto;
import com.zidio.connect.dto.UserDto;
import com.zidio.connect.enums.AuthorityTypeEnum;
import com.zidio.connect.exception.CustomException;
import com.zidio.connect.repository.AdminProfileRepository;
import com.zidio.connect.repository.RecruiterProfileRepository;
import com.zidio.connect.repository.StudentProfileRepository;
import com.zidio.connect.repository.TeacherProfileRepository;
import com.zidio.connect.repository.UserRepository;
import com.zidio.connect.service.OTPService;
import com.zidio.connect.service.ProfileManagerService;
import com.zidio.connect.service.UserAuthorityService;
import com.zidio.connect.service.UserService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepo;
	private UserAuthorityService userAuthorityService;
	private ModelMapper modelMapper;
	private OTPService otpService;
	private AdminProfileRepository adminProfileRepo;
	private RecruiterProfileRepository recruiterProfileRepo;
	private TeacherProfileRepository teacherProfileRepo;
	private StudentProfileRepository studentProfileRepos;
	private ProfileManagerService profileManagerService;
	private BCryptPasswordEncoder passwordEncoder;
	private JwtHelper jwtHelper;
	@Autowired
	@Lazy
	private AuthenticationManager authenticationManager;

	// Constructor
	@Lazy
	public UserServiceImpl(UserRepository userRepo, UserAuthorityService userAuthorityService, ModelMapper modelMapper,
			OTPService otpService, AdminProfileRepository adminProfileRepo,
			RecruiterProfileRepository recruiterProfileRepo, TeacherProfileRepository teacherProfileRepo,
			StudentProfileRepository studentProfileRepos, ProfileManagerService profileManagerService,
			BCryptPasswordEncoder passwordEncoder, JwtHelper jwtHelper) {

		this.userRepo = userRepo;
		this.userAuthorityService = userAuthorityService;
		this.modelMapper = modelMapper;
		this.otpService = otpService;
		this.adminProfileRepo = adminProfileRepo;
		this.recruiterProfileRepo = recruiterProfileRepo;
		this.teacherProfileRepo = teacherProfileRepo;
		this.studentProfileRepos = studentProfileRepos;
		this.profileManagerService = profileManagerService;
		this.passwordEncoder = passwordEncoder;
		this.jwtHelper = jwtHelper;
	}

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
				newUser.setAddress(new Address());

				// Assign profile type to user.
				UserAuthority userAuthority = userAuthorityService
						.getAuthorityByName("ROLE_" + registrationRequestDto.getSelectedRole().toUpperCase());
				newUser.setUserType(userAuthority);

				// Assign roles to user.
				List<UserAuthority> userAuthorities = this.getUserAuthorities(userAuthority);
				newUser.setAuthorities(userAuthorities);

				// Encrypt user password.
				String encodedPassword = passwordEncoder.encode(newUser.getPassword());
				newUser.setPassword(encodedPassword);

				// Save User into DB.
				User createdUser = userRepo.save(newUser);

				// Creating user profile.
				AuthorityTypeEnum roleType = AuthorityTypeEnum
						.valueOf(userAuthority.getAuthority().replace("ROLE_", ""));
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

	public List<UserAuthority> getUserAuthorities(UserAuthority userAuthority) {
		String authorityName = userAuthority.getAuthority();
		List<UserAuthority> allAuthorities = userAuthorityService.getAllAuthorities();
		List<UserAuthority> assignAuthorities = new ArrayList<>();

		if (authorityName.equals("ROLE_" + AuthorityTypeEnum.ADMIN.toString())) {
			allAuthorities.stream().forEach((r) -> {
				if (r.getAuthority().equals("ROLE_" + AuthorityTypeEnum.ADMIN.toString())
						|| r.getAuthority().equals("ROLE_" + AuthorityTypeEnum.RECRUITER.toString())
						|| r.getAuthority().equals("ROLE_" + AuthorityTypeEnum.TEACHER.toString())
						|| r.getAuthority().equals("ROLE_" + AuthorityTypeEnum.STUDENT.toString())) {
					assignAuthorities.add(r);
				}
			});
		} else if (authorityName.equals("ROLE_" + AuthorityTypeEnum.RECRUITER.toString())) {
			allAuthorities.stream().forEach((r) -> {
				if (r.getAuthority().equals("ROLE_" + AuthorityTypeEnum.RECRUITER.toString())
						|| r.getAuthority().equals("ROLE_" + AuthorityTypeEnum.TEACHER.toString())
						|| r.getAuthority().equals("ROLE_" + AuthorityTypeEnum.STUDENT.toString())) {
					assignAuthorities.add(r);
				}
			});
		} else if (authorityName.equals("ROLE_" + AuthorityTypeEnum.TEACHER.toString())) {
			allAuthorities.stream().forEach((r) -> {
				if (r.getAuthority().equals("ROLE_" + AuthorityTypeEnum.TEACHER.toString())
						|| r.getAuthority().equals("ROLE_" + AuthorityTypeEnum.STUDENT.toString())) {
					assignAuthorities.add(r);
				}
			});
		} else if (authorityName.equals("ROLE_" + AuthorityTypeEnum.STUDENT.toString())) {
			allAuthorities.stream().forEach((r) -> {
				if (r.getAuthority().equals("ROLE_" + AuthorityTypeEnum.STUDENT.toString())) {
					assignAuthorities.add(r);
				}
			});
		}
		return assignAuthorities;
	}

	@Override
	public OtpResponseDto otpToEmail(String email) {
		return otpService.otpToEmail(email, 6);
	}

	public JwtLoginResponse loginUser(JwtLoginRequest loginRequest) {
		return this.doAuthenticate(loginRequest);
	}

	private JwtLoginResponse doAuthenticate(JwtLoginRequest loginRequest) {
		// Get user from DB or InMemory
		Authentication authentication;

		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		} catch (BadCredentialsException e) {
			throw new CustomException("Invalid username or password", HttpStatus.NOT_FOUND);
		} catch (AuthenticationException exception) {
			throw new CustomException("Bad credentials", HttpStatus.BAD_REQUEST);
		}

		// If user exists than Generate token
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String jwtToken = jwtHelper.generateTokenFromUsername(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		String firstName = "";
		String lastName = "";
		String userEmail = userDetails.getUsername();
		UserDto userDto = this.getUserByEmail(userEmail);
		String profileType = userDto.getUserType().getAuthority();
		profileType = profileType.replace("ROLE_", "").toUpperCase();

		if (profileType.equals(AuthorityTypeEnum.ADMIN.toString())) {
			AdminProfileDto userProfile = profileManagerService.getUserProfile(userDto, AdminProfileDto.class);
			firstName = userProfile.getFirstName();
			lastName = userProfile.getLastName();
		} else if (profileType.equals(AuthorityTypeEnum.RECRUITER.toString())) {
			RecruiterProfileDto userProfile = profileManagerService.getUserProfile(userDto, RecruiterProfileDto.class);
			firstName = userProfile.getFirstName();
			lastName = userProfile.getLastName();
		} else if (profileType.equals(AuthorityTypeEnum.TEACHER.toString())) {
			TeacherProfileDto userProfile = profileManagerService.getUserProfile(userDto, TeacherProfileDto.class);
			firstName = userProfile.getFirstName();
			lastName = userProfile.getLastName();
		} else if (profileType.equals(AuthorityTypeEnum.STUDENT.toString())) {
			StudentProfileDto userProfile = profileManagerService.getUserProfile(userDto, StudentProfileDto.class);
			firstName = userProfile.getFirstName();
			lastName = userProfile.getLastName();
		}

		JwtLoginResponse response = new JwtLoginResponse();
		response.setFirstName(firstName);
		response.setLastName(lastName);
		response.setUsername(userDetails.getUsername());
		response.setJwtToken(jwtToken);
		response.setRoles(roles);
		response.setSuccess(true);
		return response;
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
	public Long getUserIdByEmail(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		return user.getUserId();
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
