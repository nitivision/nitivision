package com.niti.vision.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niti.vision.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
	List<Job> findTop5ByOrderByCreatedAtDesc();
}
