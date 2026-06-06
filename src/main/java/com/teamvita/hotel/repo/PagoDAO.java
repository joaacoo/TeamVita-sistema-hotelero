package com.teamvita.hotel.repo;
import java.sql.*;

public class PagoDAO {
    // Versión con medio_pago (nueva)
    public void registrarPago(int idReserva, double monto, String medioPago) {
        String sql = "INSERT INTO pagos (id_reserva, monto, medio_pago, fecha) VALUES (?, ?, ?, CURDATE())";
        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idReserva);
            stmt.setDouble(2, monto);
            stmt.setString(3, medioPago != null ? medioPago : "Efectivo");
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Versión legacy sin medio_pago (mantiene compatibilidad)
    public void registrarPago(int idReserva, double monto) {
        registrarPago(idReserva, monto, "Efectivo");
    }
}
