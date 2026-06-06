package com.teamvita.hotel.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {
    public VentanaLogin() {
        setTitle("TeamVita - Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelCentral.add(new JLabel("Usuario:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField txtUsuario = new JTextField(15);
        panelCentral.add(txtUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelCentral.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        JPasswordField txtPassword = new JPasswordField(15);
        panelCentral.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnLogin = new JButton("Ingresar");
        panelCentral.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            String user = txtUsuario.getText();
            String pass = new String(txtPassword.getPassword());
            
            // Credenciales simples (admin / admin)
            if (user.equals("admin") && pass.equals("admin")) {
                this.dispose(); // Cerrar login
                VentanaPrincipal principal = new VentanaPrincipal();
                principal.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panelCentral, BorderLayout.CENTER);
    }
}
