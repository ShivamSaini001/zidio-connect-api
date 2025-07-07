package com.zidio.connect.config.security.jwt.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class JwtLoginResponse {

	private String firstName;
	private String lastName;
	private String username;
	private String jwtToken;
	private List<String> roles;
	private boolean success;

	public JwtLoginResponse(String firstName, String lastName, String username, String jwtToken, List<String> roles,
			boolean success) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.jwtToken = jwtToken;
		this.roles = roles;
		this.success = success;
	}
}
