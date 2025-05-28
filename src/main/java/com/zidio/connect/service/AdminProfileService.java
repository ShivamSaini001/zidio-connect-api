package com.zidio.connect.service;

import java.util.List;

import com.zidio.connect.dto.AdminProfileDto;

public interface AdminProfileService {

	AdminProfileDto createAdminProfile(AdminProfileDto profileDto, String email);

	AdminProfileDto updateAdminProfile(AdminProfileDto profileDto, String email);

	void deleteAdminProfileByEmail(String email);

	AdminProfileDto getAdminProfileByEmail(String email);

	AdminProfileDto getAdminProfileById(Long profileId);

	List<AdminProfileDto> getAllAdminProfiles();

}
