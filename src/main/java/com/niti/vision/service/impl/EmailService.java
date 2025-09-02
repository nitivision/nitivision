package com.niti.vision.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String cc, String bcc,
                          String subject, String htmlBody,
                          MultipartFile[] attachments) throws Exception {
    	System.out.println("Start Service sendEmail method");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);

        if (cc != null && !cc.isEmpty()) {
            helper.setCc(cc.split(","));
        }
        if (bcc != null && !bcc.isEmpty()) {
            helper.setBcc(bcc.split(","));
        }

        helper.setSubject(subject);

        // set HTML body
        helper.setText(htmlBody, true);

        // add attachments
        if (attachments != null) {
            for (MultipartFile file : attachments) {
                if (!file.isEmpty()) {
                    helper.addAttachment(file.getOriginalFilename(), file);
                }
            }
        }
        System.out.println("END Controller sendEmail method");
        mailSender.send(mimeMessage);
    }
}