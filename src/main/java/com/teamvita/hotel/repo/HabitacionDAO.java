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
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(int numero, String tipo, double precioBase, int capacidad) {
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            try {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO habitacion (numero, tipo, precio_base, capacidad, disponible) VALUES (?, ?, ?, ?, TRUE)");
                ps.setInt(1, numero);
                ps.setString(2, tipo);
                ps.setDouble(3, precioBase);
                ps.setInt(4, capacidad);
                ps.executeUpdate();
                ps.close();
            } catch (java.sql.SQLException ex2) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO habitacion (numero, tipo, precio_base, disponible) VALUES (?, ?, ?, TRUE)");
                ps.setInt(1, numero);
                ps.setString(2, tipo);
                ps.setDouble(3, precioBase);
                ps.executeUpdate();
                ps.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error insertando habitación: " + e.getMessage(), e);
        }
    }

    public void updateCompleto(int numero, String tipo, double precioBase, int capacidad) {
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            try {
                PreparedStatement psUpd = con.prepareStatement(
                        "UPDATE habitacion SET tipo = ?, precio_base = ?, capacidad = ? WHERE numero = ?");
                psUpd.setString(1, tipo);
                psUpd.setDouble(2, precioBase);
                psUpd.setInt(3, capacidad);
                psUpd.setInt(4, numero);
                psUpd.executeUpdate();
                psUpd.close();
            } catch (java.sql.SQLException ex2) {
                PreparedStatement psUpd = con.prepareStatement(
                        "UPDATE habitacion SET tipo = ?, precio_base = ? WHERE numero = ?");
                psUpd.setString(1, tipo);
                psUpd.setDouble(2, precioBase);
                psUpd.setInt(3, numero);
                psUpd.executeUpdate();
                psUpd.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error actualizando habitación: " + e.getMessage(), e);
        }
    }

    public java.util.List<Object[]> getAllForTable() {
        java.util.List<Object[]> lista = new java.util.ArrayList<>();
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps;
            try {
                ps = con.prepareStatement(
                        "SELECT numero, tipo, precio_base, capacidad, disponible FROM habitacion ORDER BY numero");
                java.sql.ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    lista.add(new Object[] {
                            rs.getInt("numero"), rs.getString("tipo"),
                            String.format("$ %.2f", rs.getDouble("precio_base")),
                            rs.getInt("capacidad")
                    });
                }
                rs.close();
                ps.close();
            } catch (java.sql.SQLException ex) {
                ps = con.prepareStatement(
                        "SELECT numero, tipo, precio_base, disponible FROM habitacion ORDER BY numero");
                java.sql.ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    lista.add(new Object[] {
                            rs.getInt("numero"), rs.getString("tipo"),
                            String.format("$ %.2f", rs.getDouble("precio_base")),
                            "-"
                    });
                }
                rs.close();
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Object[] getHabitacionData(int numero) {
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con
                    .prepareStatement("SELECT tipo, precio_base, capacidad FROM habitacion WHERE numero = ?");
            ps.setInt(1, numero);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String tipo = rs.getString("tipo");
                double precio = rs.getDouble("precio_base");
                int cap;
                try {
                    cap = rs.getInt("capacidad");
                } catch (Exception e) {
                    cap = 1;
                }
                rs.close();
                ps.close();
                return new Object[] { tipo, precio, cap };
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isHabitacionOcupada(int numero) {
        boolean ocupada = false;
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement psCheck = con.prepareStatement(
                    "SELECT COUNT(*) FROM detalle_reserva dr " +
                            "JOIN reserva r ON r.id = dr.id_reserva " +
                            "WHERE dr.numero_habitacion = ? AND r.estado = 'EN CURSO'");
            psCheck.setInt(1, numero);
            java.sql.ResultSet rsCheck = psCheck.executeQuery();
            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                ocupada = true;
            }
            rsCheck.close();
            psCheck.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ocupada;
    }
}
