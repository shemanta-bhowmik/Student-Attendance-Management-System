package com.example.attendance.security;

public class SecurityConstants {
    
    // Role constants
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_STUDENT = "STUDENT";
    
    // URL patterns
    public static final String ADMIN_URL = "/admin/**";
    public static final String STUDENT_URL = "/student/**";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String REGISTER_URL = "/register";
    public static final String HOME_URL = "/";
    
    // Authority prefixes
    public static final String ROLE_PREFIX = "ROLE_";
}

