-- Insert admin user (password: "password" BCrypt encoded)
INSERT IGNORE INTO users (username, password, role, active)
VALUES ('admin@example.com', '$2a$10$yRxRYK/s8vVKYYqGVg2O4eqZvX0CnUUlzZMQwQJnYPZrzAkGo4/ja', 'ADMIN', true);

-- Insert sample students with user accounts (password: "password" BCrypt encoded)
-- Student 1
INSERT IGNORE INTO users (username, password, role, active)
VALUES ('john.doe@example.com', '$2a$10$yRxRYK/s8vVKYYqGVg2O4eqZvX0CnUUlzZMQwQJnYPZrzAkGo4/ja', 'STUDENT', true);
INSERT IGNORE INTO students (first_name, last_name, email, student_id, user_id)
VALUES ('John', 'Doe', 'john.doe@example.com', 'S2025001', (SELECT id FROM users WHERE username = 'john.doe@example.com'));

-- Student 2
INSERT IGNORE INTO users (username, password, role, active)
VALUES ('jane.smith@example.com', '$2a$10$yRxRYK/s8vVKYYqGVg2O4eqZvX0CnUUlzZMQwQJnYPZrzAkGo4/ja', 'STUDENT', true);
INSERT IGNORE INTO students (first_name, last_name, email, student_id, user_id)
VALUES ('Jane', 'Smith', 'jane.smith@example.com', 'S2025002', (SELECT id FROM users WHERE username = 'jane.smith@example.com'));

-- Student 3
INSERT IGNORE INTO users (username, password, role, active)
VALUES ('mike.wilson@example.com', '$2a$10$yRxRYK/s8vVKYYqGVg2O4eqZvX0CnUUlzZMQwQJnYPZrzAkGo4/ja', 'STUDENT', true);
INSERT IGNORE INTO students (first_name, last_name, email, student_id, user_id)
VALUES ('Mike', 'Wilson', 'mike.wilson@example.com', 'S2025003', (SELECT id FROM users WHERE username = 'mike.wilson@example.com'));

-- Student 4
INSERT IGNORE INTO users (username, password, role, active)
VALUES ('sarah.johnson@example.com', '$2a$10$yRxRYK/s8vVKYYqGVg2O4eqZvX0CnUUlzZMQwQJnYPZrzAkGo4/ja', 'STUDENT', true);
INSERT IGNORE INTO students (first_name, last_name, email, student_id, user_id)
VALUES ('Sarah', 'Johnson', 'sarah.johnson@example.com', 'S2025004', (SELECT id FROM users WHERE username = 'sarah.johnson@example.com'));

-- Student 5
INSERT IGNORE INTO users (username, password, role, active)
VALUES ('alex.brown@example.com', '$2a$10$yRxRYK/s8vVKYYqGVg2O4eqZvX0CnUUlzZMQwQJnYPZrzAkGo4/ja', 'STUDENT', true);
INSERT IGNORE INTO students (first_name, last_name, email, student_id, user_id)
VALUES ('Alex', 'Brown', 'alex.brown@example.com', 'S2025005', (SELECT id FROM users WHERE username = 'alex.brown@example.com'));

-- Insert sample attendance records for the past week
-- Student 1 (John Doe) - Present all days
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'john.doe@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'john.doe@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'john.doe@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'john.doe@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'john.doe@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'john.doe@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'john.doe@example.com'),
    CURRENT_DATE(),
    true,
    'On time'
);

-- Student 2 (Jane Smith) - Absent one day
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'jane.smith@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'jane.smith@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'jane.smith@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY),
    false,
    'Sick leave'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'jane.smith@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'jane.smith@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'jane.smith@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'jane.smith@example.com'),
    CURRENT_DATE(),
    true,
    'On time'
);

-- Student 3 (Mike Wilson) - Absent two days
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'mike.wilson@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'mike.wilson@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY),
    false,
    'Personal leave'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'mike.wilson@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'mike.wilson@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'mike.wilson@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY),
    false,
    'Family emergency'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'mike.wilson@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'mike.wilson@example.com'),
    CURRENT_DATE(),
    true,
    'On time'
);

-- Student 4 (Sarah Johnson) - Late one day, absent one day
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'sarah.johnson@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'sarah.johnson@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY),
    true,
    'Late by 10 minutes'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'sarah.johnson@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'sarah.johnson@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY),
    false,
    'Sick leave'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'sarah.johnson@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'sarah.johnson@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'sarah.johnson@example.com'),
    CURRENT_DATE(),
    true,
    'On time'
);

-- Student 5 (Alex Brown) - Absent three days
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'alex.brown@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY),
    false,
    'Medical leave'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'alex.brown@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY),
    false,
    'Medical leave'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'alex.brown@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY),
    false,
    'Medical leave'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'alex.brown@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'alex.brown@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'alex.brown@example.com'),
    DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY),
    true,
    'On time'
);
INSERT INTO attendance (student_id, date, present, remarks) 
VALUES (
    (SELECT id FROM students WHERE email = 'alex.brown@example.com'),
    CURRENT_DATE(),
    true,
    'On time'
);

