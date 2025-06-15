package com.example.attendance.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance.model.Attendance;
import com.example.attendance.model.Student;
import com.example.attendance.service.AttendanceService;
import com.example.attendance.service.StudentService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private AttendanceService attendanceService;
    
    /**
     * Admin dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        int totalStudents = studentService.findAllStudents().size();
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("today", LocalDate.now());
        return "admin/dashboard";
    }
    
    /**
     * Student management - List all students
     */
    @GetMapping("/students")
    public String listStudents(Model model) {
        List<Student> students = studentService.findAllStudents();
        
        // Calculate active students count
        long activeStudentsCount = getActiveStudentsCount(students);
        
        // Calculate average attendance rate
        double attendanceRate = calculateAverageAttendanceRate(students);
        
        model.addAttribute("students", students);
        model.addAttribute("activeStudentsCount", activeStudentsCount);
        model.addAttribute("attendanceRate", attendanceRate);
        
        // Add student attendance data
        Map<Long, Double> studentAttendance = new HashMap<>();
        for (Student student : students) {
            double percentage = attendanceService.calculateAttendancePercentage(student.getId());
            studentAttendance.put(student.getId(), percentage);
        }
        model.addAttribute("studentAttendance", studentAttendance);
        
        return "admin/students";
    }
    
    /**
     * Utility method to count active students
     */
    private long getActiveStudentsCount(List<Student> students) {
        return students.stream()
                .filter(student -> student.getUser() != null && student.getUser().isActive())
                .count();
    }
    
    /**
     * Utility method to calculate average attendance rate
     */
    private double calculateAverageAttendanceRate(List<Student> students) {
        if (students.isEmpty()) {
            return 0.0;
        }
        
        double totalPercentage = 0.0;
        for (Student student : students) {
            totalPercentage += attendanceService.calculateAttendancePercentage(student.getId());
        }
        
        return totalPercentage / students.size();
    }
    
    /**
     * Student management - Show add student form
     */
    @GetMapping("/students/add")
    public String addStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "admin/add-student";
    }
    
    /**
     * Student management - Save new student
     */
    @PostMapping("/students/add")
    public String saveStudent(@ModelAttribute Student student, 
                              @RequestParam String password,
                              @RequestParam String confirmPassword,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        // Validate the student data
        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty() ||
            student.getLastName() == null || student.getLastName().trim().isEmpty() ||
            student.getEmail() == null || student.getEmail().trim().isEmpty() ||
            student.getStudentId() == null || student.getStudentId().trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            
            redirectAttributes.addFlashAttribute("error", "All required fields must be filled");
            redirectAttributes.addFlashAttribute("student", student);
            return "redirect:/admin/students/add";
        }
        
        // Validate password length
        if (password.length() < 6 || password.length() > 20) {
            redirectAttributes.addFlashAttribute("error", "Password must be between 6 and 20 characters");
            redirectAttributes.addFlashAttribute("student", student);
            return "redirect:/admin/students/add";
        }
        
        // Validate that passwords match
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            redirectAttributes.addFlashAttribute("student", student);
            return "redirect:/admin/students/add";
        }
        
        // Validate email format
        if (!student.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            redirectAttributes.addFlashAttribute("error", "Invalid email format");
            redirectAttributes.addFlashAttribute("student", student);
            return "redirect:/admin/students/add";
        }
        
        // Validate student ID format (letters, numbers, and hyphens only)
        if (!student.getStudentId().matches("^[A-Za-z0-9-]+$") || 
            student.getStudentId().length() < 5 || 
            student.getStudentId().length() > 20) {
            redirectAttributes.addFlashAttribute("error", "Student ID must be 5-20 characters (letters, numbers, and hyphens only)");
            redirectAttributes.addFlashAttribute("student", student);
            return "redirect:/admin/students/add";
        }
        
        try {
            // Check for duplicate email
            if (studentService.findByEmail(student.getEmail()) != null) {
                redirectAttributes.addFlashAttribute("error", "Email already in use");
                redirectAttributes.addFlashAttribute("student", student);
                return "redirect:/admin/students/add";
            }
            
            // Check for duplicate student ID
            if (studentService.findByStudentId(student.getStudentId()) != null) {
                redirectAttributes.addFlashAttribute("error", "Student ID already in use");
                redirectAttributes.addFlashAttribute("student", student);
                return "redirect:/admin/students/add";
            }
            
            // Create the student
            studentService.createStudent(student, password);
            redirectAttributes.addFlashAttribute("success", "Student added successfully");
            return "redirect:/admin/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding student: " + e.getMessage());
            redirectAttributes.addFlashAttribute("student", student);
            return "redirect:/admin/students/add";
        }
    }
    
    /**
     * Student management - Show edit student form
     */
    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Student> student = studentService.findById(id);
        
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            return "admin/edit-student";
        } else {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/admin/students";
        }
    }
    
    /**
     * Student management - Update student
     */
    @PostMapping("/students/edit/{id}")
    public String updateStudent(@PathVariable Long id, 
                                @ModelAttribute Student student, 
                                RedirectAttributes redirectAttributes) {
        try {
            student.setId(id);
            studentService.updateStudent(student);
            redirectAttributes.addFlashAttribute("success", "Student updated successfully");
            return "redirect:/admin/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating student: " + e.getMessage());
            return "redirect:/admin/students/edit/" + id;
        }
    }
    
    /**
     * Student management - Delete student
     */
    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("success", "Student deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting student: " + e.getMessage());
        }
        return "redirect:/admin/students";
    }
    
    /**
     * Attendance management - Show mark attendance form
     */
    @GetMapping("/attendance/mark")
    public String markAttendanceForm(Model model) {
        List<Student> students = studentService.findAllStudents();
        model.addAttribute("students", students);
        model.addAttribute("today", LocalDate.now());
        return "admin/mark-attendance";
    }
    
    /**
     * Attendance management - Save attendance
     */
    @PostMapping("/attendance/mark")
    public String saveAttendance(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                 @RequestParam Map<String, String> formData,
                                 RedirectAttributes redirectAttributes) {
        try {
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                if (entry.getKey().startsWith("student_")) {
                    String studentIdStr = entry.getKey().substring("student_".length());
                    Long studentId = Long.parseLong(studentIdStr);
                    boolean present = "on".equals(entry.getValue());
                    
                    String remarksKey = "remarks_" + studentIdStr;
                    String remarks = formData.getOrDefault(remarksKey, "");
                    
                    attendanceService.markAttendance(studentId, date, present, remarks);
                }
            }
            
            redirectAttributes.addFlashAttribute("success", "Attendance marked successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error marking attendance: " + e.getMessage());
        }
        
        return "redirect:/admin/attendance/mark";
    }
    
    /**
     * Attendance management - View attendance for a specific date
     */
    @GetMapping("/attendance/view")
    public String viewAttendance(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                 Model model) {
        if (date == null) {
            date = LocalDate.now();
        }
        
        Map<Student, Boolean> attendanceReport = attendanceService.generateDailyAttendanceReport(date);
        
        // Calculate attendance statistics
        int totalStudents = attendanceReport.size();
        int presentStudents = countPresentStudents(attendanceReport);
        double attendanceRate = calculateAttendanceRate(attendanceReport);
        
        model.addAttribute("attendanceReport", attendanceReport);
        model.addAttribute("date", date);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("presentStudents", presentStudents);
        model.addAttribute("attendanceRate", attendanceRate);
        
        return "admin/view-attendance";
    }
    
    /**
     * Utility method to count present students
     */
    private int countPresentStudents(Map<Student, Boolean> attendanceReport) {
        int count = 0;
        for (Boolean present : attendanceReport.values()) {
            if (present) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Utility method to calculate attendance rate
     */
    private double calculateAttendanceRate(Map<Student, Boolean> attendanceReport) {
        if (attendanceReport.isEmpty()) {
            return 0.0;
        }
        
        return (double) countPresentStudents(attendanceReport) * 100 / attendanceReport.size();
    }
    
    /**
     * Attendance management - View student attendance
     */
    @GetMapping("/attendance/student/{id}")
    public String viewStudentAttendance(@PathVariable Long id,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {
        Optional<Student> student = studentService.findById(id);
        
        if (!student.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/admin/students";
        }
        
        // Create final variables for use in lambda
        final LocalDate effectiveStartDate = startDate != null ? startDate : LocalDate.now().withDayOfMonth(1);
        final LocalDate effectiveEndDate = endDate != null ? endDate : LocalDate.now();
        
        List<Attendance> attendanceRecords = attendanceService.getAttendanceByStudent(id)
                .stream()
                .filter(a -> !a.getDate().isBefore(effectiveStartDate) && !a.getDate().isAfter(effectiveEndDate))
                .collect(Collectors.toList());
        double attendancePercentage = attendanceService.calculateAttendancePercentage(id);
        
        model.addAttribute("student", student.get());
        model.addAttribute("attendanceRecords", attendanceRecords);
        model.addAttribute("attendancePercentage", attendancePercentage);
        model.addAttribute("startDate", effectiveStartDate);
        model.addAttribute("endDate", effectiveEndDate);
        
        return "admin/student-attendance";
    }
    
    /**
     * Attendance management - View reports
     */
    @GetMapping("/reports")
    public String viewReports(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Model model) {
        try {
            // Set default date range to current month if not specified
            LocalDate now = LocalDate.now();
            if (startDate == null) {
                startDate = now.withDayOfMonth(1); // First day of current month
            }
            if (endDate == null) {
                endDate = now; // Today
            }
            
            // Calculate days between dates
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1; // +1 to include end date
            
            List<Student> students = studentService.findAllStudents();
            Map<Student, Double> attendancePercentages = new HashMap<>();
            Map<Student, Integer> presentDays = new HashMap<>();
            Map<Student, Integer> absentDays = new HashMap<>();
            
            for (Student student : students) {
                double percentage = attendanceService.calculateAttendancePercentage(student.getId());
                attendancePercentages.put(student, percentage);
                
                // Calculate present and absent days (placeholder - real implementation would filter by date range)
                int presentCount = (int)(percentage * daysBetween / 100.0);
                presentDays.put(student, presentCount);
                absentDays.put(student, (int)daysBetween - presentCount);
            }
            
            // Calculate average attendance
            double averageAttendance = calculateAverageAttendance(attendancePercentages);
            
            model.addAttribute("students", students);
            model.addAttribute("attendancePercentages", attendancePercentages);
            model.addAttribute("averageAttendance", averageAttendance);
            model.addAttribute("daysBetween", daysBetween);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("presentDays", presentDays);
            model.addAttribute("absentDays", absentDays);
            
            return "admin/reports";
        } catch (Exception e) {
            // Log the error
            System.err.println("Error generating attendance report: " + e.getMessage());
            e.printStackTrace();
            
            // Add error message
            model.addAttribute("error", "An error occurred while generating the report. Please try again.");
            return "admin/reports";
        }
    }
    
    /**
     * Utility method to calculate average attendance from percentages map
     */
    private double calculateAverageAttendance(Map<Student, Double> attendancePercentages) {
        if (attendancePercentages == null || attendancePercentages.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (Double percentage : attendancePercentages.values()) {
            if (percentage != null) {
                sum += percentage;
            }
        }
        
        return sum / attendancePercentages.size();
    }
}

