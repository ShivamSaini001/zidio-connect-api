package com.zidio.connect.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.zidio.connect.dto.StudentProfileDto;
import com.zidio.connect.dto.UserDto;
import com.zidio.connect.entities.StudentProfile;
import com.zidio.connect.entities.User;
import com.zidio.connect.repository.StudentProfileRepository;
import com.zidio.connect.repository.UserRepository;
import com.zidio.connect.service.StudentProfileService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {

	StudentProfileRepository studentProfileRepo;
	UserRepository userRepo;
	ModelMapper modelMapper;

	// Constructor
	public StudentProfileServiceImpl(StudentProfileRepository studentProfileRepo, UserRepository userRepo,
			ModelMapper modelMapper) {
		super();
		this.studentProfileRepo = studentProfileRepo;
		this.userRepo = userRepo;
		this.modelMapper = modelMapper;
	}

	@Transactional
	@Override
	public StudentProfileDto createStudentProfile(StudentProfileDto profileDto, String email) {

		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));

		if (profileDto == null) {
			throw new RuntimeException("Please provide profile details!!");
		}

		if (user.getStudentProfile() == null) {
			StudentProfile studentProfile = modelMapper.map(profileDto, StudentProfile.class);
			// Creating Teacher profile
			studentProfile.setUser(user);
			StudentProfile savedProfile = studentProfileRepo.save(studentProfile);

			// updating User entity into database.
			user.setStudentProfile(savedProfile);
			User savedUser = userRepo.save(user);

			StudentProfileDto savedStudentProfileDto = modelMapper.map(savedProfile, StudentProfileDto.class);
			savedStudentProfileDto.setUserDto(modelMapper.map(savedUser, UserDto.class));
			return savedStudentProfileDto;
		} else {
			throw new EntityExistsException("Profile already exists!!");
		}
	}

	@Override
	public StudentProfileDto updateStudentProfile(StudentProfileDto profileDto, String email) {
		// fetching student profile from database.
		StudentProfile studentProfile = this.getStudentProfile(email);
		
		if(studentProfile == null) {
			throw new EntityNotFoundException("Student profile does not exists.");
		}
		
		// updating StudentProfile.
		studentProfile.setFirstName(profileDto.getFirstName());
		studentProfile.setLastName(profileDto.getLastName());
		studentProfile.setBio(profileDto.getBio());
		studentProfile.setDateOfBirth(profileDto.getDateOfBirth());
		studentProfile.setGender(profileDto.getGender());
		studentProfile.setMobile(profileDto.getMobile());
		studentProfile.setLinkedInUrl(profileDto.getLinkedInUrl());
		studentProfile.setResumeUrl(profileDto.getResumeUrl());
		// update into database.
		StudentProfile savedStudentProfile = studentProfileRepo.save(studentProfile);

		StudentProfileDto studentProfileDto = modelMapper.map(savedStudentProfile, StudentProfileDto.class);
		studentProfileDto.setUserDto(modelMapper.map(savedStudentProfile.getUser(), UserDto.class));
		return studentProfileDto;
	}

	@Transactional
	@Override
	public void deleteStudentProfileByEmail(String email) {
		// fetching student profile form db.
		StudentProfile studentProfile = this.getStudentProfile(email);
		
		// If student profile does not exists.
		if (studentProfile == null) {
			throw new EntityNotFoundException("Student profile does not exists.");
		}
		
		// extracting user entity.
		User user = studentProfile.getUser();
		// updating user entity.
		user.setStudentProfile(null);
		userRepo.save(user);

		// updating student profile.
		studentProfile.setUser(null);
		studentProfileRepo.save(studentProfile);

		// Deleting Student profile.
		studentProfileRepo.delete(studentProfile);
	}

	@Override
	public StudentProfileDto getStudentProfileByEmail(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("Student does not exists!!"));
		StudentProfile studentProfile = user.getStudentProfile();

		if(studentProfile == null) {
			throw new EntityNotFoundException("Profile does not exists!!");
		}
		
		StudentProfileDto studentProfileDto = modelMapper.map(studentProfile, StudentProfileDto.class);
		studentProfileDto.setUserDto(modelMapper.map(user, UserDto.class));
		return studentProfileDto;
	}

	@Override
	public StudentProfileDto getStudentProfileById(Long profileId) {
		StudentProfile studentProfile = studentProfileRepo.findById(profileId)
				.orElseThrow(() -> new EntityNotFoundException("Profile does not exists..."));
		User user = studentProfile.getUser();
		// Convert Entities into DTO.
		StudentProfileDto studentProfileDto = modelMapper.map(studentProfile, StudentProfileDto.class);
		UserDto userDto = modelMapper.map(user, UserDto.class);
		studentProfileDto.setUserDto(userDto);
		return studentProfileDto;
	}

	@Override
	public List<StudentProfileDto> getAllStudentProfiles() {
		return studentProfileRepo.findAll().stream().map((studentProfile) -> {
			StudentProfileDto studentProfileDto = modelMapper.map(studentProfile, StudentProfileDto.class);
			UserDto userDto = modelMapper.map(studentProfile.getUser(), UserDto.class);
			studentProfileDto.setUserDto(userDto);
			return studentProfileDto;
		}).toList();
	}

	private StudentProfile getStudentProfile(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		return user.getStudentProfile();
	}

}
