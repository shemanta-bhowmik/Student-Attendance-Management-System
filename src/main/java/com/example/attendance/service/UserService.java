package com.example.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.attendance.model.User;
import com.example.attendance.repository.UserRepository;
import com.example.attendance.security.SecurityConstants;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Create a new admin user
     */
    public User createAdminUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(SecurityConstants.ROLE_ADMIN);
        user.setActive(true);
        return userRepository.save(user);
    }
    
    /**
     * Create a new student user
     */
    public User createStudentUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(SecurityConstants.ROLE_STUDENT);
        user.setActive(true);
        return userRepository.save(user);
    }
    
    /**
     * Find user by username
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Find user by ID
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Get all users
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Update user (excluding password)
     */
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId()));
        
        existingUser.setUsername(user.getUsername());
        existingUser.setRole(user.getRole());
        existingUser.setActive(user.isActive());
        
        return userRepository.save(existingUser);
    }
    
    /**
     * Update user password
     */
    public User updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
    
    /**
     * Delete user
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

