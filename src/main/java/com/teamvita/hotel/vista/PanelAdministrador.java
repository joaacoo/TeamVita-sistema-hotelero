package com.teamvita.hotel.vista;

import javax.swing.*;
import java.awt.*;

public class PanelAdministrador extends JPanel {
    public PanelAdministrador() {
        setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("Panel de Administración", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        JTextArea txtReporte = new JTextArea();
        txtReporte.setText("--- Estadísticas del Hotel ---\nHabitaciones Ocupadas: 15\nHabitaciones Libres: 45\nIngresos del mes: $15,400.00");
        txtReporte.setEditable(false);
        txtReporte.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        add(new JScrollPane(txtReporte), BorderLayout.CENTER);
    }
}
