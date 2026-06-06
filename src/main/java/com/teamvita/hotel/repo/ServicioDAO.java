package com.teamvita.hotel.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {
    
    public List<Object[]> obtenerServiciosParaTabla() {
        List<Object[]> servicios = new ArrayList<>();
        String sql = "SELECT nombre, precio FROM servicio ORDER BY nombre";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                servicios.add(new Object[]{
                    rs.getString("nombre"),
                    String.format("$ %.2f", rs.getDouble("precio"))
                });
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("[ServicioDAO] Error al obtener servicios: " + e.getMessage());
            e.printStackTrace();
        }
        return servicios;
    }

    public boolean actualizarPrecio(String nombre, double nuevoPrecio) {
        String sql = "UPDATE servicio SET precio = ? WHERE nombre = ?";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, nuevoPrecio);
            ps.setString(2, nombre);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[ServicioDAO] Error al actualizar precio: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Helper para los Decorators
    public static double getPrecioServicio(String nombreServicio, double defaultPrecio) {
        String sql = "SELECT precio FROM servicio WHERE nombre = ?";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombreServicio);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double precio = rs.getDouble("precio");
                rs.close();
                ps.close();
                return precio;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("[ServicioDAO] Error al obtener precio de " + nombreServicio + ": " + e.getMessage());
        }
        return defaultPrecio;
    }
}
