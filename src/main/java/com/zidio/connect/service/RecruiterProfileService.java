package com.zidio.connect.service;

import java.util.List;

import com.zidio.connect.dto.RecruiterProfileDto;

public interface RecruiterProfileService {

	RecruiterProfileDto createRecruiterProfile(RecruiterProfileDto profileDto, String email);

	RecruiterProfileDto updateRecruiterProfile(RecruiterProfileDto profileDto, String email);

	void deleteRecruiterProfileByEmail(String email);

	RecruiterProfileDto getRecruiterProfileByEmail(String email);
	
	RecruiterProfileDto getRecruiterProfileById(Long profileId);

	List<RecruiterProfileDto> getAllRecruiterProfiles();

}
