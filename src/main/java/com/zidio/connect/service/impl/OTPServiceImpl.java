package com.zidio.connect.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.zidio.connect.dto.OtpResponseDto;
import com.zidio.connect.exception.OtpException;
import com.zidio.connect.service.OTPService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Service
public class OTPServiceImpl implements OTPService {

	private static final Long OTP_VALIDITY_IN_MINUTES = 10l; // Time in Minutes
	private static final Long OTP_RESEND_AFTER_SECONDS = (1 * 60l); // Time in Seconds

	private JavaMailSender mailSender;

	private Map<String, VerificationDetails> userEmailVerification = new LinkedHashMap<>();

	// Constructor
	public OTPServiceImpl(JavaMailSender mailSender) {
		super();
		this.mailSender = mailSender;
	}

	@Setter
	@Getter
	@AllArgsConstructor
	@ToString
	private class VerificationDetails {
		private String otp;
		private LocalDateTime sendOtpAt;

		public boolean isExpiredOtp(String otp) {
			return this.sendOtpAt.plusMinutes(OTP_VALIDITY_IN_MINUTES).isBefore(LocalDateTime.now());
		}

		public boolean isValidOtp(String otp) {
			return this.otp.equals(otp) ? true : false;
		}
	}

	@Override
	public OtpResponseDto otpToEmail(String email, int digits) {
		VerificationDetails verificationDetails = userEmailVerification.get(email);
		// Check Resend Otp validity
		if (verificationDetails != null && verificationDetails.getSendOtpAt().plusSeconds(OTP_RESEND_AFTER_SECONDS)
				.isAfter(LocalDateTime.now())) {
			throw new OtpException("You cannot send otp before " + OTP_RESEND_AFTER_SECONDS + " Seconds", 400);
		}

		String otp = generateOtp(digits);
		try {
			// Send Email to user email.
			this.sendOtpToEmail(email, otp);
			// Add otp to Map
			this.putDetails(email, otp);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return new OtpResponseDto(email, OTP_VALIDITY_IN_MINUTES, OTP_RESEND_AFTER_SECONDS, userEmailVerification.get(email).getSendOtpAt());
	}

	public void sendOtpToEmail(String to, String otp) throws MessagingException {
		// Sending Email with html Formating like professional message by gmail, github
		// etc.
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, true);

		mailMessage.setTo(to);
		mailMessage.setFrom("Zidio Connect Verification  <" + to + ">");
		mailMessage.setSubject("Your One-Time Password (OTP) for Registration");
		String bodyText = "<div style=\"font-family: Arial, sans-serif; text-align: center; padding: 20px;\">"
				+ "<h2 style=\"color: #333;\">Your One-Time Password (OTP)</h2>"
				+ "<p style=\"font-size: 16px;\">Use the following OTP to complete your registration:</p>"
				+ "<h1 style=\"color: #4CAF50; font-size: 36px;\">" + otp + "</h1>"
				+ "<p style=\"font-size: 14px; color: #666;\">This code is valid for " + OTP_VALIDITY_IN_MINUTES
				+ " minutes. Do not share it with anyone.</p>" + "<hr style=\"margin: 20px 0;\">"
				+ "<p style=\"font-size: 12px; color: #999;\">If you did not request this, please ignore this email or contact support.</p>"
				+ "</div>";
		mailMessage.setText(bodyText, true);
		mailSender.send(mimeMessage);
	}

	public void putDetails(String email, String otp) {
		VerificationDetails verificationDetails = userEmailVerification.get(email);
		if (verificationDetails == null) {
			verificationDetails = new VerificationDetails(otp, LocalDateTime.now());
			userEmailVerification.put(email, verificationDetails);
		} else {
			verificationDetails.setOtp(otp);
			verificationDetails.setSendOtpAt(LocalDateTime.now());
		}
	}

	@Override
	public boolean verifyOtp(String email, String otp) {
		VerificationDetails verificationDetails = userEmailVerification.get(email);
		if (verificationDetails == null) {
			return false;
		}
		// Otp is Expired
		if (verificationDetails.isExpiredOtp(otp)) {
			throw new OtpException("Your OTP has been expired. Please Try again!!", 400);
		}
		// invalid otp
		if (!verificationDetails.isValidOtp(otp)) {
			throw new OtpException("Please Enter a valid OTP!!", 400);
		}
		return true;
	}

	public static String generateOtp(int otpLength) {
		StringBuilder otp = new StringBuilder();
		SecureRandom random = new SecureRandom();

		int digit;
		for (int index = 1; index <= otpLength; index++) {
			digit = random.nextInt(10);
			if (index == 1 && digit == 0) {
				index--;
			} else {
				otp.append(digit);
			}
		}
		return otp.toString();
	}

	@Override
	public void clearUserDetails(String email) {
		if (userEmailVerification.containsKey(email)) {
			userEmailVerification.remove(email);
		}
	}
}
