package com.example.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.model.Student;
import com.example.attendance.model.User;
import com.example.attendance.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private UserService userService;
    
    /**
     * Create a new student with user account
     */
    @Transactional
    public Student createStudent(Student student, String password) {
        // Create user account for the student
        User user = userService.createStudentUser(student.getEmail(), password);
        student.setUser(user);
        
        return studentRepository.save(student);
    }
    
    /**
     * Get student by ID
     */
    public Optional<Student> findById(Long id) {
        return studentRepository.findByIdWithUser(id);
    }
    
    /**
     * Get student by email
     */
    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    
    /**
     * Get student by student ID
     */
    public Student findByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }
    
    /**
     * Get all students
     */
    public List<Student> findAllStudents() {
        return studentRepository.findAllWithUsers();
    }
    
    /**
     * Update student information
     */
    @Transactional
    public Student updateStudent(Student student) {
        Student existingStudent = studentRepository.findByIdWithUser(student.getId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + student.getId()));
        
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setStudentId(student.getStudentId());
        
        // Update email and username together if changed
        if (!existingStudent.getEmail().equals(student.getEmail())) {
            existingStudent.setEmail(student.getEmail());
            existingStudent.getUser().setUsername(student.getEmail());
        }
        
        return studentRepository.save(existingStudent);
    }
    
    /**
     * Delete student and associated user account
     */
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findByIdWithUser(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        
        // Store user ID for later deletion
        Long userId = student.getUser() != null ? student.getUser().getId() : null;
        
        // Delete student record
        studentRepository.delete(student);
        
        // Delete associated user account if exists
        if (userId != null) {
            userService.deleteUser(userId);
        }
    }
}

