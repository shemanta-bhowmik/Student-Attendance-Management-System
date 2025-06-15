package com.example.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.model.Attendance;
import com.example.attendance.model.Student;
import com.example.attendance.repository.AttendanceRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private StudentService studentService;
    
    /**
     * Mark attendance for a student
     */
    @Transactional
    public Attendance markAttendance(Long studentId, LocalDate date, boolean present, String remarks) {
        Student student = studentService.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        // No filtering - just create a new attendance record
        Attendance attendance = new Attendance(student, date, present, remarks);
        return attendanceRepository.save(attendance);
    }
    
    /**
     * Get attendance by ID
     */
    public Optional<Attendance> findById(Long id) {
        return attendanceRepository.findById(id);
    }
    
    /**
     * Get all attendance records for a student
     */
    public List<Attendance> getAttendanceByStudent(Long studentId) {
        Student student = studentService.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        return attendanceRepository.findAll().stream()
                .filter(a -> a.getStudent().getId().equals(studentId))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all attendance records (no filtering)
     */
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findAll();
    }
    
    /**
     * Calculate attendance percentage for a student
     */
    public double calculateAttendancePercentage(Long studentId) {
        List<Attendance> attendanceRecords = getAttendanceByStudent(studentId);
        
        if (attendanceRecords.isEmpty()) {
            return 0.0;
        }
        
        long presentCount = attendanceRecords.stream()
                .filter(Attendance::isPresent)
                .count();
        
        return (double) presentCount / attendanceRecords.size() * 100;
    }
    
    
    /**
     * Generate attendance report for all students on a specific date
     */
    public Map<Student, Boolean> generateDailyAttendanceReport(LocalDate date) {
        List<Attendance> attendanceRecords = attendanceRepository.findAll();
        Map<Student, Boolean> report = new HashMap<>();
        
        // Add all students to the report
        List<Student> allStudents = studentService.findAllStudents();
        allStudents.forEach(student -> report.put(student, false));
        
        // Update the report with actual attendance
        attendanceRecords.forEach(attendance -> 
            report.put(attendance.getStudent(), attendance.isPresent())
        );
        
        return report;
    }
    
    /**
     * Delete attendance record
     */
    @Transactional
    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }
}

