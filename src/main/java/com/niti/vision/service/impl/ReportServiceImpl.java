package com.niti.vision.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.niti.vision.repository.CandidateRepository;
import com.niti.vision.repository.ContactMessageRepository;
import com.niti.vision.repository.JobRepository;
import com.niti.vision.repository.UserRepository;
import com.niti.vision.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService{

    private final UserRepository userRepository;
    private final ContactMessageRepository contactRepository;
    private final JobRepository jobRepository;
    private final CandidateRepository candidateRepository;

    public ReportServiceImpl(UserRepository userRepository,
    		ContactMessageRepository contactRepository,
                         JobRepository jobRepository,
                         CandidateRepository candidateRepository) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.jobRepository = jobRepository;
        this.candidateRepository = candidateRepository;
    }

    public List<String> getRecentActivity() {
        List<String> activity = new ArrayList<>();

        userRepository.findTop5ByOrderByCreatedAtDesc()
                .forEach(u -> activity.add("🟢 New user registered: " + u.getFirstName()));

        contactRepository.findTop5ByOrderByCreatedAtDesc()
                .forEach(c -> activity.add("✉️ New message from: " + c.getEmail()));

        jobRepository.findTop5ByOrderByCreatedAtDesc()
                .forEach(j -> activity.add("💼 Job posted: " + j.getTitle()));

        candidateRepository.findTop5ByOrderByCreatedAtDesc()
                .forEach(c -> activity.add("👤 Candidate applied: " + c.getName()));

        return activity;
    }
}

