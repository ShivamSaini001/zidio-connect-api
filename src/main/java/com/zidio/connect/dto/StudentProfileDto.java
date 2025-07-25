package com.zidio.connect.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zidio.connect.entities.Address;
import com.zidio.connect.entities.Education;
import com.zidio.connect.entities.Skill;
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
	private String tagline;
	private String bio;
	private String resumeUrl;
	private String linkedInUrl;
	private String githubUrl;
	private String gender;
	private String mobile;
	private LocalDate dateOfBirth;
	private String profileImageUrl;

	private List<EducationDto> educations;
	private Set<SkillDto> skills = new HashSet<>();
	private Address address;
	private UserDto userDto;
}
