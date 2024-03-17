CREATE TABLE IF NOT EXISTS roles (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     role_name VARCHAR(255) NOT NULL,
                                     role_servId VARCHAR(255) NOT NULL,
                                     description TEXT NOT NULL
);
