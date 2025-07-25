package com.zidio.connect.controller.profile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zidio.connect.config.security.jwt.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zidio.connect.dto.StudentProfileDto;
import com.zidio.connect.service.StudentProfileService;

@RestController
@RequestMapping("/api/v1/student/profile")
public class StudentProfileController {

	@Autowired
	StudentProfileService studentProfileService;

	@Autowired
	private JwtHelper jwtHelper;

	@PostMapping("/create/{email}")
	public ResponseEntity<StudentProfileDto> createProfile(StudentProfileDto profileDto, @PathVariable("email") String userEmail) {
		// userDto should contains email
		StudentProfileDto savedProfile = studentProfileService.createStudentProfile(profileDto, userEmail);
		return ResponseEntity.ok(savedProfile);
	}

	@PutMapping("/update")
	public ResponseEntity<StudentProfileDto> updateProfile(@RequestBody StudentProfileDto profileDto, HttpServletRequest request) {
		// Get email from jwt token
		String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));

		StudentProfileDto updatedStudentProfile = studentProfileService.updateStudentProfile(profileDto, email);
		return ResponseEntity.ok(updatedStudentProfile);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, Object>> deleteProfile(HttpServletRequest request) {
		// Get email from jwt token
		String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));

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

	@GetMapping("/get/email")
	public ResponseEntity<StudentProfileDto> getProfileByEmail(HttpServletRequest request) {
		// Get email from jwt token
		String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));

		StudentProfileDto studentProfile = studentProfileService.getStudentProfileByEmail(email);
		return ResponseEntity.ok(studentProfile);
	}

	@GetMapping("/get")
	public ResponseEntity<List<StudentProfileDto>> getAllProfiles() {
		List<StudentProfileDto> allStudentProfiles = studentProfileService.getAllStudentProfiles();
		return ResponseEntity.ok(allStudentProfiles);
	}
}
