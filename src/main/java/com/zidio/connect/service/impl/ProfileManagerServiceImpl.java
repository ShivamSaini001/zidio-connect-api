package com.zidio.connect.service.impl;

import org.springframework.stereotype.Service;

import com.zidio.connect.dto.AdminProfileDto;
import com.zidio.connect.dto.ProfileDto;
import com.zidio.connect.dto.RecruiterProfileDto;
import com.zidio.connect.dto.StudentProfileDto;
import com.zidio.connect.dto.TeacherProfileDto;
import com.zidio.connect.dto.UserDto;
import com.zidio.connect.enums.AuthorityTypeEnum;
import com.zidio.connect.service.AdminProfileService;
import com.zidio.connect.service.ProfileManagerService;
import com.zidio.connect.service.RecruiterProfileService;
import com.zidio.connect.service.StudentProfileService;
import com.zidio.connect.service.TeacherProfileService;
import com.zidio.connect.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProfileManagerServiceImpl implements ProfileManagerService {

	private StudentProfileService studentProfileService;
	private TeacherProfileService teacherProfileService;
	private RecruiterProfileService recruiterProfileService;
	private AdminProfileService adminProfileService;
	private UserService userService;

	// Constructor
	public ProfileManagerServiceImpl(StudentProfileService studentProfileService,
			TeacherProfileService teacherProfileService, RecruiterProfileService recruiterProfileService,
			AdminProfileService adminProfileService, UserService userService) {
		super();
		this.studentProfileService = studentProfileService;
		this.teacherProfileService = teacherProfileService;
		this.recruiterProfileService = recruiterProfileService;
		this.adminProfileService = adminProfileService;
		this.userService = userService;
	}

	@Override
	public <T extends ProfileDto> T createUserProfile(String email, T profileDto) {
		UserDto userDto = userService.getUserByEmail(email);
		String userType = userDto.getUserType().getAuthority().replace("ROLE_", "").toUpperCase(); // Skip "ROLE_"
		AuthorityTypeEnum role = AuthorityTypeEnum.valueOf(userType);

		switch (role) {
		case ADMIN:
			if (profileDto instanceof AdminProfileDto adminDto) {
				return (T) adminProfileService.createAdminProfile(adminDto, email);
			}
			break;
		case RECRUITER:
			if (profileDto instanceof RecruiterProfileDto recruiterDto) {
				return (T) recruiterProfileService.createRecruiterProfile(recruiterDto, email);
			}
			break;
		case TEACHER:
			if (profileDto instanceof TeacherProfileDto teacherDto) {
				return (T) teacherProfileService.createTeacherProfile(teacherDto, email);
			}
			break;
		case STUDENT:
			if (profileDto instanceof StudentProfileDto studentDto) {
				return (T) studentProfileService.createStudentProfile(studentDto, email);
			}
			break;
		}
		throw new IllegalArgumentException("Profile DTO type does not match user role: " + role);
	}

	@Override
	public <T extends ProfileDto> T updateUserProfile(String email, T updatedProfileDto) {
		UserDto userDto = userService.getUserByEmail(email);
		String userType = userDto.getUserType().getAuthority().replace("ROLE_", "").toUpperCase(); // Skip "ROLE_"
		AuthorityTypeEnum role = AuthorityTypeEnum.valueOf(userType);

		switch (role) {
		case ADMIN:
			if (updatedProfileDto instanceof AdminProfileDto adminDto) {
				return (T) adminProfileService.updateAdminProfile(adminDto, email);
			}
			break;
		case RECRUITER:
			if (updatedProfileDto instanceof RecruiterProfileDto recruiterDto) {
				return (T) recruiterProfileService.updateRecruiterProfile(recruiterDto, email);
			}
			break;
		case TEACHER:
			if (updatedProfileDto instanceof TeacherProfileDto teacherDto) {
				return (T) teacherProfileService.updateTeacherProfile(teacherDto, email);
			}
			break;
		case STUDENT:
			if (updatedProfileDto instanceof StudentProfileDto studentDto) {
				return (T) studentProfileService.updateStudentProfile(studentDto, email);
			}
			break;
		}
		throw new IllegalArgumentException("Profile DTO type does not match user role: " + role);
	}

	@Override
	public <T extends ProfileDto> T getUserProfile(UserDto userDto, Class<T> profileType) {
		String userType = userDto.getUserType().getAuthority().replace("ROLE_", "").toUpperCase(); // Skip "ROLE_"
		AuthorityTypeEnum role = AuthorityTypeEnum.valueOf(userType);

		switch (role) {
		case ADMIN:
			AdminProfileDto adminProfileDto = adminProfileService.getAdminProfileByEmail(userDto.getEmail());
			if (adminProfileDto.getClass().equals(profileType)) {
				return (T) adminProfileDto;
			}
			break;
		case RECRUITER:
			RecruiterProfileDto recruiterProfileDto = recruiterProfileService
					.getRecruiterProfileByEmail(userDto.getEmail());
			if (recruiterProfileDto.getClass().equals(profileType)) {
				return (T) recruiterProfileDto;
			}
			break;
		case TEACHER:
			TeacherProfileDto teacherProfileDto = teacherProfileService.getTeacherProfileByEmail(userDto.getEmail());
			if (teacherProfileDto.getClass().equals(profileType)) {
				return (T) teacherProfileDto;
			}
			break;
		case STUDENT:
			StudentProfileDto studentProfileDto = studentProfileService.getStudentProfileByEmail(userDto.getEmail());
			if (studentProfileDto.getClass().equals(profileType)) {
				return (T) studentProfileDto;
			}
			break;
		default:
			throw new EntityNotFoundException("Profile does not exists.");
		}
		throw new IllegalArgumentException("Profile DTO type does not match user role: " + role);
	}

	@Override
	public void deleteUserProfile(String email) {
		UserDto userDto = userService.getUserByEmail(email);
		String userType = userDto.getUserType().getAuthority().replace("ROLE_", "").toUpperCase(); // Skip "ROLE_"
		AuthorityTypeEnum role = AuthorityTypeEnum.valueOf(userType);

		switch (role) {
		case ADMIN:
			adminProfileService.deleteAdminProfileByEmail(email);
			break;
		case RECRUITER:
			recruiterProfileService.deleteRecruiterProfileByEmail(email);
			break;
		case TEACHER:
			teacherProfileService.deleteTeacherProfileByEmail(email);
			break;
		case STUDENT:
			studentProfileService.deleteStudentProfileByEmail(email);
			break;
		default:
			throw new EntityNotFoundException("Profile does not exists.");
		}
	}

}
