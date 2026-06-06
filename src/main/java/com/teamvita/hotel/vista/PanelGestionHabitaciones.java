package com.teamvita.hotel.vista;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PanelGestionHabitaciones extends JPanel {
    private JTable table;
    private javax.swing.table.DefaultTableModel model;

    public PanelGestionHabitaciones() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Gestión de Habitaciones y Tarifas", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        String[] columnNames = {"Número", "Tipo", "Precio Base", "Capacidad", "Disponible"};
        model = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd  = new JButton("Nueva Habitación");
        JButton btnEdit = new JButton("Editar Habitación");
        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        add(btnPanel, BorderLayout.SOUTH);

        btnAdd .addActionListener(e -> agregarHabitacion());
        btnEdit.addActionListener(e -> editarHabitacion());

        cargarDatos();
    }

    private void cargarDatos() {
        model.setRowCount(0);
        try {
            Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
            PreparedStatement ps;
            try {
                ps = con.prepareStatement(
                    "SELECT numero, tipo, precio_base, capacidad, disponible FROM habitacion ORDER BY numero");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("numero"),
                        rs.getString("tipo"),
                        String.format("$ %.2f", rs.getDouble("precio_base")),
                        rs.getInt("capacidad"),
                        rs.getBoolean("disponible") ? "Sí" : "No"
                    });
                }
                rs.close(); ps.close();
            } catch (SQLException ex) {
                // Sin columna capacidad (BD vieja)
                ps = con.prepareStatement(
                    "SELECT numero, tipo, precio_base, disponible FROM habitacion ORDER BY numero");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("numero"), rs.getString("tipo"),
                        String.format("$ %.2f", rs.getDouble("precio_base")),
                        "-",
                        rs.getBoolean("disponible") ? "Sí" : "No"
                    });
                }
                rs.close(); ps.close();
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    /** Devuelve la capacidad máxima según el tipo. */
    private int capacidadPorTipo(String tipo) {
        if ("Suite".equals(tipo)) return 6;
        if ("Doble".equals(tipo)) return 2;
        return 1; // Simple
    }

    private void agregarHabitacion() {
        JTextField txtNum = new JTextField();
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Simple", "Doble", "Suite"});

        // Etiquetas informativas (precio y capacidad fijos según tipo)
        JLabel lblPrecio    = new JLabel("$ 50.00  (1 persona)");
        cmbTipo.addActionListener(e -> {
            String t = (String) cmbTipo.getSelectedItem();
            if ("Doble".equals(t))  lblPrecio.setText("$ 80.00  (2 personas)");
            else if ("Suite".equals(t)) lblPrecio.setText("$ 150.00  (6 personas)");
            else                        lblPrecio.setText("$ 50.00  (1 persona)");
        });

        int option = JOptionPane.showConfirmDialog(this, new Object[]{
            "Número de Habitación:", txtNum,
            "Tipo:", cmbTipo,
            "Precio y Capacidad (Fijo):", lblPrecio
        }, "Nueva Habitación", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int num = Integer.parseInt(txtNum.getText().trim());
                String tipo  = (String) cmbTipo.getSelectedItem();
                double precio = "Doble".equals(tipo) ? 80.0 : "Suite".equals(tipo) ? 150.0 : 50.0;
                int cap = capacidadPorTipo(tipo);

                Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
                try {
                    PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO habitacion (numero, tipo, precio_base, capacidad, disponible) VALUES (?, ?, ?, ?, TRUE)");
                    ps.setInt(1, num); ps.setString(2, tipo); ps.setDouble(3, precio); ps.setInt(4, cap);
                    ps.executeUpdate(); ps.close();
                } catch (SQLException ex2) {
                    PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO habitacion (numero, tipo, precio_base, disponible) VALUES (?, ?, ?, TRUE)");
                    ps.setInt(1, num); ps.setString(2, tipo); ps.setDouble(3, precio);
                    ps.executeUpdate(); ps.close();
                }
                JOptionPane.showMessageDialog(this, "Habitación " + num + " (" + tipo + ", cap. " + cap + " pers.) agregada.");
                cargarDatos();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un número de habitación válido.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void editarHabitacion() {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una habitación.");
            return;
        }

        int numero = (int) model.getValueAt(selected, 0);

        // Verificar si tiene reserva EN CURSO
        try {
            Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
            PreparedStatement psCheck = con.prepareStatement(
                "SELECT COUNT(*) FROM detalle_reserva dr " +
                "JOIN reserva r ON r.id = dr.id_reserva " +
                "WHERE dr.numero_habitacion = ? AND r.estado = 'EN CURSO'");
            psCheck.setInt(1, numero);
            ResultSet rsCheck = psCheck.executeQuery();
            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                rsCheck.close(); psCheck.close();
                JOptionPane.showMessageDialog(this,
                    "La habitación " + numero + " tiene un huésped actualmente.\nNo se puede editar mientras esté ocupada.",
                    "Habitación ocupada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            rsCheck.close(); psCheck.close();

            // Cargar datos actuales
            PreparedStatement psHab = con.prepareStatement(
                "SELECT tipo, precio_base, capacidad FROM habitacion WHERE numero = ?");
            psHab.setInt(1, numero);
            ResultSet rsHab = psHab.executeQuery();
            if (!rsHab.next()) { rsHab.close(); psHab.close(); return; }

            String tipoActual   = rsHab.getString("tipo");
            double precioActual = rsHab.getDouble("precio_base");
            int capActual;
            try { capActual = rsHab.getInt("capacidad"); } catch (Exception e) { capActual = 1; }
            rsHab.close(); psHab.close();

            // Formulario de edición
            JComboBox<String> cmbTipo  = new JComboBox<>(new String[]{"Simple", "Doble", "Suite"});
            cmbTipo.setSelectedItem(tipoActual);
            JLabel lblPrecio = new JLabel(String.format("$ %.2f", precioActual));
            JTextField txtCap = new JTextField(String.valueOf(capActual));

            cmbTipo.addActionListener(e -> {
                String t = (String) cmbTipo.getSelectedItem();
                lblPrecio.setText("Doble".equals(t) ? "$ 80.00" : "Suite".equals(t) ? "$ 150.00" : "$ 50.00");
            });

            int option = JOptionPane.showConfirmDialog(this, new Object[]{
                "Habitación Nro:", new JLabel(String.valueOf(numero)),
                "Tipo:", cmbTipo,
                "Precio y Capacidad (Fijo):", lblPrecio
            }, "Editar Habitación " + numero, JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String nuevoTipo  = (String) cmbTipo.getSelectedItem();
                double nuevoPrecio = "Doble".equals(nuevoTipo) ? 80.0 : "Suite".equals(nuevoTipo) ? 150.0 : 50.0;
                int nuevaCap = capacidadPorTipo(nuevoTipo);

                try {
                    PreparedStatement psUpd = con.prepareStatement(
                        "UPDATE habitacion SET tipo = ?, precio_base = ?, capacidad = ? WHERE numero = ?");
                    psUpd.setString(1, nuevoTipo); psUpd.setDouble(2, nuevoPrecio);
                    psUpd.setInt(3, nuevaCap);     psUpd.setInt(4, numero);
                    psUpd.executeUpdate(); psUpd.close();
                } catch (SQLException ex2) {
                    PreparedStatement psUpd = con.prepareStatement(
                        "UPDATE habitacion SET tipo = ?, precio_base = ? WHERE numero = ?");
                    psUpd.setString(1, nuevoTipo); psUpd.setDouble(2, nuevoPrecio); psUpd.setInt(3, numero);
                    psUpd.executeUpdate(); psUpd.close();
                }
                JOptionPane.showMessageDialog(this, "Habitación " + numero + " actualizada.");
                cargarDatos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
