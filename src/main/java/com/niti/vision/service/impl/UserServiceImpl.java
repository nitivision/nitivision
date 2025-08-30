package com.niti.vision.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.niti.vision.dto.UserDto;
import com.niti.vision.entity.Role;
import com.niti.vision.entity.User;
import com.niti.vision.repository.CandidateRepository;
import com.niti.vision.repository.ContactMessageRepository;
import com.niti.vision.repository.JobRepository;
import com.niti.vision.repository.RoleRepository;
import com.niti.vision.repository.UserRepository;
import com.niti.vision.service.EmailService;
import com.niti.vision.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	EmailService emailService;
	@Autowired
    private JavaMailSender mailSender;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private CandidateRepository candidateRepository;
    private ContactMessageRepository contactMessageRepository;
    private JobRepository jobRepository;
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           CandidateRepository candidateRepository,
                           ContactMessageRepository contactMessageRepository,
                           JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.candidateRepository=candidateRepository;
        this.contactMessageRepository=contactMessageRepository;
        this.jobRepository=jobRepository;
    }

    @Override
    public void saveUser(UserDto userDto) {
    	 User user = mapToEntity(userDto);

         // encode password
         user.setPassword(passwordEncoder.encode(userDto.getPassword()));

         // set default role (ROLE_USER)
         Role role = roleRepository.findByName("USER")
                 .orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));
         user.getRoles().add(role);

         // default user is inactive until approved by admin
         user.setActive(false);
         System.out.println("USER : "+user.toString());
         userRepository.save(user);
        
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

	/*
	 * @Override public Optional<User> findByEmails(String email) { return
	 * userRepository.findByEmails(email); }
	 */
	
	@Override
	public List<UserDto> findAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map((user) -> convertEntityToDto(user)).collect(Collectors.toList());
	}
	 
	/*
	 * @Override public List<UserDto> findAllUsers() { return
	 * userRepository.findAll() .stream() .map(this::mapToDto) // Entity -> DTO
	 * .collect(Collectors.toList()); }
	 */
	
	private UserDto convertEntityToDto(User user) {
		UserDto userDto = new UserDto(); // String[] name = user.getName().split(" ");
		userDto.setId(user.getId());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setActive(user.getActive());
		userDto.setDeactivatedAt(user.getDeactivatedAt());
		userDto.setCreated_dt(user.getCreatedAt());
		return userDto;
	}
	 

	/*
	 * private Role checkRoleExist() { Role role = new Role();
	 * role.setName("ROLE_ADMIN"); return roleRepository.save(role); }
	 */
    public void sendOtpToEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setOtp(otp);
        user.setOtpGeneratedAt(System.currentTimeMillis());

        userRepository.save(user); // Save updated user with OTP

        sendOtpEmail(email, otp); // Send the OTP email
    }
    public boolean verifyOtpAndResetPassword(String email, String otp, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false; // User not found
        }
        long now = System.currentTimeMillis();
        // Check OTP match and expiration (5 minutes)
        if (!otp.equals(user.getOtp()) || (now - user.getOtpGeneratedAt()) > 10 * 60 * 1000) {
            return false; // OTP is invalid or expired
        }
        // Encode new password
        user.setPassword(passwordEncoder.encode(newPassword));
        // Clear OTP fields
        user.setOtp(null);
        user.setOtpGeneratedAt(null);
        // Save updated user
        userRepository.save(user);
        return true;
    }

    private void sendOtpEmail(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Niti Vision Password Reset OTP");
            helper.setFrom("nitivisionglobal@gmail.com");

            String html = "<html><body>" +
                    "<h3>Password Reset OTP</h3>" +
                    "<p>Dear user,</p>" +
                    "<p>Your OTP is: <strong>" + otp + "</strong></p>" +
                    "<p>This OTP will expire in 5 minutes.</p>" +
                    "<br/>" +
                    "<p>Regards,<br/>Niti Vision Team</p>" +
                    "</body></html>";

            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
    
    @Override
    public void updateStatus(Long id, Boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        boolean isActive = active != null && active;
        user.setActive(isActive);

        if (isActive) {
            user.setDeactivatedAt(null);

            // Restore default role(s) if needed
            if (user.getRoles().isEmpty()) {
                Role defaultRole = roleRepository.findByName("ADMIN")
                        .orElseThrow(() -> new RuntimeException("Default role not found"));
                user.getRoles().add(defaultRole);
            }
        } else {
            user.setDeactivatedAt(LocalDateTime.now());

            // Remove all roles (will remove rows from users_roles table)
            user.getRoles().clear();
        }

        userRepository.save(user);
    }
    
	/*
	 * @Override public void updateStatus(Long id, Boolean active) { User user =
	 * userRepository.findById(id) .orElseThrow(() -> new
	 * RuntimeException("User not found with id: " + id)); user.setActive(active !=
	 * null && active); userRepository.save(user); }
	 */

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToDto) // Entity -> DTO
                .orElse(null);
    }
    
    private User mapToEntity(UserDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setCreatedAt(LocalDateTime.now()); 
        return user;
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setDeactivatedAt(user.getDeactivatedAt());
        dto.setCreated_dt(user.getCreatedAt()); 
        dto.setActive(user.getActive() != null ? user.getActive() : false);
        return dto;
    }
    
    @Override
    public String sendOtpForPasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
        	return "NOT_FOUND";
        }

        boolean isActive = Boolean.TRUE.equals(user.getActive());
         if(!isActive) {
	       return "INACTIVE";
          }
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        user.setOtp(otp);
        user.setOtpGeneratedAt(System.currentTimeMillis());
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);

        return "OTP_SENT";
    }

    @Override
    public void deleteUsersByIds(List<Long> ids) {
        for (Long id : ids) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

            // Optional: Clear roles or child associations before deletion
            user.getRoles().clear();
            userRepository.delete(user);
        }
    }
    
    
    
    @Override
    public List<User> getUsersByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return userRepository.findBycreatedAtBetween(startDateTime, endDateTime);
    }
    
    
    public long countAllUsers() {
        return userRepository.count();
    }
    public long countAllMessages() {
        return contactMessageRepository.count();
    }
    public long countAllJobs() {
        return jobRepository.count();
    }
    public long countAllCandidates() {
        return candidateRepository.count();
    }
}
