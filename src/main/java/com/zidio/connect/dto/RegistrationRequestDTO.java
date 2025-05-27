package com.zidio.connect.dto;

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
public class RegistrationRequestDTO {

	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String profileImageUrl;
	private String selectedRole;
	private String verificationCode;
}
