package com.example.Student.Attendance.Management.System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.Student.Attendance.Management.System", "com.example.attendance"})
@EntityScan(basePackages = {"com.example.attendance.model"})
@EnableJpaRepositories(basePackages = {"com.example.attendance.repository"})
public class StudentAttendanceManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentAttendanceManagementSystemApplication.class, args);
	}

}
