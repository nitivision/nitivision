package com.niti.vision.service;

import java.time.LocalDateTime;
import java.util.List;

import com.niti.vision.dto.UserDto;
import com.niti.vision.entity.User;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    //Optional<User> findByEmail(String email);
    boolean verifyOtpAndResetPassword(String email, String otp, String newPassword);
    void sendOtpToEmail(String email);
    //Optional<User> findByEmails(String email);
    
    List<UserDto> findAllUsers();
    UserDto findById(Long id);
    void updateStatus(Long id, Boolean active);
    String sendOtpForPasswordReset(String email);

	void deleteUsersByIds(List<Long> ids);
	List<User> getUsersByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime);
	long countAllUsers();
	 public long countAllMessages();
	    public long countAllJobs();
	    public long countAllCandidates() ;
	
}
