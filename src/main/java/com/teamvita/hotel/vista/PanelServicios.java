package com.teamvita.hotel.vista;

import javax.swing.*;
import java.awt.*;

public class PanelServicios extends JPanel {
    private javax.swing.table.DefaultTableModel model;
    private JTable table;
    private com.teamvita.hotel.repo.ServicioDAO servicioDAO;

    public PanelServicios() {
        servicioDAO = new com.teamvita.hotel.repo.ServicioDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ── NORTE: título + botón de seña ──────────────────────────────────
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Gestión de Servicios Extra", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(title, BorderLayout.CENTER);

        JButton btnSenia = new JButton("Editar Valor de la Seña");
        btnSenia.addActionListener(e -> editarSenia());
        topPanel.add(btnSenia, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ── CENTRO: tabla de servicios + botón editar ──────────────────────
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

        String[] columnNames = {"Nombre Servicio", "Precio Adicional"};
        model = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Servicios Decorator"));
        centerPanel.add(scrollPane, BorderLayout.CENTER);  // ← aquí estaba el bug

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnEdit = new JButton("Editar Precio de Servicio");
        btnPanel.add(btnEdit);
        btnEdit.addActionListener(e -> editarPrecioServicio());
        centerPanel.add(btnPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);  // ← esto faltaba

        // ── SUR: configuración del hotel (botón plano, sin recuadro) ──────
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        
        JLabel lblConfig = new JLabel("Configuración del Hotel:");
        lblConfig.setFont(new Font("Arial", Font.BOLD, 13));

        JButton btnConfigHotel = new JButton("Editar Configuración");
        btnConfigHotel.addActionListener(e -> editarConfiguracionGeneral());

        bottomPanel.add(lblConfig);
        bottomPanel.add(btnConfigHotel);
        add(bottomPanel, BorderLayout.SOUTH);

        cargarDatos();
    }

    // Llamado al cambiar de pestaña
    public void recargar() { cargarDatos(); }

    private void cargarDatos() {
        model.setRowCount(0);
        java.util.List<Object[]> servicios = servicioDAO.obtenerServiciosParaTabla();
        for (Object[] row : servicios) {
            model.addRow(row);
        }
    }

    private void editarPrecioServicio() {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un servicio de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = (String) model.getValueAt(selected, 0);
        String precioActual = ((String) model.getValueAt(selected, 1)).replace("$ ", "").replace(",", ".");

        String nuevoPrecioStr = JOptionPane.showInputDialog(this, "Ingrese el nuevo precio para " + nombre + ":", precioActual);
        if (nuevoPrecioStr != null && !nuevoPrecioStr.trim().isEmpty()) {
            try {
                double nuevoPrecio = Double.parseDouble(nuevoPrecioStr.replace(",", "."));
                if (servicioDAO.actualizarPrecio(nombre, nuevoPrecio)) {
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el servicio.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarOActualizarConfig(java.sql.Connection con, String clave, String valor) throws java.sql.SQLException {
        java.sql.PreparedStatement ps = con.prepareStatement("UPDATE configuracion SET valor = ? WHERE clave = ?");
        ps.setString(1, valor);
        ps.setString(2, clave);
        int updated = ps.executeUpdate();
        ps.close();
        if (updated == 0) {
            java.sql.PreparedStatement psIns = con.prepareStatement("INSERT INTO configuracion (clave, valor) VALUES (?, ?)");
            psIns.setString(1, clave);
            psIns.setString(2, valor);
            psIns.executeUpdate();
            psIns.close();
        }
    }

    private String obtenerConfig(java.sql.Connection con, String clave, String defaultValue) {
        try {
            java.sql.PreparedStatement psSelect = con.prepareStatement("SELECT valor FROM configuracion WHERE clave = ?");
            psSelect.setString(1, clave);
            java.sql.ResultSet rs = psSelect.executeQuery();
            String res = rs.next() ? rs.getString(1) : defaultValue;
            rs.close(); psSelect.close();
            return res;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private void editarConfiguracionGeneral() {
        try {
            java.sql.Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
            
            String nomActual = obtenerConfig(con, "hotel_nombre", "TeamVita Hotel");
            String dirActual = obtenerConfig(con, "hotel_direccion", "Av. Alvear 1000");

            JTextField txtNom = new JTextField(nomActual);
            JTextField txtDir = new JTextField(dirActual);

            int option = JOptionPane.showConfirmDialog(this, new Object[]{
                "Nombre del Hotel:", txtNom,
                "Dirección:", txtDir
            }, "Configuración General", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String nuevoNom = txtNom.getText().trim();
                String nuevaDir = txtDir.getText().trim();

                if (!nuevoNom.isEmpty()) guardarOActualizarConfig(con, "hotel_nombre", nuevoNom);
                if (!nuevaDir.isEmpty()) guardarOActualizarConfig(con, "hotel_direccion", nuevaDir);

                JOptionPane.showMessageDialog(this, "Configuración actualizada con éxito.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarSenia() {
        try {
            java.sql.Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
            String actual = "30";
            java.sql.PreparedStatement psSelect = con.prepareStatement("SELECT valor FROM configuracion WHERE clave = 'senia_porcentaje'");
            java.sql.ResultSet rs = psSelect.executeQuery();
            if (rs.next()) actual = rs.getString(1);
            rs.close(); psSelect.close();

            String nuevo = JOptionPane.showInputDialog(SwingUtilities.getWindowAncestor(this),
                    "Ingrese el porcentaje de seña requerido (ej: 30):", actual);
            if (nuevo != null && !nuevo.trim().isEmpty()) {
                java.sql.PreparedStatement ps = con.prepareStatement("UPDATE configuracion SET valor = ? WHERE clave = 'senia_porcentaje'");
                ps.setString(1, nuevo.trim());
                int updated = ps.executeUpdate();
                ps.close();
                if (updated == 0) {
                    java.sql.PreparedStatement psIns = con.prepareStatement("INSERT INTO configuracion (clave, valor) VALUES ('senia_porcentaje', ?)");
                    psIns.setString(1, nuevo.trim());
                    psIns.executeUpdate();
                    psIns.close();
                }
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                        "Seña actualizada a " + nuevo.trim() + "%.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
