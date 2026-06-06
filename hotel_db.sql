CREATE DATABASE IF NOT EXISTS hotel_db;
USE hotel_db;

CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

-- Insertar los dos usuarios que pide el cliente
INSERT IGNORE INTO usuarios (username, password) VALUES ('admin', 'admin123');
INSERT IGNORE INTO usuarios (username, password) VALUES ('recepcion', 'recepcion123');

CREATE TABLE IF NOT EXISTS huesped (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS habitacion (
    numero INT PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    precio_base DOUBLE NOT NULL,
    disponible BOOLEAN DEFAULT TRUE
);

-- Poblar habitaciones iniciales
INSERT IGNORE INTO habitacion (numero, tipo, precio_base) VALUES (101, 'Simple', 50.0);
INSERT IGNORE INTO habitacion (numero, tipo, precio_base) VALUES (102, 'Doble', 80.0);
INSERT IGNORE INTO habitacion (numero, tipo, precio_base) VALUES (201, 'Suite', 150.0);

CREATE TABLE IF NOT EXISTS reserva (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_huesped INT NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_creacion DATE NOT NULL,
    FOREIGN KEY (id_huesped) REFERENCES huesped(id)
);

CREATE TABLE IF NOT EXISTS facturas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total DOUBLE NOT NULL,
    fecha DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS pagos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    monto DOUBLE NOT NULL,
    fecha DATE NOT NULL
);
