package com.niti.vision.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niti.vision.entity.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
	List<Candidate> findTop5ByOrderByCreatedAtDesc();
}
