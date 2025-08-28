package com.niti.vision.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.niti.vision.service.EmailService;
@Service
public class EmailServiceImpl implements EmailService{
	@Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(fromEmail);
        message.setSubject("Password Reset OTP - Niti Vision");
        message.setText("Your OTP for password reset is: " + otp + "\n\nIt is valid for 10 minutes.");

        javaMailSender.send(message);
    }}
