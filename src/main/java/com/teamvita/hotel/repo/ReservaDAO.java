package com.teamvita.hotel.repo;

import com.teamvita.hotel.model.reserva.Reserva;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReservaDAO {
    public int insert(Reserva reserva, double totalEstimado, int cantidadPersonas) {
        String sql = "INSERT INTO reserva (id_huesped, estado, fecha_creacion, total_estimado, cantidad_personas) VALUES (?, ?, CURDATE(), ?, ?)";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            if (con == null)
                throw new RuntimeException("No hay conexion a BD");

            PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, reserva.getHuesped().getId() == 0 ? 1 : reserva.getHuesped().getId()); // ID ficticio o real
            ps.setString(2, reserva.getEstado().getNombreEstado());
            ps.setDouble(3, totalEstimado);
            ps.setInt(4, cantidadPersonas);
            ps.executeUpdate();

            java.sql.ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                reserva.setId(id);
                return id;
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error en BD: " + e.getMessage());
        }
    }

    public java.util.List<Object[]> obtenerReservasParaTabla() {
        java.util.List<Object[]> lista = new java.util.ArrayList<>();
        String sql = "SELECT r.id, h.nombre, r.fecha_creacion, r.estado, r.total_estimado " +
                "FROM reserva r JOIN huesped h ON r.id_huesped = h.id ORDER BY r.id DESC";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            if (con == null)
                throw new RuntimeException("No hay conexion a BD");

            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[] {
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_creacion"),
                        "Check-Out pendiente",
                        rs.getString("estado"),
                        String.format("$ %.2f", rs.getDouble("total_estimado"))
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
            if (con == null)
                throw new RuntimeException("No hay conexion a BD");

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idReserva);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar estado en BD: " + e.getMessage());
        }
    }

    public void insertDetalleReserva(int idReserva, int numeroHabitacion, java.time.LocalDate checkIn, java.time.LocalDate checkOut) {
        String sql = "INSERT INTO detalle_reserva (id_reserva, numero_habitacion, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            if (con == null)
                throw new RuntimeException("No hay conexion a BD");

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idReserva);
            ps.setInt(2, numeroHabitacion);
            ps.setDate(3, java.sql.Date.valueOf(checkIn));
            ps.setDate(4, java.sql.Date.valueOf(checkOut));
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error guardando detalle_reserva: " + e.getMessage());
        }
    }
}
