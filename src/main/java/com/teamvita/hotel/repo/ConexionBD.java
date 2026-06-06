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
        } catch (SQLException e) {
            System.err.println(
                    "Error al conectar a MySQL. Verifique que MySQL se esté ejecutando y las credenciales sean correctas.");
            e.printStackTrace();
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
