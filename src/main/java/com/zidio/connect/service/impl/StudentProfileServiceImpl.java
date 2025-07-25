package com.zidio.connect.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.zidio.connect.dto.EducationDto;
import com.zidio.connect.dto.SkillDto;
import com.zidio.connect.entities.CloudinaryFile;
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

	@Transactional
	@Override
	public StudentProfileDto updateStudentProfile(StudentProfileDto profileDto, String email) {
		// fetching student profile from database.
		StudentProfile studentProfile = this.getStudentProfile(email);
		
		if(studentProfile == null) {
			throw new EntityNotFoundException("Student profile does not exists.");
		}
		
		// updating StudentProfile.
		StudentProfile updatedProfile = modelMapper.map(profileDto, StudentProfile.class);
		updatedProfile.setUserId(studentProfile.getUserId());
		updatedProfile.setUser(studentProfile.getUser());
		updatedProfile.setProfileImage(studentProfile.getProfileImage());
		updatedProfile.setResume(studentProfile.getResume());

		// Save profile
		studentProfileRepo.save(updatedProfile);

		// Update user Entity.
		User user = studentProfile.getUser();
		user.setUpdatedAt(LocalDateTime.now());
		if(profileDto.getAddress() != null)
			user.setAddress(profileDto.getAddress());

		// update into database.
		userRepo.save(user);
		return this.getStudentProfileByEmail(email);
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

		CloudinaryFile profileImage = studentProfile.getProfileImage();
		CloudinaryFile resume = studentProfile.getResume();
		if(profileImage != null){
			studentProfileDto.setProfileImageUrl(profileImage.getSecureUrl());
		}
		if(resume != null){
			studentProfileDto.setResumeUrl(resume.getSecureUrl());
		}

		studentProfileDto.setEducations(user.getEducations().stream().map((edu) -> modelMapper.map(edu, EducationDto.class)).toList());
		studentProfileDto.setSkills(user.getSkills().stream().map((skill) -> modelMapper.map(skill, SkillDto.class)).collect(Collectors.toSet()));
		studentProfileDto.setAddress(user.getAddress());
		return studentProfileDto;
	}

	@Override
	public StudentProfileDto getStudentProfileById(Long profileId) {
		StudentProfile studentProfile = studentProfileRepo.findById(profileId)
				.orElseThrow(() -> new EntityNotFoundException("Profile does not exists..."));
		User user = studentProfile.getUser();
		// Convert Entities into DTO.
		StudentProfileDto studentProfileDto = modelMapper.map(studentProfile, StudentProfileDto.class);
		studentProfileDto.setUserDto(modelMapper.map(user, UserDto.class));
		studentProfileDto.setEducations(user.getEducations().stream().map((edu) -> modelMapper.map(edu, EducationDto.class)).toList());
		studentProfileDto.setSkills(user.getSkills().stream().map((skill) -> modelMapper.map(skill, SkillDto.class)).collect(Collectors.toSet()));
		studentProfileDto.setAddress(user.getAddress());
		return studentProfileDto;
	}

	@Override
	public List<StudentProfileDto> getAllStudentProfiles() {
		return studentProfileRepo.findAll().stream().map((studentProfile) -> {
			User user = studentProfile.getUser();
			StudentProfileDto studentProfileDto = modelMapper.map(studentProfile, StudentProfileDto.class);
			studentProfileDto.setUserDto(modelMapper.map(user, UserDto.class));
			studentProfileDto.setEducations(user.getEducations().stream().map((edu) -> modelMapper.map(edu, EducationDto.class)).toList());
			studentProfileDto.setSkills(user.getSkills().stream().map((skill) -> modelMapper.map(skill, SkillDto.class)).collect(Collectors.toSet()));
			studentProfileDto.setAddress(user.getAddress());
			return studentProfileDto;
		}).toList();
	}

	private StudentProfile getStudentProfile(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		return user.getStudentProfile();
	}

}
