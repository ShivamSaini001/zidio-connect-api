package com.zidio.connect.dto;

import java.time.LocalDateTime;

import com.zidio.connect.entities.Role;

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
public class UserDto {

	private String email;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean isActivated;
	private Role userType;
}
