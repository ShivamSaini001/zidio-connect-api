package com.zidio.connect.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zidio.connect.dto.RecruiterProfileDto;
import com.zidio.connect.dto.UserDto;
import com.zidio.connect.entities.RecruiterProfile;
import com.zidio.connect.entities.User;
import com.zidio.connect.repository.RecruiterProfileRepository;
import com.zidio.connect.repository.UserRepository;
import com.zidio.connect.service.RecruiterProfileService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class RecruiterProfileServiceImpl implements RecruiterProfileService {

	@Autowired
	RecruiterProfileRepository recruiterProfileRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	ModelMapper modelMapper;

	@Transactional
	@Override
	public RecruiterProfileDto createRecruiterProfile(RecruiterProfileDto profileDto, String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));

		if (profileDto == null) {
			throw new RuntimeException("Please provide profile details!!");
		}

		if (user.getRecruiterProfile() == null) {
			RecruiterProfile recruiterProfile = modelMapper.map(profileDto, RecruiterProfile.class);
			// Create recruiter profile and save it into database.
			recruiterProfile.setUser(user);
			RecruiterProfile savedRecruiterProfile = recruiterProfileRepo.save(recruiterProfile);

			// updating User entities.
			user.setRecruiterProfile(savedRecruiterProfile);
			User savedUser = userRepo.save(user);

			RecruiterProfileDto savedRecruiterProfileDto = modelMapper.map(savedRecruiterProfile,
					RecruiterProfileDto.class);
			savedRecruiterProfileDto.setUserDto(modelMapper.map(savedUser, UserDto.class));
			return savedRecruiterProfileDto;
		} else {
			throw new EntityExistsException("Profile already exists!!");
		}
	}

	@Override
	public RecruiterProfileDto updateRecruiterProfile(RecruiterProfileDto profileDto, String email) {
		// fetching teacher profile from database.
		RecruiterProfile recruiterProfile = this.getRecruiterProfile(email);

		// If profile does not exists.
		if (recruiterProfile == null) {
			throw new EntityNotFoundException("Recruiter profile does not exists.");
		}

		// updating Recruiter Profile.
		recruiterProfile.setFirstName(profileDto.getFirstName());
		recruiterProfile.setLastName(profileDto.getLastName());
		recruiterProfile.setBio(profileDto.getBio());
		recruiterProfile.setDateOfBirth(profileDto.getDateOfBirth());
		recruiterProfile.setGender(profileDto.getGender());
		recruiterProfile.setMobile(profileDto.getMobile());
		recruiterProfile.setTotalPostedJobs(profileDto.getTotalPostedJobs());
		recruiterProfile.setTotalPostedInternships(profileDto.getTotalPostedInternships());
		recruiterProfile.setDesignation(profileDto.getDesignation());

		// update into database.
		RecruiterProfile savedRecruiterProfile = recruiterProfileRepo.save(recruiterProfile);
		RecruiterProfileDto recruiterProfileDto = modelMapper.map(savedRecruiterProfile, RecruiterProfileDto.class);
		recruiterProfileDto.setUserDto(modelMapper.map(savedRecruiterProfile.getUser(), UserDto.class));
		return recruiterProfileDto;
	}

	@Transactional
	@Override
	public void deleteRecruiterProfileByEmail(String email) {
		// fetching recruiter profile form db.
		RecruiterProfile recruiterProfile = this.getRecruiterProfile(email);

		// If recruiter profile does not exists.
		if (recruiterProfile == null) {
			throw new EntityNotFoundException("Recruiter profile does not exists.");
		}
		// extracting user entity.
		User user = recruiterProfile.getUser();
		
		// updating user entity.
		user.setRecruiterProfile(null);
		userRepo.save(user);

		// updating recruiter profile.
		recruiterProfile.setUser(null);
		recruiterProfileRepo.save(recruiterProfile);

		// Deleting Student profile.
		recruiterProfileRepo.delete(recruiterProfile);
	}

	@Override
	public RecruiterProfileDto getRecruiterProfileByEmail(String email) {
		// Get user from db
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		// Get recruiter profile
		RecruiterProfile recruiterProfile = user.getRecruiterProfile();
		// Convert recruiter profile.
		RecruiterProfileDto recruiterProfileDto = modelMapper.map(recruiterProfile, RecruiterProfileDto.class);
		recruiterProfileDto.setUserDto(modelMapper.map(user, UserDto.class));
		return recruiterProfileDto;
	}

	@Override
	public RecruiterProfileDto getRecruiterProfileById(Long profileId) {
		// get recruiter profile from db
		RecruiterProfile recruiterProfile = recruiterProfileRepo.findById(profileId)
				.orElseThrow(() -> new EntityNotFoundException("Profile does not exists..."));

		// Get user
		User user = recruiterProfile.getUser();
		// Convert Entities into DTO.
		RecruiterProfileDto recruiterProfileDto = modelMapper.map(recruiterProfile, RecruiterProfileDto.class);
		recruiterProfileDto.setUserDto(modelMapper.map(user, UserDto.class));
		return recruiterProfileDto;
	}

	@Override
	public List<RecruiterProfileDto> getAllRecruiterProfiles() {
		return recruiterProfileRepo.findAll().stream().map((recruiterProfile) -> {
			RecruiterProfileDto recruiterProfileDto = modelMapper.map(recruiterProfile, RecruiterProfileDto.class);
			recruiterProfileDto.setUserDto(modelMapper.map(recruiterProfile.getUser(), UserDto.class));
			return recruiterProfileDto;
		}).toList();
	}

	private RecruiterProfile getRecruiterProfile(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		return user.getRecruiterProfile();
	}

}
