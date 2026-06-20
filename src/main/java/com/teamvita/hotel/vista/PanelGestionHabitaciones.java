package com.teamvita.hotel.vista;

import javax.swing.*;
import java.awt.*;

public class PanelGestionHabitaciones extends JPanel {
    private JTable table;
    private javax.swing.table.DefaultTableModel model;
    private com.teamvita.hotel.negocio.SistemaHotelFacade facade;

    public PanelGestionHabitaciones() {
        this.facade = new com.teamvita.hotel.negocio.SistemaHotelFacade();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Gestión de Habitaciones y Tarifas", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        String[] columnNames = {"Número", "Tipo", "Precio Base", "Capacidad"};
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
        java.util.List<Object[]> datos = facade.obtenerTodasHabitaciones();
        for (Object[] fila : datos) {
            model.addRow(fila);
        }
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

                facade.agregarHabitacion(num, tipo, precio, cap);

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
            if (facade.isHabitacionOcupada(numero)) {
                JOptionPane.showMessageDialog(this,
                    "La habitación " + numero + " tiene un huésped actualmente.\nNo se puede editar mientras esté ocupada.",
                    "Habitación ocupada", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Cargar datos actuales
            Object[] datosActuales = facade.getHabitacionData(numero);
            if (datosActuales == null) return;

            String tipoActual   = (String) datosActuales[0];
            double precioActual = (Double) datosActuales[1];
            int capActual = (Integer) datosActuales[2];

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

                facade.editarHabitacion(numero, nuevoTipo, nuevoPrecio, nuevaCap);

                JOptionPane.showMessageDialog(this, "Habitación " + numero + " actualizada.");
                cargarDatos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
