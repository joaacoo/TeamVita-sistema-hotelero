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
            tabbedPane.addTab("Gestión de Reservas", new PanelReserva());
            tabbedPane.addTab("Check-In", new PanelCheckIn());
        } else if ("admin".equals(user) || "administrador".equals(user)) {
            tabbedPane.addTab("Administración", new PanelAdministrador());
        } else {
            // Por si acaso entra un usuario sin rol específico o para pruebas
            tabbedPane.addTab("Gestión de Reservas", new PanelReserva());
            tabbedPane.addTab("Check-In", new PanelCheckIn());
            tabbedPane.addTab("Administración", new PanelAdministrador());
        }

        add(tabbedPane, BorderLayout.CENTER);
    }
}
