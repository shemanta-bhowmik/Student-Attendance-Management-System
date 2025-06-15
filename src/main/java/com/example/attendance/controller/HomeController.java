package com.example.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance.model.Student;
import com.example.attendance.model.User;
import com.example.attendance.security.SecurityConstants;
import com.example.attendance.service.StudentService;
import com.example.attendance.service.UserService;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private StudentService studentService;
    
    /**
     * Root URL mapping
     */
    @GetMapping("/")
    public String home() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // If user is authenticated, redirect to appropriate dashboard
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            String username = auth.getName();
            User user = userService.findByUsername(username);

            if (user != null) {
                if (SecurityConstants.ROLE_ADMIN.equals(user.getRole())) {
                    return "redirect:/admin/dashboard";
                } else if (SecurityConstants.ROLE_STUDENT.equals(user.getRole())) {
                    return "redirect:/student/dashboard";
                }
            }
        }

        // Otherwise, redirect to login page
        return "redirect:/login";
    }
    
    /**
     * Login page mapping
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    /**
     * Registration page mapping
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }
    
    /**
     * Handle student registration
     */
    @PostMapping("/register")
    public String registerStudent(@ModelAttribute("student") Student student, String password, RedirectAttributes redirectAttributes) {
        try {
            // Check if username already exists
            if (userService.findByUsername(student.getEmail()) != null) {
                redirectAttributes.addFlashAttribute("error", "Email already exists");
                return "redirect:/register";
            }
            
            // Create student with user account
            studentService.createStudent(student, password);
            redirectAttributes.addFlashAttribute("success", "Registration successful. Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/register";
        }
    }
    
    /**
     * Dashboard routing
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);
        
        if (user != null) {
            if (SecurityConstants.ROLE_ADMIN.equals(user.getRole())) {
                return "redirect:/admin/dashboard";
            } else if (SecurityConstants.ROLE_STUDENT.equals(user.getRole())) {
                return "redirect:/student/dashboard";
            }
        }
        
        return "redirect:/login";
    }
}

