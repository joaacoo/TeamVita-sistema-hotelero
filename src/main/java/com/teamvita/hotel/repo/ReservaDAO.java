package com.teamvita.hotel.repo;
import com.teamvita.hotel.model.*;
import com.teamvita.hotel.model.habitacion.*;
import com.teamvita.hotel.model.reserva.*;
import com.teamvita.hotel.model.servicio.*;
import com.teamvita.hotel.model.fidelizacion.*;
import com.teamvita.hotel.model.facturacion.*;
import java.util.List;
import java.sql.*;

public class ReservaDAO {
    public Reserva getReserva(int idReserva) {
        String sql = "SELECT * FROM reservas WHERE id = ?";
        try (Connection conn = ConexionBD.getInstancia().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReserva);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // TODO: Instanciar y retornar reserva desde el ResultSet
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void actualizarReserva(Reserva reserva) {
        String sql = "UPDATE reservas SET estado = ? WHERE id = ?";
        try (Connection conn = ConexionBD.getInstancia().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // TODO: Setear parámetros del statement
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void guardarReserva(Reserva reserva) {
        String sql = "INSERT INTO reservas (huesped_id, fecha_in, fecha_out) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getInstancia().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // TODO: Setear parámetros del statement
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
