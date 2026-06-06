package com.teamvita.hotel.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PromocionDAO {
    
    public List<Object[]> obtenerPromocionesParaTabla() {
        List<Object[]> promociones = new ArrayList<>();
        String sql = "SELECT id, nombre, descuento_porcentaje FROM promocion ORDER BY descuento_porcentaje";
        Connection con = ConexionBD.getInstancia().getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                promociones.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    String.format("%.1f %%", rs.getDouble("descuento_porcentaje"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promociones;
    }

    public boolean agregarPromocion(String nombre, double descuento) {
        String sql = "INSERT INTO promocion (nombre, descuento_porcentaje) VALUES (?, ?)";
        Connection con = ConexionBD.getInstancia().getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setDouble(2, descuento);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarPromocion(int id) {
        String sql = "DELETE FROM promocion WHERE id = ?";
        Connection con = ConexionBD.getInstancia().getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
