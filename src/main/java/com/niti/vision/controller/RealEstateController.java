package com.niti.vision.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.niti.vision.entity.Blog;
import com.niti.vision.repository.BlogRepository;

@Controller
public class RealEstateController {

    private final BlogRepository blogRepo;

    public RealEstateController(BlogRepository blogRepo) {
        this.blogRepo = blogRepo;
    }

    @GetMapping("/realestate")
    public String realEstatePage(Model model) {
        // Fetch blogs sorted by date DESC
        List<Blog> blogs = blogRepo.findAllByOrderByPublishedDateDesc();

        // Pass to Thymeleaf
        model.addAttribute("blogs", blogs);

        // For local SEO targeting (dynamic city)
        model.addAttribute("city", "Delhi"); // You can set dynamically later

        return "realestate"; // maps to realestate.html
    }
}

