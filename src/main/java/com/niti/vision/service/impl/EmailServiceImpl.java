package com.niti.vision.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.niti.vision.service.EmailService;
@Service
public class EmailServiceImpl implements EmailService{
	
    private JavaMailSender javaMailSender;
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.javaMailSender = mailSender;
    }
    @Override
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("nitivisionglobal@gmail.com");
        message.setSubject("Password Reset OTP - Niti Vision");
        message.setText("Your OTP for password reset is: " + otp + "\n\nIt is valid for 10 minutes.");

        javaMailSender.send(message);
    }}
