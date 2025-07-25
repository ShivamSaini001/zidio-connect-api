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
	private String tagline;
	private String gender;
	private String mobile;
	private String bio;
	private String linkedInUrl;
	private String githubUrl;
	private LocalDate dateOfBirth;

	@OneToOne
	@JoinColumn(name = "profile_image")
	private CloudinaryFile profileImage;
	
	@OneToOne
	private CloudinaryFile resume;

	@OneToOne
	@MapsId
	@JoinColumn(name = "userId", nullable = true)
	private User user;

	public StudentProfile(String firstName, String lastName, User user) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.user = user;
	}
	
}
