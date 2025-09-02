package com.niti.vision.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.niti.vision.service.impl.EmailService;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
public class EmailController {

	private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    
    @GetMapping("/emailService")
   	public String emailServicePage() {
   	    return "emailService"; // popupForm.html under /templates
   	}
    
    @PostMapping(value = "/send-email", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> sendEmail(
            @RequestParam("to") String to,
            @RequestParam(value = "cc", required = false) String cc,
            @RequestParam(value = "bcc", required = false) String bcc,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,   // <-- this will contain your editor.innerHTML
            @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
    	System.out.println("Start Controller sendEmail method");
        try {
            emailService.sendEmail(to, cc, bcc, subject, message, attachments);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Email sent successfully!");
            System.out.println("END Controller sendEmail method");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}