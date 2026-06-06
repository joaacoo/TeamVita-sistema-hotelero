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
        btnHeaderPanel.add(btnTarifas);
        headerPanel.add(btnHeaderPanel, BorderLayout.EAST);
        
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
