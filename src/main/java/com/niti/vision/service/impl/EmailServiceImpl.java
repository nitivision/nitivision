package com.niti.vision.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.niti.vision.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	private JavaMailSender javaMailSender;

	public EmailServiceImpl(JavaMailSender mailSender) {
		this.javaMailSender = mailSender;
	}

	@Override
	public void sendOtpEmail(String to, String otp) {
		System.out.println("Start sendOtpEmail method");
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setFrom("nitivisionglobal@gmail.com");
			helper.setTo(to);
			helper.setSubject("Password Reset OTP - Niti Vision");
			helper.setText("Your OTP for password reset is: " + otp + "\n\nIt is valid for 10 minutes.");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("End sendOtpEmail method");
		javaMailSender.send(mimeMessage);
	}
}
