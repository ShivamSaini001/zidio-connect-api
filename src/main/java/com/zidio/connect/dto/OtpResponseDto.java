package com.zidio.connect.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtpResponseDto {

	private String otpEmail;
	private Long OtpValidityInMinutes;
	private Long OtpResendAfterSeconds;
	private LocalDateTime sendAt;
}
