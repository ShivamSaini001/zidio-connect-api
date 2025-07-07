package com.zidio.connect.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.zidio.connect.dto.AdminProfileDto;
import com.zidio.connect.dto.UserDto;
import com.zidio.connect.entities.AdminProfile;
import com.zidio.connect.entities.User;
import com.zidio.connect.repository.AdminProfileRepository;
import com.zidio.connect.repository.UserRepository;
import com.zidio.connect.service.AdminProfileService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AdminProfileServiceImpl implements AdminProfileService {

	AdminProfileRepository adminProfileRepo;
	UserRepository userRepo;
	ModelMapper modelMapper;

	// Constructor
	public AdminProfileServiceImpl(AdminProfileRepository adminProfileRepo, UserRepository userRepo,
			ModelMapper modelMapper) {
		this.adminProfileRepo = adminProfileRepo;
		this.userRepo = userRepo;
		this.modelMapper = modelMapper;
	}

	@Transactional
	@Override
	public AdminProfileDto createAdminProfile(AdminProfileDto profileDto, String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));

		if (profileDto == null) {
			throw new RuntimeException("Please provide profile details!!");
		}

		if (user.getAdminProfile() == null) {
			// Creating admin profile object.
			AdminProfile adminProfile = modelMapper.map(profileDto, AdminProfile.class);
			// Create admin profile and save it into database.
			adminProfile.setUser(user);
			AdminProfile savedAdminProfile = adminProfileRepo.save(adminProfile);

			// updating User entities.
			user.setAdminProfile(savedAdminProfile);
			User savedUser = userRepo.save(user);

			AdminProfileDto savedAdminProfileDto = modelMapper.map(savedAdminProfile, AdminProfileDto.class);
			savedAdminProfileDto.setUserDto(modelMapper.map(savedUser, UserDto.class));
			return savedAdminProfileDto;
		} else {
			throw new EntityExistsException("Profile already exists!!");
		}
	}

	@Override
	public AdminProfileDto updateAdminProfile(AdminProfileDto profileDto, String email) {
		// fetching admin profile from database.
		AdminProfile adminProfile = this.getAdminProfile(email);

		// If profile does not exists.
		if (adminProfile == null) {
			throw new EntityNotFoundException("Admin profile does not exists.");
		}

		// updating Recruiter Profile.
		adminProfile.setFirstName(profileDto.getFirstName());
		adminProfile.setLastName(profileDto.getLastName());
		adminProfile.setBio(profileDto.getBio());
		adminProfile.setDateOfBirth(profileDto.getDateOfBirth());
		adminProfile.setGender(profileDto.getGender());
		adminProfile.setMobile(profileDto.getMobile());

		// update into database.
		AdminProfile savedAdminProfile = adminProfileRepo.save(adminProfile);
		AdminProfileDto adminProfileDto = modelMapper.map(savedAdminProfile, AdminProfileDto.class);
		adminProfileDto.setUserDto(modelMapper.map(savedAdminProfile.getUser(), UserDto.class));
		return adminProfileDto;
	}

	@Transactional
	@Override
	public void deleteAdminProfileByEmail(String email) {
		// fetching recruiter profile form db.
		AdminProfile adminProfile = this.getAdminProfile(email);

		// If admin profile does not exists.
		if (adminProfile == null) {
			throw new EntityNotFoundException("Admin profile does not exists.");
		}
		// extracting user entity.
		User user = adminProfile.getUser();

		// updating user entity.
		user.setAdminProfile(null);
		userRepo.save(user);

		// updating admin profile.
		adminProfile.setUser(null);
		adminProfileRepo.save(adminProfile);

		// Deleting admin profile.
		adminProfileRepo.delete(adminProfile);
	}

	@Override
	public AdminProfileDto getAdminProfileByEmail(String email) {
		// Get user from db
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		// Get admin profile
		AdminProfile adminProfile = user.getAdminProfile();
		
		if (adminProfile == null) {
			throw new EntityNotFoundException("Profile does not exists!!");
		}
		
		// Convert admin profile.
		AdminProfileDto adminProfileDto = modelMapper.map(adminProfile, AdminProfileDto.class);
		adminProfileDto.setUserDto(modelMapper.map(user, UserDto.class));
		return adminProfileDto;
	}

	@Override
	public AdminProfileDto getAdminProfileById(Long profileId) {
		// get admin profile from db
		AdminProfile admiinProfile = adminProfileRepo.findById(profileId)
				.orElseThrow(() -> new EntityNotFoundException("Profile does not exists..."));

		// Get user
		User user = admiinProfile.getUser();
		// Convert Entities into DTO.
		AdminProfileDto adminProfileDto = modelMapper.map(admiinProfile, AdminProfileDto.class);
		adminProfileDto.setUserDto(modelMapper.map(user, UserDto.class));
		return adminProfileDto;
	}

	@Override
	public List<AdminProfileDto> getAllAdminProfiles() {
		return adminProfileRepo.findAll().stream().map((adminProfile) -> {
			AdminProfileDto adminProfileDto = modelMapper.map(adminProfile, AdminProfileDto.class);
			adminProfileDto.setUserDto(modelMapper.map(adminProfile.getUser(), UserDto.class));
			return adminProfileDto;
		}).toList();
	}

	private AdminProfile getAdminProfile(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		return user.getAdminProfile();
	}

}
