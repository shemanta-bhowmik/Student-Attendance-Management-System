package com.example.attendance.model;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String password;
    private String role;
    private boolean active;
    
    @OneToOne(mappedBy = "user", fetch = jakarta.persistence.FetchType.LAZY)
    private Student student;
    
    // Constructors
    public User() {
    }
    
    public User(String username, String password, String role, boolean active) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.active = active;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", role=" + role + ", active=" + active + "]";
    }
}

