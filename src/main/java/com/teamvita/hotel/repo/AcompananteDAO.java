package com.teamvita.hotel.repo;

import com.teamvita.hotel.model.reserva.Acompanante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AcompananteDAO {

    public void insertar(Acompanante acompanante) {
        String sql = "INSERT INTO acompanante (id_reserva, nombre, dni) VALUES (?, ?, ?)";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, acompanante.getIdReserva());
            ps.setString(2, acompanante.getNombre());
            ps.setString(3, acompanante.getDni());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[AcompananteDAO] Error al insertar acompanante: " + e.getMessage());
        }
    }

    public List<Acompanante> obtenerPorReserva(int idReserva) {
        List<Acompanante> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, dni FROM acompanante WHERE id_reserva = ?";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Acompanante(
                    rs.getInt("id"),
                    idReserva,
                    rs.getString("nombre"),
                    rs.getString("dni")
                ));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
