package com.teamvita.hotel.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelAdministrador extends JPanel {
    private JLabel lblOcupacion;
    private JLabel lblLibres;
    private JLabel lblIngresos;

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
        
        // Inicializar labels
        lblOcupacion = new JLabel("0%", SwingConstants.CENTER);
        lblOcupacion.setFont(new Font("Arial", Font.BOLD, 24));
        lblOcupacion.setForeground(new Color(41, 128, 185));
        
        lblLibres = new JLabel("0", SwingConstants.CENTER);
        lblLibres.setFont(new Font("Arial", Font.BOLD, 24));
        lblLibres.setForeground(new Color(41, 128, 185));
        
        lblIngresos = new JLabel("$ 0", SwingConstants.CENTER);
        lblIngresos.setFont(new Font("Arial", Font.BOLD, 24));
        lblIngresos.setForeground(new Color(41, 128, 185));
        
        // Cargar datos reales
        recargarDatos();
        
        // Cuadros de estadísticas
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.add(createStatBox("Ocupación Actual", lblOcupacion));
        statsPanel.add(createStatBox("Habitaciones Libres", lblLibres));
        statsPanel.add(createStatBox("Ingresos Totales", lblIngresos));
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
    
    public void recargarDatos() {
        try {
            java.sql.Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
            
            // Calcula libres si disponible=1 Y NO está reservada para HOY
            java.sql.PreparedStatement psHab = con.prepareStatement(
                "SELECT COUNT(*), SUM(CASE WHEN h.disponible=1 AND h.numero NOT IN " +
                "(SELECT dr.numero_habitacion FROM detalle_reserva dr JOIN reserva r ON dr.id_reserva = r.id " +
                "WHERE r.estado NOT IN ('Cancelada', 'Finalizada') AND dr.fecha_inicio <= CURDATE() AND dr.fecha_fin > CURDATE()) " +
                "THEN 1 ELSE 0 END) FROM habitacion h");
            java.sql.ResultSet rsHab = psHab.executeQuery();
            if(rsHab.next()) {
                int totalHab = rsHab.getInt(1);
                int libresHab = rsHab.getInt(2);
                lblLibres.setText(String.valueOf(libresHab));
                if(totalHab > 0) {
                    int ocupadas = totalHab - libresHab;
                    lblOcupacion.setText((ocupadas * 100 / totalHab) + "%");
                }
            }
            rsHab.close(); psHab.close();
            
            java.sql.PreparedStatement psIng = con.prepareStatement("SELECT SUM(monto) FROM pagos");
            java.sql.ResultSet rsIngresos = psIng.executeQuery();
            if(rsIngresos.next()) {
                double total = rsIngresos.getDouble(1);
                lblIngresos.setText(String.format("$ %.2f", total));
            }
            rsIngresos.close(); psIng.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private JPanel createStatBox(String title, JLabel lblValue) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        box.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitle.setForeground(Color.DARK_GRAY);
        
        box.add(lblTitle, BorderLayout.NORTH);
        box.add(lblValue, BorderLayout.CENTER);
        return box;
    }
}
