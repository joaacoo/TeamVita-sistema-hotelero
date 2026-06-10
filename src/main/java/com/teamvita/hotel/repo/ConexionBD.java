package com.teamvita.hotel.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static ConexionBD instancia;
    private Connection conexion;

    // Registrar el driver MySQL cuando se carga la clase
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver JDBC MySQL cargado correctamente");
        } catch (ClassNotFoundException e) {
            System.err.println(
                    "Driver JDBC MySQL no encontrado. Asegúrese de que mysql-connector.jar esté en el classpath.");
            e.printStackTrace();
        }
    }

    private ConexionBD() {
        try {
            // Ajustar URL si usa un puerto o zona horaria personalizada
            String url = "jdbc:mysql://localhost:3306/hotel_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            String usuario = "root"; // Cambiar si creó otro usuario MySQL
            String password = ""; // Por defecto en XAMPP está vacío; establezca la contraseña si la cambió
            this.conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("Conectado a la base de datos MySQL 'hotel_db'");
            aplicarMigracionesFase3();
        } catch (SQLException e) {
            System.err.println(
                    "Error al conectar a MySQL. Verifique que MySQL se esté ejecutando y las credenciales sean correctas.");
            e.printStackTrace();
        }
    }

    private void aplicarMigracionesFase3() {
        try {
            // Migración para 'servicio'
            try {
                java.sql.Statement st = conexion.createStatement();
                st.execute("ALTER TABLE servicio ADD COLUMN categoria VARCHAR(50) DEFAULT 'General'");
                st.close();
            } catch (SQLException ignore) { /* Ya existe la columna */ }
            
            // Migración para 'reserva' - cantidad de personas
            try {
                java.sql.Statement st = conexion.createStatement();
                st.execute("ALTER TABLE reserva ADD COLUMN cantidad_personas INT DEFAULT 1");
                st.close();
            } catch (SQLException ignore) { /* Ya existe la columna */ }

            // Insertar datos que el usuario no tiene porque la tabla ya existía
            String inserts = "INSERT IGNORE INTO servicio (nombre, precio, categoria) VALUES " +
                "('Lavandería', 15.0, 'General'), " +
                "('Menú Ejecutivo', 30.0, 'Restaurante'), " +
                "('Cena Romántica', 80.0, 'Restaurante'), " +
                "('Menú Infantil', 15.0, 'Restaurante'), " +
                "('Masaje Descontracturante', 50.0, 'Spa'), " +
                "('Masaje Relajante', 40.0, 'Spa'), " +
                "('Sauna', 25.0, 'Spa')";
            java.sql.Statement stInsert = conexion.createStatement();
            stInsert.execute(inserts);
            stInsert.close();

            // Asegurarnos de que acompanante existe
            String createAcompanante = "CREATE TABLE IF NOT EXISTS acompanante (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "id_reserva INT NOT NULL, " +
                "nombre VARCHAR(100) NOT NULL, " +
                "dni VARCHAR(10) NOT NULL, " +
                "FOREIGN KEY (id_reserva) REFERENCES reserva(id) ON DELETE CASCADE)";
            java.sql.Statement stAcom = conexion.createStatement();
            stAcom.execute(createAcompanante);
            stAcom.close();
            System.out.println("[ConexionBD] Migraciones de Fase 3 aplicadas correctamente.");
        } catch (Exception e) {
            System.err.println("Error al aplicar migraciones automáticas: " + e.getMessage());
        }
    }

    public static ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public Connection getConexion() {
        try {
            // Si la conexión es nula o está cerrada, reconectar
            if (conexion == null || conexion.isClosed()) {
                System.out.println("Conexión cerrada o nula. Reconectando...");
                String url = "jdbc:mysql://localhost:3306/hotel_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                String usuario = "root";
                String password = "";
                conexion = DriverManager.getConnection(url, usuario, password);
                System.out.println("Reconexión exitosa a MySQL.");
            }
        } catch (SQLException e) {
            System.err.println("Error al reconectar: " + e.getMessage());
            e.printStackTrace();
        }
        return conexion;
    }
}
