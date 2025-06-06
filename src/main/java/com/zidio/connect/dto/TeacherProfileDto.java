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
public class TeacherProfileDto implements ProfileDto {

	private String firstName;
	private String lastName;
	private String bio;
	private String gender;
	private String mobile;
	private String specialization;
	private String highestQualification;
	private int yearOfExperience;
	private LocalDate dateOfBirth;
	private String profileImageUrl;
	private UserDto userDto;
}
