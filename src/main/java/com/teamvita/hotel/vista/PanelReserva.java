package com.teamvita.hotel.vista;

import com.teamvita.hotel.negocio.SistemaHotelFacade;
import javax.swing.*;
import java.awt.*;

public class PanelReserva extends JPanel {
    private SistemaHotelFacade facade;

    public PanelReserva() {
        this.facade = new SistemaHotelFacade();
        setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("Registrar Nueva Reserva", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        // Usamos GridBagLayout para evitar que los campos se estiren por toda la pantalla
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("DNI Huésped:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField txtDni = new JTextField(15);
        formPanel.add(txtDni, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Check-In (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        JTextField txtCheckIn = new JTextField(15);
        formPanel.add(txtCheckIn, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Check-Out (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JTextField txtCheckOut = new JTextField(15);
        formPanel.add(txtCheckOut, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Tipo de Habitación:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Simple", "Doble", "Suite"});
        cmbTipo.setPreferredSize(new Dimension(165, 25));
        formPanel.add(cmbTipo, gbc);

        // Envolvemos el formulario para que quede centrado arriba y no se estire
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(formPanel);
        add(wrapper, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Confirmar Reserva");
        btnGuardar.setPreferredSize(new Dimension(200, 40));
        btnGuardar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Reserva registrada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel btnPanel = new JPanel();
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        btnPanel.add(btnGuardar);
        add(btnPanel, BorderLayout.SOUTH);
    }
}
