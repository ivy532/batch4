-- =====================================================
-- RETAIL ORDERING SYSTEM - MySQL Database Schema
-- =====================================================

CREATE DATABASE IF NOT EXISTS retail_ordering;
USE retail_ordering;

-- =====================================================
-- USERS TABLE
-- =====================================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    loyalty_points INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email)
);

-- =====================================================
-- BRANDS TABLE
-- =====================================================
CREATE TABLE brands (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    logo_url VARCHAR(255),
    active BOOLEAN DEFAULT TRUE
);

-- =====================================================
-- CATEGORIES TABLE
-- =====================================================
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(255)
);

-- =====================================================
-- PRODUCTS TABLE
-- =====================================================
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    brand_id BIGINT,
    category_id BIGINT,
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (brand_id) REFERENCES brands(id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    INDEX idx_category (category_id),
    INDEX idx_brand (brand_id)
);

-- =====================================================
-- PACKAGING TABLE (USP - Multiple pricing per product)
-- =====================================================
CREATE TABLE packaging (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    size VARCHAR(50) NOT NULL,        -- SMALL, MEDIUM, LARGE
    type VARCHAR(50) NOT NULL,        -- BOX, BOTTLE, PACK, CAN
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    description VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_product (product_id)
);

-- =====================================================
-- CART TABLE
-- =====================================================
CREATE TABLE cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =====================================================
-- CART_ITEMS TABLE
-- =====================================================
CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    packaging_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE,
    FOREIGN KEY (packaging_id) REFERENCES packaging(id),
    UNIQUE KEY unique_cart_packaging (cart_id, packaging_id)
);

-- =====================================================
-- ORDERS TABLE
-- =====================================================
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING','CONFIRMED','PREPARING','OUT_FOR_DELIVERY','DELIVERED','CANCELLED') DEFAULT 'PENDING',
    delivery_address TEXT,
    coupon_code VARCHAR(50),
    discount_amount DECIMAL(10, 2) DEFAULT 0.00,
    loyalty_points_used INT DEFAULT 0,
    loyalty_points_earned INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user (user_id),
    INDEX idx_status (status)
);

-- =====================================================
-- ORDER_ITEMS TABLE
-- =====================================================
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    packaging_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price_at_order DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (packaging_id) REFERENCES packaging(id)
);

-- =====================================================
-- COUPONS TABLE
-- =====================================================
CREATE TABLE coupons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    discount_type ENUM('PERCENTAGE', 'FLAT') NOT NULL,
    discount_value DECIMAL(10, 2) NOT NULL,
    min_order_amount DECIMAL(10, 2) DEFAULT 0.00,
    expiry_date DATE,
    usage_limit INT,
    used_count INT DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    INDEX idx_code (code)
);

-- =====================================================
-- SEED DATA
-- =====================================================

