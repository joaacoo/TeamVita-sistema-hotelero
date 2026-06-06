package com.teamvita.hotel.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private JTabbedPane tabbedPane;

    public VentanaPrincipal() {
        setTitle("TeamVita - Sistema de Gestión Hotelera");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Gestión de Reservas", new PanelReserva());
        tabbedPane.addTab("Check-In", new PanelCheckIn());
        tabbedPane.addTab("Administración", new PanelAdministrador());

        add(tabbedPane, BorderLayout.CENTER);
    }
}
