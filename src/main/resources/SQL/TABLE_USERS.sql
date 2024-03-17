CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     user_id VARCHAR(255) NOT NULL,
                                     user_name VARCHAR(255) NOT NULL,
                                     joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     role_id INT,
                                     FOREIGN KEY (role_id) REFERENCES roles(id)
);