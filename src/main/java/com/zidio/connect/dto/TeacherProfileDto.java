package com.zidio.connect.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeacherProfileDto implements ProfileDto {

	// Personal Information
	private String firstName;
	private String lastName;
	private String mobile;
	private String gender;
	private LocalDate dateOfBirth;
	private String bio;
	private String profileImageUrl;
	private String linkedinProfileUrl;

	// Professional Details
	private String designation;
	private int yearOfExperience;
	private String highestQualification;
	private List<String> specializations;
	private List<String> languagesKnown;
	private List<String> modeOfTeaching;
	private List<String> preferredStudentLevel;

	// Resume, Portfolio and Documents.
	private List<String> certificateUrls;
	private String portfolioWebsiteUrl;

	// System fields
	@JsonProperty("isVerified")
	private boolean isVerified;
	
	@JsonProperty("isFeatured")
	private boolean isFeatured;
	private float teacherRating;
	private long totalEnrollments;
	private UserDto userDto;
}
