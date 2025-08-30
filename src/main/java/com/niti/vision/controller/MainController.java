package com.niti.vision.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niti.vision.dto.UserDto;
import com.niti.vision.entity.Candidate;
import com.niti.vision.entity.ContactMessage;
import com.niti.vision.entity.Job;
import com.niti.vision.entity.User;
import com.niti.vision.repository.CandidateRepository;
import com.niti.vision.repository.ContactMessageRepository;
import com.niti.vision.repository.JobRepository;
import com.niti.vision.service.UserService;

import jakarta.validation.Valid;

@Controller
@CrossOrigin(origins = "*")
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	private ContactMessageRepository repository;
	private UserService userService;
	private JobRepository jobRepo;
	private CandidateRepository candidateRepo;
	//Dev
	private static final String SECRET_KEY = "6LcGzq0rAAAAAKKnrXW4yA_IhteVWsuvNaLIAGE6";
    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    //Prod
    //private static final String SECRET_KEY = "6LcnBLgrAAAAAE_QLlm1EcW6G12DDmaOvSIZ4vR5";
    //private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
	@Autowired
	public MainController(UserService userService, ContactMessageRepository repository, JobRepository jobRepo,
			CandidateRepository candidateRepo) {
		this.userService = userService;
		this.repository = repository;
		this.jobRepo = jobRepo;
		this.candidateRepo = candidateRepo;
	}

	@PostMapping("/contact/save")
	public String saveContact(@ModelAttribute ContactMessage message, RedirectAttributes redirectAttributes) {
		logger.info("Start saveContact Method");
		repository.save(message);
		redirectAttributes.addFlashAttribute("successMessage",
				"Form submitted successfully. We’ll get back to you as soon as possible!");
		logger.info("End saveContact Method");
		return "redirect:/contact"; // redirect to list page
	}

	/*
	 * // Get all messages
	 * 
	 * @GetMapping("/admin/contactList") public String listContacts(Model model) {
	 * model.addAttribute("contacts", repository.findAll()); return "contactList"; }
	 */

	@GetMapping({ "/", "/index" })
	public String home() {
		logger.info("Home page");
		return "index";
	}

	@GetMapping({ "/about" })
	public String about() {
		logger.info("About Us page");
		return "about";
	}

	@GetMapping({ "/blog" })
	public String blog() {
		logger.info("Blog page");
		return "blog";
	}

	@GetMapping({ "/contact" })
	public String contact() {
		logger.info("Contact Us page");
		return "contact";
	}

	@GetMapping({ "/portfolio" })
	public String portfolio() {
		logger.info("Portfolio page");
		return "portfolio";
	}

	@GetMapping({ "/services" })
	public String services() {
		logger.info("Service page");
		return "services";
	}

	@GetMapping({ "/consulting" })
	public String consulting() {
		logger.info("Consulting page");
		return "consulting";
	}

	@GetMapping({ "/manpower" })
	public String manpower() {
		logger.info("Manpower page");
		return "manpower";
	}

	@GetMapping({ "/hrsolution" })
	public String hrsolution() {
		logger.info("HR Solution page");
		return "hrsolution";
	}

	@GetMapping({ "/career" })
	public String career(Model model) {
		logger.info("Career page");
		model.addAttribute("jobs", jobRepo.findAll());
		return "career";
	}

	@GetMapping("/apply/{id}")
	public String apply(@PathVariable Long id, Model model) {
		Job job = jobRepo.findById(id).orElseThrow(() -> new RuntimeException("Job not found with ID " + id));
		model.addAttribute("job", job);
		model.addAttribute("candidate", new Candidate());
		return "applynow";
	}

	@PostMapping("/apply/save")
	public ResponseEntity<String> saveApplication(@ModelAttribute Candidate candidate, @RequestParam("jobId") Long jobId,
			@RequestParam("resume") MultipartFile file, RedirectAttributes ra) throws IOException {
		// Fetch job from DB (ensure it's managed)
		System.out.println("jobId : " + jobId);
		Job job = jobRepo.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found with ID " + jobId));

		candidate.setJob(job);
		// Save resume file
		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		Path path = Paths.get("uploads").resolve(fileName);
		Files.createDirectories(path.getParent());
		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

		candidate.setResumePath(fileName);

		candidateRepo.save(candidate);

		 return ResponseEntity.ok("Application Submitted Successfully!");
	}

	@GetMapping("/web-development")
	public String webDevelopmentPage() {
		return "web-development"; // corresponds to web-development.html in templates/
	}

	@GetMapping("/login")
	public String loginForm() {
		return "login";
	}

	// handler method to handle user registration request
	@GetMapping("register")
	public String showRegistrationForm(Model model) {
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		return "register";
	}

	// handler method to handle register user form submit request
	@PostMapping("/register/save")
	public String registration(@Valid @ModelAttribute("user") UserDto user, BindingResult result, Model model) {
		User existing = userService.findByEmail(user.getEmail());
		if (existing != null) {
			result.rejectValue("email", null, "There is already an account registered with that email");
		}
		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "register";
		}
		System.out.println("USER from Main Controller : "+user.toString());
		userService.saveUser(user);
		return "redirect:/register?success";
	}

	/*
	 * @GetMapping("/admin/dashboard") public String listRegisteredUsers(Model
	 * model) { // model.addAttribute("title", "Admin Dashboard - Niti Vision");
	 * List<UserDto> users = userService.findAllUsers(); model.addAttribute("users",
	 * users); return "dashboard"; }
	 * 
	 * @GetMapping("/admin/users") public String listUsers(Model model) { //
	 * model.addAttribute("title", "Admin Dashboard - Niti Vision"); List<UserDto>
	 * users = userService.findAllUsers(); model.addAttribute("users", users);
	 * return "users"; }
	 */

	@GetMapping("/forgot-password")
	public String forgotPassword() {
		return "forgot-password";
	}

	/*
	 * @PostMapping("/send-otp") public ResponseEntity<Map<String, String>>
	 * sendOtp(@RequestBody Map<String, String> request) { String email =
	 * request.get("email"); userService.sendOtpToEmail(email);
	 * 
	 * Map<String, String> response = new HashMap<>(); response.put("message",
	 * "OTP sent to your email."); return ResponseEntity.ok(response); }
	 */

	@PostMapping("/reset-password")
	public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
	    String email = request.get("email");
	    String otp = request.get("otp");
	    String newPassword = request.get("newPassword");

	    Map<String, String> response = new HashMap<>();
	    boolean result = userService.verifyOtpAndResetPassword(email, otp, newPassword);

	    if (result) {
	        response.put("message", "Password reset successful.");
	        return ResponseEntity.ok(response);
	    } else {
	        response.put("message", "Invalid OTP or Email.");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	}

	@GetMapping("/job/{id}")
	public String jobDetail(@PathVariable Long id, Model model) {
		Job job = jobRepo.findById(id).orElseThrow(() -> new RuntimeException("Job not found with ID " + id));
		model.addAttribute("job", job);
		return "job"; // job.html
	}
	
	@PostMapping("/savepopup")
	public ResponseEntity<Map<String, String>> submitMessage(@RequestBody ContactMessage message) {
	    System.out.println("Received message: " + message); // debug
	    repository.save(message);

	    Map<String, String> response = new HashMap<>();
	    response.put("message", "Your message has been submitted successfully.");
	    return ResponseEntity.ok(response);
	}
	@GetMapping("/popup")
	public String popupPage() {
	    return "popupForm"; // popupForm.html under /templates
	}
	

    @PostMapping("/verifyCaptcha")
    public String verifyCaptcha(@RequestParam("g-recaptcha-response") String captchaResponse) {
        RestTemplate restTemplate = new RestTemplate();
        String url = VERIFY_URL + "?secret=" + SECRET_KEY + "&response=" + captchaResponse;

        Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);

        if ((Boolean) response.get("success")) {
            return "Captcha Verified!";
        } else {
            return "Captcha Failed!";
        }
    }
    
    @PostMapping("/send-otp")
    @ResponseBody
    public ResponseEntity<Map<String, String>> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, String> response = new HashMap<>();

        String result = userService.sendOtpForPasswordReset(email);

        switch (result) {
            case "OTP_SENT":
                response.put("message", "OTP sent to your email.");
                return ResponseEntity.ok(response);
            case "INACTIVE":
                response.put("message", "Your account is not activated. Please wait for admin approval.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            case "NOT_FOUND":
            default:
                response.put("message", "User not found with this email.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @GetMapping("/internship")
	public String Internship() {
	    return "internship";
	} 

}
