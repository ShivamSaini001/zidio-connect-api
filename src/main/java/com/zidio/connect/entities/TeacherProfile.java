package com.zidio.connect.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.UniqueConstraint;
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
public class TeacherProfile {

	@Id
	private Long userId; // Matches users.userId
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

	@ElementCollection
	@CollectionTable(name = "teacher_specializations", joinColumns = @JoinColumn(name = "teacher_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
			"teacher_id", "specialization" }))
	@Column(name = "specialization")
	private List<String> specializations;

	@ElementCollection
	@CollectionTable(name = "teacher_languages", joinColumns = @JoinColumn(name = "teacher_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
			"teacher_id", "language" }))
	@Column(name = "language")
	private List<String> languagesKnown;

	// Teaching Preferences
	@ElementCollection
	@CollectionTable(name = "teacher_mode_of_teaching", joinColumns = @JoinColumn(name = "teacher_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
			"teacher_id", "modeOfTeaching" }))
	@Column(name = "modeOfTeaching")
	private List<String> modeOfTeaching;

	@ElementCollection
	@CollectionTable(name = "preferred_student_level", joinColumns = @JoinColumn(name = "teacher_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
			"teacher_id", "preferredStudentLevel" }))
	@Column(name = "preferredStudentLevel")
	private List<String> preferredStudentLevel;

	// Resume, Portfolio and Documents.
	@ElementCollection
	@CollectionTable(name = "certificate_urls", joinColumns = @JoinColumn(name = "teacher_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
			"teacher_id", "certificateUrl" }))
	@Column(name = "certificateUrl")
	private List<String> certificateUrls;
	private String portfolioWebsiteUrl;

	// System fields
	@Column(nullable = false)
	private boolean isVerified = true;
	private boolean isFeatured = true;
	private float teacherRating = 0.1f;
	private long totalEnrollments = 0;

	@OneToOne
	@MapsId
	@JoinColumn(name = "userId")
	private User user;

	public TeacherProfile(String firstName, String lastName, User user) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.user = user;
	}

}
