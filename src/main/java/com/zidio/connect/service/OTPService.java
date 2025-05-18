package com.zidio.connect.service;

import com.zidio.connect.dto.OtpResponseDto;

public interface OTPService {

	OtpResponseDto otpToEmail(String email, int digits);
	boolean verifyOtp(String email, String otp);
	void clearUserDetails(String email);
}
