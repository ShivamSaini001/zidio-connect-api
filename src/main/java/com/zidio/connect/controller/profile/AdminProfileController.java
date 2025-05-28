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

import com.zidio.connect.dto.AdminProfileDto;
import com.zidio.connect.service.AdminProfileService;

@RestController
@RequestMapping("/api/v1/recruiter-profile")
public class AdminProfileController {

	@Autowired
	private AdminProfileService adminProfileService;

	@PostMapping("/create/{email}")
	public ResponseEntity<AdminProfileDto> createProfile(AdminProfileDto profileDto,
			@PathVariable("email") String userEmail) {
		// userDto should contains email
		AdminProfileDto savedProfile = adminProfileService.createAdminProfile(profileDto, userEmail);
		return ResponseEntity.ok(savedProfile);
	}

	@PutMapping("/update/{email}")
	public ResponseEntity<AdminProfileDto> updateProfile(AdminProfileDto profileDto,
			@PathVariable("email") String userEmail) {
		AdminProfileDto updatedProfile = adminProfileService.updateAdminProfile(profileDto, userEmail);
		return ResponseEntity.ok(updatedProfile);
	}

	@DeleteMapping("/delete/{email}")
	public ResponseEntity<Map<String, Object>> deleteProfile(@PathVariable String email) {
		adminProfileService.deleteAdminProfileByEmail(email);
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("Response Message: ", "Student profile successfully deleted...");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get/id/{id}")
	public ResponseEntity<AdminProfileDto> getProfileById(@PathVariable Long id) {
		AdminProfileDto profile = adminProfileService.getAdminProfileById(id);
		return ResponseEntity.ok(profile);
	}

	@GetMapping("/get/email/{email}")
	public ResponseEntity<AdminProfileDto> getProfileByEmail(@PathVariable String email) {
		AdminProfileDto profile = adminProfileService.getAdminProfileByEmail(email);
		return ResponseEntity.ok(profile);
	}

	@GetMapping("/get")
	public ResponseEntity<List<AdminProfileDto>> getAllProfiles() {
		List<AdminProfileDto> allProfiles = adminProfileService.getAllAdminProfiles();
		return ResponseEntity.ok(allProfiles);
	}
}