-- Admin User (password: admin123)
INSERT INTO users (name, email, password, role) VALUES
('Admin', 'admin@retail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'ADMIN');

-- Brands
INSERT INTO brands (name, description) VALUES
('Dominos', 'Premium Pizza Brand'),
('Pepsi Co', 'Cold Drinks Leader'),
('Britannia', 'Trusted Breads & Bakery'),
('Pizza Hut', 'Stuffed Crust Specialists'),
('Coca Cola', 'World Famous Beverages');

-- Categories
INSERT INTO categories (name, description) VALUES
('Pizza', 'Hot and Fresh Pizzas'),
('Cold Drinks', 'Refreshing Beverages'),
('Breads', 'Fresh Baked Breads');

-- Products
INSERT INTO products (name, description, image_url, brand_id, category_id) VALUES
('Margherita Pizza', 'Classic tomato and mozzarella', '/images/margherita.jpg', 1, 1),
('Pepperoni Pizza', 'Loaded with pepperoni slices', '/images/pepperoni.jpg', 1, 1),
('Stuffed Crust Pizza', 'Cheese-stuffed golden crust', '/images/stuffed.jpg', 4, 1),
('Pepsi', 'Refreshing cola drink', '/images/pepsi.jpg', 2, 2),
('Mountain Dew', 'Citrus energy drink', '/images/dew.jpg', 2, 2),
('Coca Cola', 'Classic cola since 1886', '/images/coke.jpg', 5, 2),
('Whole Wheat Bread', 'Healthy whole grain bread', '/images/wheat.jpg', 3, 3),
('Multigrain Bread', '7-grain nutritious bread', '/images/multigrain.jpg', 3, 3);

-- Packaging Options (USP)
INSERT INTO packaging (product_id, size, type, price, stock_quantity, description) VALUES
-- Margherita Pizza
(1, 'SMALL', 'BOX', 149.00, 50, '6-inch small pizza'),
(1, 'MEDIUM', 'BOX', 249.00, 40, '9-inch medium pizza'),
(1, 'LARGE', 'BOX', 399.00, 30, '12-inch large pizza'),
-- Pepperoni Pizza
(2, 'SMALL', 'BOX', 199.00, 50, '6-inch small pizza'),
(2, 'MEDIUM', 'BOX', 329.00, 40, '9-inch medium pizza'),
(2, 'LARGE', 'BOX', 499.00, 30, '12-inch large pizza'),
-- Stuffed Crust
(3, 'MEDIUM', 'BOX', 379.00, 30, '9-inch stuffed crust'),
(3, 'LARGE', 'BOX', 579.00, 20, '12-inch stuffed crust'),
-- Pepsi
(4, 'SMALL', 'CAN', 40.00, 200, '250ml can'),
(4, 'MEDIUM', 'BOTTLE', 60.00, 150, '500ml bottle'),
(4, 'LARGE', 'BOTTLE', 90.00, 100, '1.5L bottle'),
-- Mountain Dew
(5, 'SMALL', 'CAN', 40.00, 200, '250ml can'),
(5, 'MEDIUM', 'BOTTLE', 60.00, 150, '500ml bottle'),
(5, 'LARGE', 'BOTTLE', 90.00, 100, '1.5L bottle'),
-- Coca Cola
(6, 'SMALL', 'CAN', 40.00, 200, '250ml can'),
(6, 'MEDIUM', 'BOTTLE', 60.00, 150, '500ml bottle'),
(6, 'LARGE', 'BOTTLE', 90.00, 100, '1.5L bottle'),
-- Whole Wheat Bread
(7, 'SMALL', 'PACK', 35.00, 100, '200g pack'),
(7, 'LARGE', 'PACK', 65.00, 80, '400g pack'),
-- Multigrain Bread
(8, 'SMALL', 'PACK', 45.00, 100, '200g pack'),
(8, 'LARGE', 'PACK', 85.00, 80, '400g pack');

-- Coupons
INSERT INTO coupons (code, discount_type, discount_value, min_order_amount, expiry_date, usage_limit) VALUES
('WELCOME10', 'PERCENTAGE', 10.00, 100.00, '2025-12-31', 1000),
('FLAT50', 'FLAT', 50.00, 300.00, '2025-12-31', 500),
('PIZZA20', 'PERCENTAGE', 20.00, 200.00, '2025-12-31', 300),
('LOYALTY100', 'FLAT', 100.00, 500.00, '2025-12-31', 200);

-- =====================================================
-- USEFUL VIEWS
-- =====================================================

CREATE VIEW order_summary AS
SELECT
    o.id AS order_id,
    u.name AS customer_name,
    u.email,
    o.total_amount,
    o.status,
    o.created_at,
    COUNT(oi.id) AS item_count
FROM orders o
JOIN users u ON o.user_id = u.id
JOIN order_items oi ON o.id = oi.order_id
GROUP BY o.id, u.name, u.email, o.total_amount, o.status, o.created_at;

CREATE VIEW product_inventory AS
SELECT
    p.name AS product_name,
    c.name AS category,
    b.name AS brand,
    pkg.size,
    pkg.type,
    pkg.price,
    pkg.stock_quantity
FROM products p
JOIN categories c ON p.category_id = c.id
JOIN brands b ON p.brand_id = b.id
JOIN packaging pkg ON p.id = pkg.product_id
ORDER BY p.name, pkg.price;