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
public class StudentProfileDto implements ProfileDto {

	private String firstName;
	private String lastName;
	private String bio;
	private String resumeUrl;
	private String linkedInUrl;
	private String gender;
	private String mobile;
	private LocalDate dateOfBirth;
	private String profileImageUrl;
	private UserDto userDto;
}
