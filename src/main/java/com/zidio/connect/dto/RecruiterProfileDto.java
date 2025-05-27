package com.zidio.connect.dto;

import java.time.LocalDate;

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
public class RecruiterProfileDto {

	private String firstName;
	private String lastName;
	private String bio;
	private String mobile;
	private String gender;
	private LocalDate dateOfBirth;
	private long totalPostedJobs;
	private long totalPostedInternships;
	private String designation; // Hr Manager, Technical Recruiter
}
