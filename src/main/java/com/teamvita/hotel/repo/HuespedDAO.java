package com.teamvita.hotel.repo;
import com.teamvita.hotel.model.*;
import com.teamvita.hotel.model.habitacion.*;
import com.teamvita.hotel.model.reserva.*;
import com.teamvita.hotel.model.servicio.*;
import com.teamvita.hotel.model.fidelizacion.*;
import com.teamvita.hotel.model.facturacion.*;
import java.util.List;
import java.sql.*;

public class HuespedDAO {
    public Huesped obtenerORegistrarHuesped(Object datosHuesped) {
        String sql = "SELECT * FROM huespedes WHERE dni = ?";
        try (Connection conn = ConexionBD.getInstancia().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // TODO: Buscar si existe, sino hacer INSERT
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
