package com.zidio.connect.service;

import com.zidio.connect.dto.AdminProfileDto;
import com.zidio.connect.dto.RecruiterProfileDto;
import com.zidio.connect.dto.StudentProfileDto;
import com.zidio.connect.dto.TeacherProfileDto;
import com.zidio.connect.entities.AdminProfile;
import com.zidio.connect.entities.RecruiterProfile;
import com.zidio.connect.entities.StudentProfile;
import com.zidio.connect.entities.TeacherProfile;
import com.zidio.connect.entities.User;

public interface ProfileService {

	AdminProfileDto createProfile(AdminProfile adminProfile, User user);
	RecruiterProfileDto createProfile(RecruiterProfile recruiterProfile, User user);
	StudentProfileDto createProfile(StudentProfile studentprofile, User user);
	TeacherProfileDto createProfile(TeacherProfile teacherProfile, User user);
	
	AdminProfileDto getAdminProfile();
	RecruiterProfileDto getRecruiterProfile();
	StudentProfileDto getStudentProfile();
	TeacherProfileDto getTeacherProfile();
	
	
}
