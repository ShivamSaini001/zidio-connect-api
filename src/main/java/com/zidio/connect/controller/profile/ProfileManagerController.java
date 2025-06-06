package com.zidio.connect.controller.profile;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zidio.connect.dto.AdminProfileDto;
import com.zidio.connect.dto.ProfileDto;
import com.zidio.connect.dto.RecruiterProfileDto;
import com.zidio.connect.dto.StudentProfileDto;
import com.zidio.connect.dto.TeacherProfileDto;
import com.zidio.connect.dto.UserDto;
import com.zidio.connect.enums.RoleTypeEnum;
import com.zidio.connect.service.ProfileManagerService;
import com.zidio.connect.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/v1/user-profile")
public class ProfileManagerController {

	@Autowired
	private ProfileManagerService profileManagerService;

	@Autowired
	private UserService userService;

	@PostMapping("/create/admin/{email}")
	public ResponseEntity<?> createAdminProfile(@PathVariable String email, AdminProfileDto adminProfileDto) {
		AdminProfileDto createdProfile = profileManagerService.createUserProfile(email, adminProfileDto);
		return ResponseEntity.ok(createdProfile);
	}

	@PostMapping("/create/recruiter/{email}")
	public ResponseEntity<RecruiterProfileDto> createRecruiterProfile(@PathVariable String email,
			RecruiterProfileDto recruiterProfileDto) {
		RecruiterProfileDto createdProfile = profileManagerService.createUserProfile(email, recruiterProfileDto);
		return ResponseEntity.ok(createdProfile);
	}

	@PostMapping("/create/teacher/{email}")
	public ResponseEntity<TeacherProfileDto> createTeacherProfile(@PathVariable String email,
			TeacherProfileDto teacherProfileDto) {
		TeacherProfileDto createdProfile = profileManagerService.createUserProfile(email, teacherProfileDto);
		return ResponseEntity.ok(createdProfile);
	}

	@PostMapping("/create/student/{email}")
	public ResponseEntity<StudentProfileDto> createStudentProfile(@PathVariable String email,
			StudentProfileDto studentProfileDto) {
		StudentProfileDto createdProfile = profileManagerService.createUserProfile(email, studentProfileDto);
		return ResponseEntity.ok(createdProfile);
	}

	@PutMapping("/update/admin/{email}")
	public ResponseEntity<AdminProfileDto> updateAdminProfile(@PathVariable String email, AdminProfileDto profileDto) {
		AdminProfileDto updateProfile = profileManagerService.updateUserProfile(email, profileDto);
		return ResponseEntity.ok(updateProfile);
	}

	@PutMapping("/update/recruiter/{email}")
	public ResponseEntity<RecruiterProfileDto> updateRecruiterProfile(@PathVariable String email,
			RecruiterProfileDto profileDto) {
		RecruiterProfileDto updateProfile = profileManagerService.updateUserProfile(email, profileDto);
		return ResponseEntity.ok(updateProfile);
	}

	@PutMapping("/update/teacher/{email}")
	public ResponseEntity<TeacherProfileDto> updateTeacherProfile(@PathVariable String email,
			TeacherProfileDto profileDto) {
		TeacherProfileDto updateProfile = profileManagerService.updateUserProfile(email, profileDto);
		return ResponseEntity.ok(updateProfile);
	}

	@PutMapping("/update/student/{email}")
	public ResponseEntity<StudentProfileDto> updateStudentProfile(@PathVariable String email,
			StudentProfileDto profileDto) {
		StudentProfileDto updateProfile = profileManagerService.updateUserProfile(email, profileDto);
		return ResponseEntity.ok(updateProfile);
	}

	@DeleteMapping("/delete/{email}")
	public ResponseEntity<Map<String, Object>> deleteProfile(@PathVariable String email) {
		profileManagerService.deleteUserProfile(email);
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("Message: ", "Profile successfully deleted...");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get/{email}")
	public ResponseEntity<ProfileDto> getProfileByEmail(@PathVariable String email) {
		UserDto userDto = userService.getUserByEmail(email);
		String userType = userDto.getUserType().getName().replace("ROLE_", "").toUpperCase(); // Skip "ROLE_"
		RoleTypeEnum role = RoleTypeEnum.valueOf(userType);

		switch (role) {
		case ADMIN:
			return ResponseEntity.ok(profileManagerService.getUserProfile(userDto, AdminProfileDto.class));
		case RECRUITER:
			return ResponseEntity.ok(profileManagerService.getUserProfile(userDto, RecruiterProfileDto.class));
		case TEACHER:
			return ResponseEntity.ok(profileManagerService.getUserProfile(userDto, TeacherProfileDto.class));
		case STUDENT:
			return ResponseEntity.ok(profileManagerService.getUserProfile(userDto, StudentProfileDto.class));
		default:
			throw new EntityNotFoundException("Role does not exists.");
		}
	}
}
