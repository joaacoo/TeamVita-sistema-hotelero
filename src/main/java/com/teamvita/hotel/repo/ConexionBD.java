package com.teamvita.hotel.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static ConexionBD instancia;
    private Connection conexion;

    // Register MySQL driver when the class is loaded
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC driver not found. Ensure mysql-connector.jar is on the classpath.");
            e.printStackTrace();
        }
    }

    private ConexionBD() {
        try {
            // Adjust URL if you use a custom port or timezone
            String url = "jdbc:mysql://localhost:3306/hotel_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            String usuario = "root"; // Change if you created another MySQL user
            String password = "";   // XAMPP default is empty; set your password if you changed it
            this.conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("✅ Connected to MySQL database 'hotel_db'");
        } catch (SQLException e) {
            System.err.println("❌ Error connecting to MySQL. Check that MySQL is running and credentials are correct.");
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
        return conexion;
    }
}


