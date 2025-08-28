package com.niti.vision.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.niti.vision.repository.ContactMessageRepository;
import com.niti.vision.repository.UserRepository;

@Service
public class UsageServiceImpl{

    private final UserRepository userRepository;
    private final ContactMessageRepository contactRepository;

    public UsageServiceImpl(UserRepository userRepository, ContactMessageRepository contactRepository) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
    }

    // Weekly user registrations (Mon–Sun)
    public List<Integer> getWeeklyRegistrations() {
        List<Integer> data = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (DayOfWeek day : DayOfWeek.values()) {
            LocalDate start = today.with(day); // start of that day
            LocalDate end = start.plusDays(1);

            int count = userRepository.countByCreatedAtBetween(start.atStartOfDay(), end.atStartOfDay());
            data.add(count);
        }
        return data;
    }

    // Weekly contact messages (Mon–Sun)
    public List<Integer> getWeeklyMessages() {
        List<Integer> data = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (DayOfWeek day : DayOfWeek.values()) {
            LocalDate start = today.with(day);
            LocalDate end = start.plusDays(1);

            int count = contactRepository.countByCreatedAtBetween(start.atStartOfDay(), end.atStartOfDay());
            data.add(count);
        }
        return data;
    }
}
