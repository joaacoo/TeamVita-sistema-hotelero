package com.teamvita.hotel.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private JTabbedPane tabbedPane;

    public VentanaPrincipal(String user) {
        setTitle("TeamVita - Sistema de Gestión Hotelera - Usuario: " + user);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        tabbedPane = new JTabbedPane();
        
        if ("recepcion".equals(user) || "recepcionista".equals(user)) {
            PanelCheckIn panelCheckIn = new PanelCheckIn();
            tabbedPane.addTab("Nueva Reserva", new PanelReserva(tabbedPane));
            tabbedPane.addTab("Gestionar Reservas", panelCheckIn);
            // Recargar reservas automáticamente al cambiar a esa pestaña
            tabbedPane.addChangeListener(e -> {
                if (tabbedPane.getSelectedIndex() == 1) {
                    panelCheckIn.cargarDatos();
                }
            });
        } else if ("admin".equals(user) || "administrador".equals(user)) {
            tabbedPane.addTab("Administración", new PanelAdministrador());
        } else {
            PanelCheckIn panelCheckIn = new PanelCheckIn();
            tabbedPane.addTab("Nueva Reserva", new PanelReserva(tabbedPane));
            tabbedPane.addTab("Gestionar Reservas", panelCheckIn);
            tabbedPane.addTab("Administración", new PanelAdministrador());
            tabbedPane.addChangeListener(e -> {
                if (tabbedPane.getSelectedIndex() == 1) {
                    panelCheckIn.cargarDatos();
                }
            });
        }

        add(tabbedPane, BorderLayout.CENTER);
    }
}
