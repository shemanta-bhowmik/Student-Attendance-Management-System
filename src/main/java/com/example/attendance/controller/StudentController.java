package com.example.attendance.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance.model.Attendance;
import com.example.attendance.model.Student;
import com.example.attendance.model.User;
import com.example.attendance.service.AttendanceService;
import com.example.attendance.service.StudentService;
import com.example.attendance.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AttendanceService attendanceService;
    
    /**
     * Helper method to get the current logged-in student
     */
    private Student getCurrentStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);
        
        if (user != null) {
            return studentService.findByEmail(username);
        }
        
        return null;
    }
    
    /**
     * Student dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Student student = getCurrentStudent();
        model.addAttribute("student", student);
        model.addAttribute("today", java.time.LocalDate.now());
        return "student/dashboard";
    }
    
    /**
     * View personal attendance
     */
    @GetMapping("/attendance")
    public String viewAttendance(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Student student = getCurrentStudent();
        
        if (student == null) {
            return "redirect:/login";
        }
        
        // Create final variables for use in lambda
        final LocalDate effectiveStartDate = startDate != null ? startDate : LocalDate.now().withDayOfMonth(1);
        final LocalDate effectiveEndDate = endDate != null ? endDate : LocalDate.now();
        
        List<Attendance> attendanceRecords = attendanceService.getAttendanceByStudent(student.getId())
                .stream()
                .filter(a -> !a.getDate().isBefore(effectiveStartDate) && !a.getDate().isAfter(effectiveEndDate))
                .collect(Collectors.toList());
        double attendancePercentage = attendanceService.calculateAttendancePercentage(student.getId());
        
        model.addAttribute("student", student);
        model.addAttribute("attendanceRecords", attendanceRecords);
        model.addAttribute("attendancePercentage", attendancePercentage);
        model.addAttribute("startDate", effectiveStartDate);
        model.addAttribute("endDate", effectiveEndDate);
        
        return "student/attendance";
    }
    
    /**
     * View profile
     */
    @GetMapping("/profile")
    public String viewProfile(Model model) {
        Student student = getCurrentStudent();
        
        if (student == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("student", student);
        return "student/profile";
    }
}

