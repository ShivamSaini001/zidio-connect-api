package com.zidio.connect.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
@Entity
public class RecruiterProfile {

	@Id
	private Long userId;	// Matches users.userId
	private String firstName;
	private String lastName;
	private String bio;
	private String mobile;
	private String gender;
	private LocalDate dateOfBirth;
	private long totalPostedJobs;
	private long totalPostedInternships;
	private String designation; // Hr Manager, Technical Recruiter
	private String profileImageUrl;
		
	@OneToOne
	@MapsId
	@JoinColumn(name = "userId")
	private User user;
}
