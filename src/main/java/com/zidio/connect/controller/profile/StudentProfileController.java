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

import com.zidio.connect.dto.StudentProfileDto;
import com.zidio.connect.service.StudentProfileService;

@RestController
@RequestMapping("/api/v1/student/profile")
public class StudentProfileController {

	@Autowired
	StudentProfileService studentProfileService;

	@PostMapping("/create/{email}")
	public ResponseEntity<StudentProfileDto> createProfile(StudentProfileDto profileDto, @PathVariable("email") String userEmail) {
		// userDto should contains email
		StudentProfileDto savedProfile = studentProfileService.createStudentProfile(profileDto, userEmail);
		return ResponseEntity.ok(savedProfile);
	}

	@PutMapping("/update/{email}")
	public ResponseEntity<StudentProfileDto> updateProfile(StudentProfileDto profileDto, @PathVariable("email") String userEmail) {
		StudentProfileDto updatedStudentProfile = studentProfileService.updateStudentProfile(profileDto, userEmail);
		return ResponseEntity.ok(updatedStudentProfile);
	}

	@DeleteMapping("/delete/{email}")
	public ResponseEntity<Map<String, Object>> deleteProfile(@PathVariable String email) {
		studentProfileService.deleteStudentProfileByEmail(email);
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("Response Message: ", "Student profile successfully deleted...");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get/id/{id}")
	public ResponseEntity<StudentProfileDto> getProfileById(@PathVariable Long id) {
		StudentProfileDto studentProfile = studentProfileService.getStudentProfileById(id);
		return ResponseEntity.ok(studentProfile);
	}

	@GetMapping("/get/email/{email}")
	public ResponseEntity<StudentProfileDto> getProfileByEmail(@PathVariable String email) {
		StudentProfileDto studentProfile = studentProfileService.getStudentProfileByEmail(email);
		return ResponseEntity.ok(studentProfile);
	}

	@GetMapping("/get")
	public ResponseEntity<List<StudentProfileDto>> getAllProfiles() {
		List<StudentProfileDto> allStudentProfiles = studentProfileService.getAllStudentProfiles();
		return ResponseEntity.ok(allStudentProfiles);
	}
}
