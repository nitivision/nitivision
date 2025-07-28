package com.niti.vision.controller;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.niti.vision.dto.UserDto;
import com.niti.vision.entity.User;
import com.niti.vision.service.UserService;

import jakarta.validation.Valid;
@Controller
public class MainController {

    private UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({ "/", "/index" })
    public String home(){
        return "index";
    }
    @GetMapping({"/about" })
    public String about(){
        return "about";
    }
    @GetMapping({"/blog" })
    public String blog(){
        return "blog";
    }
    @GetMapping({"/contact" })
    public String contact(){
        return "contact";
    }
    @GetMapping({"/portfolio" })
    public String portfolio(){
        return "portfolio";
    }
    @GetMapping({"/services" })
    public String services(){
        return "services";
    }
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // handler method to handle user registration request
    @GetMapping("register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model){
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String listRegisteredUsers(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
}
