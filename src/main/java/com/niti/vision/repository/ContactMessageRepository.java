package com.niti.vision.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niti.vision.entity.ContactMessage;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
	List<ContactMessage> findTop5ByOrderByCreatedAtDesc();
	int countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
