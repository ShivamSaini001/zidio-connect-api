package com.zidio.connect.service;

import java.util.List;

import com.zidio.connect.dto.TeacherProfileDto;

public interface TeacherProfileService {

	TeacherProfileDto createTeacherProfile(TeacherProfileDto profileDto, String email);

	TeacherProfileDto updateTeacherProfile(TeacherProfileDto profileDto, String email);

	void deleteTeacherProfileByEmail(String email);

	TeacherProfileDto getTeacherProfileByEmail(String email);
	
	TeacherProfileDto getTeacherProfileById(Long profileId);

	List<TeacherProfileDto> getAllTeacherProfiles();

}
