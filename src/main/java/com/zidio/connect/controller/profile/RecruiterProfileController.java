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

import com.zidio.connect.dto.RecruiterProfileDto;
import com.zidio.connect.service.RecruiterProfileService;

@RestController
@RequestMapping("/api/v1/recruiter/profile")
public class RecruiterProfileController {

	@Autowired
	private RecruiterProfileService recruiterProfileService;

	@PostMapping("/create/{email}")
	public ResponseEntity<RecruiterProfileDto> createProfile(RecruiterProfileDto profileDto, @PathVariable("email") String userEmail) {
		// userDto should contains email
		RecruiterProfileDto savedProfile = recruiterProfileService.createRecruiterProfile(profileDto, userEmail);
		return ResponseEntity.ok(savedProfile);
	}

	@PutMapping("/update/{email}")
	public ResponseEntity<RecruiterProfileDto> updateProfile(RecruiterProfileDto profileDto, @PathVariable("email") String userEmail) {
		RecruiterProfileDto updatedProfile = recruiterProfileService.updateRecruiterProfile(profileDto, userEmail);
		return ResponseEntity.ok(updatedProfile);
	}

	@DeleteMapping("/delete/{email}")
	public ResponseEntity<Map<String, Object>> deleteProfile(@PathVariable String email) {
		recruiterProfileService.deleteRecruiterProfileByEmail(email);
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("Response Message: ", "Student profile successfully deleted...");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get/id/{id}")
	public ResponseEntity<RecruiterProfileDto> getProfileById(@PathVariable Long id) {
		RecruiterProfileDto profile = recruiterProfileService.getRecruiterProfileById(id);
		return ResponseEntity.ok(profile);
	}

	@GetMapping("/get/email/{email}")
	public ResponseEntity<RecruiterProfileDto> getProfileByEmail(@PathVariable String email) {
		RecruiterProfileDto profile = recruiterProfileService.getRecruiterProfileByEmail(email);
		return ResponseEntity.ok(profile);
	}

	@GetMapping("/get")
	public ResponseEntity<List<RecruiterProfileDto>> getAllProfiles() {
		List<RecruiterProfileDto> allProfiles = recruiterProfileService.getAllRecruiterProfiles();
		return ResponseEntity.ok(allProfiles);
	}
}
