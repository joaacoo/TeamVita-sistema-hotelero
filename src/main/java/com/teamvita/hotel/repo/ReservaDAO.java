package com.teamvita.hotel.repo;

import com.teamvita.hotel.model.reserva.Reserva;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReservaDAO {
    public void insert(Reserva reserva) {
        String sql = "INSERT INTO reserva (id_huesped, estado, fecha_creacion) VALUES (?, ?, CURDATE())";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            if (con == null) throw new RuntimeException("No hay conexion a BD");
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, reserva.getHuesped().getId() == 0 ? 1 : reserva.getHuesped().getId()); // ID dummy o real
            ps.setString(2, reserva.getEstado().getNombreEstado());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error en BD: " + e.getMessage());
        }
    }

    public java.util.List<Object[]> obtenerReservasParaTabla() {
        java.util.List<Object[]> lista = new java.util.ArrayList<>();
        String sql = "SELECT r.id, h.nombre, r.fecha_creacion, r.estado " +
                     "FROM reserva r JOIN huesped h ON r.id_huesped = h.id ORDER BY r.id DESC";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            if (con == null) throw new RuntimeException("No hay conexion a BD");
            
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[] {
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDate("fecha_creacion"),
                    "Check-Out pendiente", // Dummy date for Check-Out as it's not in DB
                    rs.getString("estado"),
                    "$ 0.0" // Dummy total as we don't have it
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void actualizarEstado(int idReserva, String nuevoEstado) {
        String sql = "UPDATE reserva SET estado = ? WHERE id = ?";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            if (con == null) throw new RuntimeException("No hay conexion a BD");
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idReserva);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar estado en BD: " + e.getMessage());
        }
    }
}
