package com.zidio.connect.controller.profile;

import java.util.LinkedHashMap;
import java.util.List;
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

import com.zidio.connect.dto.TeacherProfileDto;
import com.zidio.connect.service.TeacherProfileService;

@RestController
@RequestMapping("/api/v1/teacher-profile")
public class TeacherProfileController {

	@Autowired
	TeacherProfileService teacherProfileService;

	@PostMapping("/create/{email}")
	public ResponseEntity<TeacherProfileDto> createProfile(TeacherProfileDto profileDto, @PathVariable("email") String userEmail) {
		// userDto should contains email
		TeacherProfileDto savedProfile = teacherProfileService.createTeacherProfile(profileDto, userEmail);
		return ResponseEntity.ok(savedProfile);
	}

	@PutMapping("/update/{email}")
	public ResponseEntity<TeacherProfileDto> updateProfile(TeacherProfileDto profileDto, @PathVariable("email") String userEmail) {
		TeacherProfileDto updatedProfile = teacherProfileService.updateTeacherProfile(profileDto, userEmail);
		return ResponseEntity.ok(updatedProfile);
	}

	@DeleteMapping("/delete/{email}")
	public ResponseEntity<Map<String, Object>> deleteProfile(@PathVariable String email) {
		teacherProfileService.deleteTeacherProfileByEmail(email);
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("Response Message: ", "Student profile successfully deleted...");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get/id/{id}")
	public ResponseEntity<TeacherProfileDto> getProfileById(@PathVariable Long id) {
		TeacherProfileDto profile = teacherProfileService.getTeacherProfileById(id);
		return ResponseEntity.ok(profile);
	}

	@GetMapping("/get/email/{email}")
	public ResponseEntity<TeacherProfileDto> getProfileByEmail(@PathVariable String email) {
		TeacherProfileDto profile = teacherProfileService.getTeacherProfileByEmail(email);
		return ResponseEntity.ok(profile);
	}

	@GetMapping("/get")
	public ResponseEntity<List<TeacherProfileDto>> getAllProfiles() {
		List<TeacherProfileDto> allProfiles = teacherProfileService.getAllTeacherProfiles();
		return ResponseEntity.ok(allProfiles);
	}
}
