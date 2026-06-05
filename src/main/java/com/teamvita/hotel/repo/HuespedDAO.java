package com.teamvita.hotel.repo;

import com.teamvita.hotel.model.Huesped;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class HuespedDAO {
    public void insert(Huesped huesped) {
        String sql = "INSERT INTO huesped (nombre, email, telefono) VALUES (?, ?, ?)";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, huesped.getNombre());
            ps.setString(2, huesped.getEmail());
            ps.setString(3, huesped.getTelefono());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
