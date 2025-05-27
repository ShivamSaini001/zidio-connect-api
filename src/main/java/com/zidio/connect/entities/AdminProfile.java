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
public class AdminProfile {

	@Id
	private Long userid; // Matches users.userId
	private String firstName;
	private String lastName;
	private String bio;
	private String mobile;
	private String gender;
	private LocalDate dateOfBirth;
	private String profileImageUrl;

	@OneToOne
	@MapsId
	@JoinColumn(name = "userId")
	private User user;
}
