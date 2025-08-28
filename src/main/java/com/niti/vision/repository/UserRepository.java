package com.niti.vision.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niti.vision.dto.UserDto;
import com.niti.vision.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findTop5ByOrderByCreatedAtDesc();
	List<User> findBycreatedAtBetween(LocalDateTime start, LocalDateTime end);
	User findByEmail(String email);
	int countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
