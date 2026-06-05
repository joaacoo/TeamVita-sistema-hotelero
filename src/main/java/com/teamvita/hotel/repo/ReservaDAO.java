package com.teamvita.hotel.repo;

import com.teamvita.hotel.model.reserva.Reserva;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReservaDAO {
    public void insert(Reserva reserva) {
        String sql = "INSERT INTO reserva (id_huesped, estado) VALUES (?, ?)";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, reserva.getHuesped().getId());
            ps.setString(2, reserva.getEstado().getNombreEstado());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
