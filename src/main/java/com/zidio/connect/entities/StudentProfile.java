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
public class StudentProfile {

	@Id
	private Long userId; // Matches users.userId
	private String firstName;
	private String lastName;
	private String bio;
	private String resumeUrl;
	private String linkedInUrl;
	private String gender;
	private String mobile;
	private LocalDate dateOfBirth;
	private String profileImageUrl;

	@OneToOne
	@MapsId
	@JoinColumn(name = "userId", nullable = true)
	private User user;
}
