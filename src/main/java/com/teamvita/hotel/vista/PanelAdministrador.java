package com.teamvita.hotel.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelAdministrador extends JPanel {
    public PanelAdministrador() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Dashboard de Administración", SwingConstants.LEFT);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        headerPanel.add(titulo, BorderLayout.WEST);
        
        JPanel btnHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTarifas = new JButton("Configurar Tarifas");
        JButton btnSenia = new JButton("Configurar Seña");
        btnHeaderPanel.add(btnTarifas);
        btnHeaderPanel.add(btnSenia);
        headerPanel.add(btnHeaderPanel, BorderLayout.EAST);
        
        btnSenia.addActionListener(e -> {
            try {
                java.sql.Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
                String actual = "30";
                
                // Fetch current from DB
                java.sql.ResultSet rs = con.createStatement().executeQuery("SELECT valor FROM configuracion WHERE clave = 'senia_porcentaje'");
                if (rs.next()) {
                    actual = rs.getString(1);
                }
                
                String nuevo = JOptionPane.showInputDialog(this, "Ingrese el porcentaje de seña requerido para reservas (ej: 30):", actual);
                
                if (nuevo != null && !nuevo.trim().isEmpty()) {
                    // Update in DB
                    java.sql.PreparedStatement ps = con.prepareStatement("UPDATE configuracion SET valor = ? WHERE clave = 'senia_porcentaje'");
                    ps.setString(1, nuevo.trim());
                    int updated = ps.executeUpdate();
                    
                    if (updated == 0) {
                        // If it didn't exist for some reason, insert it
                        java.sql.PreparedStatement psInsert = con.prepareStatement("INSERT INTO configuracion (clave, valor) VALUES ('senia_porcentaje', ?)");
                        psInsert.setString(1, nuevo.trim());
                        psInsert.executeUpdate();
                    }
                    
                    JOptionPane.showMessageDialog(this, "Porcentaje de seña actualizado a " + nuevo.trim() + "% en la Base de Datos.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar configuración en BD: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        add(headerPanel, BorderLayout.NORTH);

        // Center - Stats and Table
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        // Stats boxes
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.add(createStatBox("Ocupación Actual", "75%"));
        statsPanel.add(createStatBox("Habitaciones Libres", "12"));
        statsPanel.add(createStatBox("Ingresos Mensuales", "$ 15,400"));
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        
        // El administrador ahora solo gestiona habitaciones, promociones y reportes (no ve reservas)
        JPanel spacer = new JPanel();
        centerPanel.add(spacer, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Action Listeners
        btnTarifas.addActionListener(e -> JOptionPane.showMessageDialog(this, "Módulo de configuración de tarifas en desarrollo.", "Configuración", JOptionPane.WARNING_MESSAGE));
    }
    
    private JPanel createStatBox(String title, String value) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        box.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitle.setForeground(Color.DARK_GRAY);
        
        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 24));
        lblValue.setForeground(new Color(41, 128, 185)); // Blue color
        
        box.add(lblTitle, BorderLayout.NORTH);
        box.add(lblValue, BorderLayout.CENTER);
        return box;
    }
}
