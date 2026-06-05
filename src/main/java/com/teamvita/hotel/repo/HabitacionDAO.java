package com.teamvita.hotel.repo;
import com.teamvita.hotel.model.*;
import com.teamvita.hotel.model.habitacion.*;
import com.teamvita.hotel.model.reserva.*;
import com.teamvita.hotel.model.servicio.*;
import com.teamvita.hotel.model.fidelizacion.*;
import com.teamvita.hotel.model.facturacion.*;
import java.util.List;
import java.sql.*;

public class HabitacionDAO {
    public void marcarNoDisponible(Habitacion habitacion) {
        String sql = "UPDATE habitaciones SET disponible = false WHERE numero = ?";
        try (Connection conn = ConexionBD.getInstancia().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // TODO: Setear parámetros del statement
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean verificarDisponibilidad(List<Habitacion> habitaciones, Object fechas) {
        // TODO: Lógica de verificación con base de datos
        return true; 
    }
}
