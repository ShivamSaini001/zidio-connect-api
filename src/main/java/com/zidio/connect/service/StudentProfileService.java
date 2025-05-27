package com.zidio.connect.service;

import java.util.List;

import com.zidio.connect.dto.StudentProfileDto;

public interface StudentProfileService {

	StudentProfileDto createStudentProfile(StudentProfileDto profileDto, String email);

	StudentProfileDto updateStudentProfile(StudentProfileDto profileDto, String email);

	void deleteStudentProfileByEmail(String email);

	StudentProfileDto getStudentProfileByEmail(String email);
	
	StudentProfileDto getStudentProfileById(Long profileId);

	List<StudentProfileDto> getAllStudentProfiles();

}
