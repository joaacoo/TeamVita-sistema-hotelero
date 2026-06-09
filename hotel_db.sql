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
    telefono VARCHAR(20) NOT NULL,
    categoria_fidelizacion VARCHAR(50) DEFAULT 'Clásica'
);

CREATE TABLE IF NOT EXISTS habitacion (
    numero INT PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    precio_base DOUBLE NOT NULL,
    capacidad INT NOT NULL DEFAULT 2,
    disponible BOOLEAN DEFAULT TRUE
);

-- Poblar habitaciones iniciales
INSERT IGNORE INTO habitacion (numero, tipo, precio_base, capacidad) VALUES (101, 'Simple', 50.0, 1);
INSERT IGNORE INTO habitacion (numero, tipo, precio_base, capacidad) VALUES (102, 'Doble', 80.0, 2);
INSERT IGNORE INTO habitacion (numero, tipo, precio_base, capacidad) VALUES (201, 'Suite', 150.0, 6);

CREATE TABLE IF NOT EXISTS reserva (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_huesped INT NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_creacion DATE NOT NULL,
    fecha_checkin DATE NULL,
    fecha_checkout DATE NULL,
    total_estimado DOUBLE NOT NULL DEFAULT 0.0,
    FOREIGN KEY (id_huesped) REFERENCES huesped(id)
);

CREATE TABLE IF NOT EXISTS detalle_reserva (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NOT NULL,
    numero_habitacion INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    FOREIGN KEY (id_reserva) REFERENCES reserva(id),
    FOREIGN KEY (numero_habitacion) REFERENCES habitacion(numero)
);

CREATE TABLE IF NOT EXISTS facturas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NOT NULL,
    total DOUBLE NOT NULL,
    medio_pago VARCHAR(50) DEFAULT 'Efectivo',
    fecha DATE NOT NULL,
    FOREIGN KEY (id_reserva) REFERENCES reserva(id)
);

CREATE TABLE IF NOT EXISTS pagos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NOT NULL,
    monto DOUBLE NOT NULL,
    medio_pago VARCHAR(50) DEFAULT 'Efectivo',
    fecha DATE NOT NULL,
    FOREIGN KEY (id_reserva) REFERENCES reserva(id)
);

CREATE TABLE IF NOT EXISTS configuracion (
    clave VARCHAR(50) PRIMARY KEY,
    valor VARCHAR(100) NOT NULL
);
INSERT IGNORE INTO configuracion (clave, valor) VALUES ('senia_porcentaje', '30');
INSERT IGNORE INTO configuracion (clave, valor) VALUES ('tarifa_Simple', '50.0');
INSERT IGNORE INTO configuracion (clave, valor) VALUES ('tarifa_Doble', '80.0');
INSERT IGNORE INTO configuracion (clave, valor) VALUES ('tarifa_Suite', '150.0');
INSERT IGNORE INTO configuracion (clave, valor) VALUES ('hotel_nombre', 'TeamVita Hotel');
INSERT IGNORE INTO configuracion (clave, valor) VALUES ('hotel_direccion', 'Av. Alvear 1000');

CREATE TABLE IF NOT EXISTS servicio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    precio DOUBLE NOT NULL
);

INSERT IGNORE INTO servicio (nombre, precio) VALUES ('Lavanderia', 15.0);
INSERT IGNORE INTO servicio (nombre, precio) VALUES ('Restaurante', 30.0);
INSERT IGNORE INTO servicio (nombre, precio) VALUES ('Spa', 50.0);

CREATE TABLE IF NOT EXISTS promocion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descuento_porcentaje DOUBLE NOT NULL
);

INSERT IGNORE INTO promocion (nombre, descuento_porcentaje) VALUES ('Sin Promoción', 0.0);
INSERT IGNORE INTO promocion (nombre, descuento_porcentaje) VALUES ('Promoción 10%', 10.0);
INSERT IGNORE INTO promocion (nombre, descuento_porcentaje) VALUES ('Promoción 20%', 20.0);

CREATE TABLE IF NOT EXISTS consumo_servicio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NOT NULL,
    id_servicio INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    fecha DATE NOT NULL,
    FOREIGN KEY (id_reserva) REFERENCES reserva(id),
    FOREIGN KEY (id_servicio) REFERENCES servicio(id)
);
