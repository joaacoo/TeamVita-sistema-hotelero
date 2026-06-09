package com.teamvita.hotel.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConsumoServicioDAO {

    public void registrarConsumo(int idReserva, String nombreServicio, int cantidad) {
        String sql = "INSERT INTO consumo_servicio (id_reserva, id_servicio, cantidad, fecha) " +
                     "SELECT ?, id, ?, CURDATE() FROM servicio WHERE nombre = ?";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idReserva);
            ps.setInt(2, cantidad);
            ps.setString(3, nombreServicio);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al registrar consumo: " + e.getMessage());
        }
    }

    public double getTotalConsumos(int idReserva) {
        String sql = "SELECT COALESCE(SUM(c.cantidad * s.precio), 0) FROM consumo_servicio c " +
                     "JOIN servicio s ON c.id_servicio = s.id WHERE c.id_reserva = ?";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();
            double total = 0;
            if (rs.next()) {
                total = rs.getDouble(1);
            }
            rs.close();
            ps.close();
            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
