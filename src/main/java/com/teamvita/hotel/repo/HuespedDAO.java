package com.teamvita.hotel.repo;

import com.teamvita.hotel.model.Huesped;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class HuespedDAO {

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
    
    public java.util.List<Object[]> obtenerTodos() {
        java.util.List<Object[]> lista = new java.util.ArrayList<>();
        String sql = "SELECT id, dni, nombre, email, telefono, categoria_fidelizacion FROM huesped ORDER BY nombre";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    rs.getString("categoria_fidelizacion")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void actualizarHuesped(int id, String dni, String nombre, String email, String telefono, String categoria) {
        String sql = "UPDATE huesped SET dni=?, nombre=?, email=?, telefono=?, categoria_fidelizacion=? WHERE id=?";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, dni);
            ps.setString(2, nombre);
            ps.setString(3, email);
            ps.setString(4, telefono);
            ps.setString(5, categoria);
            ps.setInt(6, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar huésped: " + e.getMessage());
        }
    }

    public Huesped buscarPorDNI(String dni) {
        String sql = "SELECT id, nombre, email, telefono, categoria_fidelizacion FROM huesped WHERE dni = ?";
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Huesped h = new Huesped(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("telefono")
                );
                String cat = rs.getString("categoria_fidelizacion");
                if ("Gold".equals(cat)) h.setCategoria(new com.teamvita.hotel.model.fidelizacion.CategoriaGold());
                else if ("Platinum".equals(cat)) h.setCategoria(new com.teamvita.hotel.model.fidelizacion.CategoriaPlatinum());
                else h.setCategoria(new com.teamvita.hotel.model.fidelizacion.CategoriaClasica());
                
                return h;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
