package com.niti.vision.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.niti.vision.entity.Blog;
import com.niti.vision.repository.BlogRepository;

@Controller
public class BlogController {

    private final BlogRepository blogRepo;

    public BlogController(BlogRepository blogRepo) {
        this.blogRepo = blogRepo;
    }

    @GetMapping("/viewBlog/{id}")
    public String viewBlog(@PathVariable Long id, Model model) {
        Blog blog = blogRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with ID " + id));

        model.addAttribute("blog", blog);
        return "blog-detail"; // You need blog-detail.html
    }
    @GetMapping("/blog/{id}")
    public String getBlogDetail(@PathVariable Long id, Model model) {
        Blog blog = blogRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        // Fetch related blogs from same cluster, exclude current one
        List<Blog> relatedBlogs = blogRepo
                .findTop3ByClusterAndIdNotOrderByPublishedDateDesc(blog.getCluster(), blog.getId());

        model.addAttribute("blog", blog);
        model.addAttribute("relatedBlogs", relatedBlogs);
        return "blog-detail";
    }

}

