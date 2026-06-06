package com.teamvita.hotel.vista;

import com.teamvita.hotel.negocio.SistemaHotelFacade;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
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

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 0: DNI
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("DNI Huésped (Máx 8):"), gbc);
        gbc.gridx = 1;
        JTextField txtDni = new JTextField(15);
        aplicarFiltroDni(txtDni);
        formPanel.add(txtDni, gbc);

        // Fila 1: Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1;
        JTextField txtNombre = new JTextField(15);
        formPanel.add(txtNombre, gbc);

        // Fila 2: Email
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Correo Electrónico:"), gbc);
        gbc.gridx = 1;
        JTextField txtEmail = new JTextField(15);
        formPanel.add(txtEmail, gbc);

        // Fila 3: Telefono
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        JTextField txtTelefono = new JTextField(15);
        formPanel.add(txtTelefono, gbc);

        // Fila 4: Check-In
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Check-In:"), gbc);
        gbc.gridx = 1;
        JPanel pnlCheckIn = new JPanel(new BorderLayout());
        JTextField txtCheckIn = new JTextField(10);
        txtCheckIn.setEditable(false);
        JButton btnCalIn = new JButton("📅");
        btnCalIn.addActionListener(e -> {
            DatePicker dp = new DatePicker((JFrame) SwingUtilities.getWindowAncestor(this));
            dp.showDialog();
            txtCheckIn.setText(dp.setPickedDate());
        });
        pnlCheckIn.add(txtCheckIn, BorderLayout.CENTER);
        pnlCheckIn.add(btnCalIn, BorderLayout.EAST);
        formPanel.add(pnlCheckIn, gbc);

        // Fila 5: Check-Out
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Check-Out:"), gbc);
        gbc.gridx = 1;
        JPanel pnlCheckOut = new JPanel(new BorderLayout());
        JTextField txtCheckOut = new JTextField(10);
        txtCheckOut.setEditable(false);
        JButton btnCalOut = new JButton("📅");
        btnCalOut.addActionListener(e -> {
            DatePicker dp = new DatePicker((JFrame) SwingUtilities.getWindowAncestor(this));
            dp.showDialog();
            txtCheckOut.setText(dp.setPickedDate());
        });
        pnlCheckOut.add(txtCheckOut, BorderLayout.CENTER);
        pnlCheckOut.add(btnCalOut, BorderLayout.EAST);
        formPanel.add(pnlCheckOut, gbc);

        // Fila 6: Habitacion
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Tipo Habitación:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Simple", "Doble", "Suite"});
        cmbTipo.setPreferredSize(new Dimension(165, 25));
        formPanel.add(cmbTipo, gbc);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(formPanel);
        add(wrapper, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Confirmar reserva");
        btnGuardar.setPreferredSize(new Dimension(220, 40));
        btnGuardar.addActionListener(e -> {
            if (txtDni.getText().isEmpty() || txtNombre.getText().isEmpty() || txtCheckIn.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                java.time.LocalDate dIn = java.time.LocalDate.parse(txtCheckIn.getText());
                java.time.LocalDate dOut = java.time.LocalDate.parse(txtCheckOut.getText());
                
                // Llamar al Facade para guardar
                facade.registrarReservaCompleta(
                    txtDni.getText(), txtNombre.getText(), txtEmail.getText(), txtTelefono.getText(),
                    dIn, dOut, cmbTipo.getSelectedItem().toString()
                );
                
                JOptionPane.showMessageDialog(this, "Reserva registrada con éxito en MySQL.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al guardar en BD: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JPanel btnPanel = new JPanel();
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        btnPanel.add(btnGuardar);
        add(btnPanel, BorderLayout.SOUTH);
    }
    
    private void aplicarFiltroDni(JTextField txt) {
        ((AbstractDocument) txt.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String newStr = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if (newStr.matches("\\d{0,8}")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }
}
