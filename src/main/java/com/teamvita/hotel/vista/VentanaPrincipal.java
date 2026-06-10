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
            PanelReserva panelReserva = new PanelReserva(tabbedPane);
            PanelCheckIn panelCheckIn = new PanelCheckIn();
            PanelHuesped panelHuesped = new PanelHuesped();
            tabbedPane.addTab("Nueva Reserva", panelReserva);
            tabbedPane.addTab("Gestionar Reservas", panelCheckIn);
            tabbedPane.addTab("Gestión de Huéspedes", panelHuesped);
            
            tabbedPane.addChangeListener(e -> {
                int index = tabbedPane.getSelectedIndex();
                if (index == 0) panelReserva.recargarDatos();
                else if (index == 1) panelCheckIn.cargarDatos();
                else if (index == 2) panelHuesped.cargarDatos();
            });
        } else if ("admin".equals(user) || "administrador".equals(user)) {
            tabbedPane.addTab("Administración", new PanelAdministrador());
        } else {
            PanelReserva panelReserva = new PanelReserva(tabbedPane);
            PanelCheckIn panelCheckIn = new PanelCheckIn();
            PanelHuesped panelHuesped = new PanelHuesped();
            tabbedPane.addTab("Nueva Reserva", panelReserva);
            tabbedPane.addTab("Gestionar Reservas", panelCheckIn);
            tabbedPane.addTab("Gestión de Huéspedes", panelHuesped);
            tabbedPane.addTab("Administración", new PanelAdministrador());
            
            tabbedPane.addChangeListener(e -> {
                int index = tabbedPane.getSelectedIndex();
                if (index == 0) panelReserva.recargarDatos();
                else if (index == 1) panelCheckIn.cargarDatos();
                else if (index == 2) panelHuesped.cargarDatos();
            });
        }

        add(tabbedPane, BorderLayout.CENTER);
    }
}
