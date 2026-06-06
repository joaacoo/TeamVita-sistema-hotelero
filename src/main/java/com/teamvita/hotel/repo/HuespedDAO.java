package com.teamvita.hotel.repo;

import com.teamvita.hotel.model.Huesped;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class HuespedDAO {
    public void insert(Huesped huesped) {
        insertWithDni(huesped, "00000000", "Clásica"); // Legacy
    }
    
    public void insertWithDni(Huesped huesped, String dni, String categoriaFidelizacion) {
        String sql = "INSERT INTO huesped (dni, nombre, email, telefono, categoria_fidelizacion) VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), email=VALUES(email), telefono=VALUES(telefono), categoria_fidelizacion=VALUES(categoria_fidelizacion)";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            if (con == null) throw new RuntimeException("No hay conexion a BD");
            
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dni);
            ps.setString(2, huesped.getNombre());
            ps.setString(3, huesped.getEmail());
            ps.setString(4, huesped.getTelefono());
            ps.setString(5, categoriaFidelizacion);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) huesped.setId(rs.getInt(1));
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error en BD: " + e.getMessage());
        }
    }
}
