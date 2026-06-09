package com.teamvita.hotel.repo;
import com.teamvita.hotel.model.*;
import com.teamvita.hotel.model.habitacion.*;
import com.teamvita.hotel.model.reserva.*;
import com.teamvita.hotel.model.servicio.*;
import com.teamvita.hotel.model.fidelizacion.*;
import com.teamvita.hotel.model.facturacion.*;
import java.util.List;
import java.sql.*;

public class FacturaDAO {
    public void guardarFactura(int idReserva, double total, String medioPago) {
        String sql = "INSERT INTO facturas (id_reserva, total, medio_pago, fecha) VALUES (?, ?, ?, CURDATE())";
        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idReserva);
            stmt.setDouble(2, total);
            stmt.setString(3, medioPago);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la factura: " + e.getMessage());
        }
    }
}
