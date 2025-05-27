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
public class AdminProfileDto {

	private String firstName;
	private String lastName;
	private String bio;
	private String mobile;
	private String gender;
	private LocalDate dateOfBirth;
}
