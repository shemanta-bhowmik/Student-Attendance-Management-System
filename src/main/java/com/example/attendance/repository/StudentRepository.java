package com.example.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import com.example.attendance.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Student findByEmail(String email);
    
    Student findByStudentId(String studentId);
    
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.user WHERE s.id = :id")
    Optional<Student> findByIdWithUser(@Param("id") Long id);
    
    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.user")
    List<Student> findAllWithUsers();
}

