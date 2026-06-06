package com.teamvita.hotel.repo;

import com.teamvita.hotel.model.Huesped;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class HuespedDAO {
    public void insert(Huesped huesped) {
        insertWithDni(huesped, "00000000"); // Legacy
    }
    
    public void insertWithDni(Huesped huesped, String dni) {
        String sql = "INSERT INTO huesped (dni, nombre, email, telefono) VALUES (?, ?, ?, ?)";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            if (con == null) throw new RuntimeException("No hay conexion a BD");
            
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dni);
            ps.setString(2, huesped.getNombre());
            ps.setString(3, huesped.getEmail());
            ps.setString(4, huesped.getTelefono());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) huesped.setId(rs.getInt(1));
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error en BD: " + e.getMessage());
        }
    }
}
