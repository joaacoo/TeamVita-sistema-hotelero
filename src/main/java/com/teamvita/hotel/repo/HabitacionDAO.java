package com.teamvita.hotel.repo;

import com.teamvita.hotel.model.habitacion.Habitacion;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class HabitacionDAO {
    public void update(Habitacion habitacion) {
        String sql = "UPDATE habitacion SET disponible = ? WHERE numero = ?";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, habitacion.isDisponible());
            ps.setInt(2, habitacion.getNumero());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
