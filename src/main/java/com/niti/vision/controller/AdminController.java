package com.niti.vision.controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niti.vision.dto.UserDto;
import com.niti.vision.entity.Job;
import com.niti.vision.entity.User;
import com.niti.vision.repository.CandidateRepository;
import com.niti.vision.repository.ContactMessageRepository;
import com.niti.vision.repository.JobRepository;
import com.niti.vision.service.ReportService;
import com.niti.vision.service.UserService;
import com.niti.vision.service.impl.UsageServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
public class AdminController {
	private static final String UPLOAD_DIR = "uploads/";
    @Autowired private JobRepository jobRepo;
    @Autowired private CandidateRepository candidateRepo;
    @Autowired private UserService userService;
    @Autowired private ContactMessageRepository repository;
    @Autowired private ReportService reportService;
    @Autowired private UsageServiceImpl usageService;

    @GetMapping("/dashboard")
	public String listRegisteredUsers(Model model,Principal principal) {

        model.addAttribute("totalUsers", userService.countAllUsers());
        model.addAttribute("totalMessages", userService.countAllMessages());
        model.addAttribute("totalJobs", userService.countAllJobs());
        model.addAttribute("totalCandidates", userService.countAllCandidates());

        model.addAttribute("recentActivity", reportService.getRecentActivity()); // List<String>

        model.addAttribute("weeklyLabels", Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        model.addAttribute("weeklyRegistrations", usageService.getWeeklyRegistrations()); // List<Integer>
        model.addAttribute("weeklyMessages", usageService.getWeeklyMessages()); // List<Integer>
		User currentUser = userService.findByEmail(principal.getName()); // Assuming email is used to log in
	    model.addAttribute("currentUser", currentUser);
		List<UserDto> users = userService.findAllUsers();
		System.out.println("USER from Dashboard"+users.toString());
		model.addAttribute("users", users);
		return "dashboard";
	}
	// Get all messages
	@GetMapping("/contactList")
	public String listContacts(Model model,Principal principal) {
		User currentUser = userService.findByEmail(principal.getName()); // Assuming email is used to log in
	    model.addAttribute("currentUser", currentUser);
		model.addAttribute("contacts", repository.findAll());
		return "contactList";
	}
	@GetMapping("/users")
	public String listUsers(Model model,Principal principal) {
		// model.addAttribute("title", "Admin Dashboard - Niti Vision");
		List<UserDto> users = userService.findAllUsers();
		System.out.println("USER from Controller : "+users.toString());
		User currentUser = userService.findByEmail(principal.getName()); // Assuming email is used to log in
	    model.addAttribute("currentUser", currentUser);
		model.addAttribute("users", users);
		return "users";
	}
    // Post Job Page
    @GetMapping("/postjob")
    public String postJob(Model model,Principal principal) {
    	User user = userService.findByEmail(principal.getName());
        model.addAttribute("currentUser", user);
        model.addAttribute("job", new Job());
        return "postjob";
    }

    @PostMapping("/postjob/save")
    public String saveJob(@ModelAttribute Job job, RedirectAttributes ra) {
        jobRepo.save(job);
        ra.addFlashAttribute("successMessage", "Job Posted Successfully!");
        return "redirect:/admin/postjob";
    }

    // Candidate Profiles
    @GetMapping("/candidates")
    public String candidates(Model model,Principal principal) {
    	User currentUser = userService.findByEmail(principal.getName()); // Assuming email is used to log in
	    model.addAttribute("currentUser", currentUser);
        model.addAttribute("candidates", candidateRepo.findAll());
        return "candidateprofile";
    }
    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @PostMapping("/users/{id}/status")
    @ResponseBody
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @RequestParam Boolean active) {
        userService.updateStatus(id, active);
        return  ResponseEntity.ok("Status updated successfully");
    }
    
    @PostMapping("/users/delete")
    @ResponseBody
    public ResponseEntity<?> deleteSelectedUsers(@RequestBody List<Long> userIds) {
        try {
            userService.deleteUsersByIds(userIds);
            return ResponseEntity.ok("Users deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete users");
        }
    }
    
    @GetMapping(value = "/users/export-csv", produces = "text/csv")
    public void exportUsersCsv(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletResponse response) throws IOException {

        // Set filename and content type
        String filename = "users_" + startDate + "_to_" + endDate + ".csv";
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        // Fetch filtered users based on date range (example: deactivatedAt or createdAt field)
        List<User> filteredUsers = userService.getUsersByDateRange(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));

        // Write CSV to response output stream
        PrintWriter writer = response.getWriter();
        writer.println("First Name,Last Name,Email,Deactivated Date,Created Date,Status");
        for (User user : filteredUsers) {
        	boolean isActive = Boolean.TRUE.equals(user.getActive());
            writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getDeactivatedAt() != null ? user.getDeactivatedAt().toString() : "",
                    user.getCreatedAt() != null ? user.getCreatedAt().toString() : "",
                    isActive ? "Active" : "Inactive");
        }
        writer.flush();
    }
}
