package com.teamvita.hotel.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelAdministrador extends JPanel {
    public PanelAdministrador() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Encabezado
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Dashboard de Administración", SwingConstants.LEFT);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        headerPanel.add(titulo, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Centro - Estadísticas y Pestañas
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        // Obtener estadísticas reales (sin try-with-resources sobre la conexion)
        String ocupacion = "0%";
        String libres = "0";
        String ingresos = "$ 0";
        try {
            java.sql.Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
            
            java.sql.PreparedStatement psHab = con.prepareStatement(
                "SELECT COUNT(*), SUM(CASE WHEN disponible=1 THEN 1 ELSE 0 END) FROM habitacion");
            java.sql.ResultSet rsHab = psHab.executeQuery();
            if(rsHab.next()) {
                int totalHab = rsHab.getInt(1);
                int libresHab = rsHab.getInt(2);
                libres = String.valueOf(libresHab);
                if(totalHab > 0) {
                    int ocupadas = totalHab - libresHab;
                    ocupacion = (ocupadas * 100 / totalHab) + "%";
                }
            }
            rsHab.close(); psHab.close();
            
            java.sql.PreparedStatement psIng = con.prepareStatement("SELECT SUM(monto) FROM pagos");
            java.sql.ResultSet rsIngresos = psIng.executeQuery();
            if(rsIngresos.next()) {
                double total = rsIngresos.getDouble(1);
                ingresos = String.format("$ %.2f", total);
            }
            rsIngresos.close(); psIng.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // Cuadros de estadísticas
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.add(createStatBox("Ocupación Actual", ocupacion));
        statsPanel.add(createStatBox("Habitaciones Libres", libres));
        statsPanel.add(createStatBox("Ingresos Totales", ingresos));
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        
        // JTabbedPane para Habitaciones, Servicios y Promociones
        PanelServicios panelServicios = new PanelServicios();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Habitaciones y Tarifas", new PanelGestionHabitaciones());
        tabbedPane.addTab("Servicios Extra y Config.", panelServicios);
        tabbedPane.addTab("Promociones", new PanelPromociones());
        
        // Recargar servicios al cambiar a esa pestaña
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                panelServicios.recargar();
            }
        });
        
        centerPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
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
        lblValue.setForeground(new Color(41, 128, 185)); // Color azul
        
        box.add(lblTitle, BorderLayout.NORTH);
        box.add(lblValue, BorderLayout.CENTER);
        return box;
    }
}
