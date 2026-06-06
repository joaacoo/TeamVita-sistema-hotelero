package com.teamvita.hotel.vista;

import com.teamvita.hotel.repo.ConexionBD;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VentanaLogin extends JFrame {
    public VentanaLogin() {
        setTitle("TeamVita - Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
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
            
            if (autenticar(user, pass)) {
                this.dispose();
                VentanaPrincipal principal = new VentanaPrincipal(user);
                principal.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas o BD no conectada", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panelCentral, BorderLayout.CENTER);
    }
    
    private boolean autenticar(String user, String pass) {
        try {
            Connection con = ConexionBD.getInstancia().getConexion();
            if (con == null) return false;
            String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Si hay resultado, es true
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
