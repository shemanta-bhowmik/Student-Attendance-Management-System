package com.example.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.model.Attendance;
import com.example.attendance.model.Student;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // All filtering methods removed to return unfiltered data
}

