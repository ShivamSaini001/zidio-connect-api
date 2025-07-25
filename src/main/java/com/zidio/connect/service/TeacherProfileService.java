package com.zidio.connect.service;

import java.util.List;
import java.util.Map;

import com.zidio.connect.dto.TeacherProfileDto;
import com.zidio.connect.entities.CloudinaryFile;

public interface TeacherProfileService {

	TeacherProfileDto createTeacherProfile(TeacherProfileDto profileDto, String email);

	TeacherProfileDto updateTeacherProfile(TeacherProfileDto profileDto, String email);

	void deleteTeacherProfileByEmail(String email);

	TeacherProfileDto getTeacherProfileByEmail(String email);
	
	TeacherProfileDto getTeacherProfileById(Long profileId);

	List<TeacherProfileDto> getAllTeacherProfiles();

	CloudinaryFile updateProfileImageDetails(String email, CloudinaryFile profileDetails);

	Map<String, Object> getProfileImage(String email);
	
	void uploadCertificate(String email, CloudinaryFile certificateDetails);

	List<CloudinaryFile> getAllCertificates(String email);

	CloudinaryFile deleteCertificateById(String email, Long certificateId);

	
}
