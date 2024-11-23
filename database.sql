# 创建相应数据库，表，字段，插入数据
CREATE DATABASE IF NOT EXISTS jfinal_project CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE jfinal_project;
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    UNIQUE (username)
);
INSERT INTO user (username, password, email) VALUES
('User01', 'password01', 'user01@example.com'),
('User02', 'password02', 'user02@example.com'),
('User03', 'password03', 'user03@example.com'),
('User04', 'password04', 'user04@example.com'),
('User05', 'password05', 'user05@example.com'),
('User06', 'password06', 'user06@example.com'),
('User07', 'password07', 'user07@example.com'),
('User08', 'password08', 'user08@example.com'),
('User09', 'password09', 'user09@example.com'),
('User10', 'password10', 'user10@example.com');
CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0 NOT NULL
);
INSERT INTO product (name, price, stock) VALUES
('苹果', 3.00, 100),
('香蕉', 2.50, 200),
('西瓜', 8.00, 50),
('芒果', 9.00, 75),
('草莓', 12.00, 120),
('橙子', 4.50, 180),
('蓝莓', 15.00, 40),
('菠萝', 7.50, 60),
('葡萄', 6.00, 110),
('柠檬', 3.50, 90);
CREATE TABLE `order` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT DEFAULT 1 NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);
INSERT INTO `order` (user_id, product_id, quantity, total_price) VALUES
(1, 1, 3, 9.00),
(1, 2, 5, 12.50),
(2, 3, 2, 16.00),
(3, 4, 1, 9.00),
(4, 5, 4, 48.00),
(5, 6, 10, 45.00),
(6, 7, 2, 30.00),
(7, 8, 3, 22.50),
(8, 9, 5, 30.00),
(9, 10, 1, 3.50);
