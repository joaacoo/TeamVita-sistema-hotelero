package com.teamvita.hotel.vista;

import javax.swing.*;
import java.awt.*;

public class PanelPromociones extends JPanel {
    private javax.swing.table.DefaultTableModel model;
    private JTable table;
    private com.teamvita.hotel.repo.PromocionDAO promocionDAO;

    public PanelPromociones() {
        promocionDAO = new com.teamvita.hotel.repo.PromocionDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Título principal
        JLabel title = new JLabel("Gestión de Promociones", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Tabla
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        String[] columnNames = {"ID", "Nombre Promoción", "Descuento (%)"};
        model = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Botonera
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdd = new JButton("Nueva Promoción");
        JButton btnDelete = new JButton("Eliminar Seleccionada");
        
        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);
        centerPanel.add(btnPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> agregarPromocion());
        btnDelete.addActionListener(e -> eliminarPromocion());

        cargarDatos();
    }

    private void cargarDatos() {
        model.setRowCount(0);
        java.util.List<Object[]> promos = promocionDAO.obtenerPromocionesParaTabla();
        for (Object[] row : promos) {
            model.addRow(row);
        }
    }

    private void agregarPromocion() {
        JPanel pnl = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField txtNombre = new JTextField();
        JTextField txtDesc = new JTextField();
        pnl.add(new JLabel("Nombre Promoción:"));
        pnl.add(txtNombre);
        pnl.add(new JLabel("Descuento (Ej: 15.5):"));
        pnl.add(txtDesc);

        int res = JOptionPane.showConfirmDialog(this, pnl, "Nueva Promoción", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                double descuento = Double.parseDouble(txtDesc.getText().replace(",", "."));
                boolean ok = promocionDAO.agregarPromocion(txtNombre.getText(), descuento);
                if(ok) cargarDatos();
                else JOptionPane.showMessageDialog(this, "No se pudo agregar. Tal vez el nombre ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Descuento inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarPromocion() {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una promoción.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) model.getValueAt(selected, 0);
        String nombre = (String) model.getValueAt(selected, 1);
        
        if ("Sin Promoción".equals(nombre)) {
            JOptionPane.showMessageDialog(this, "No puedes eliminar 'Sin Promoción'.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar promoción " + nombre + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            promocionDAO.eliminarPromocion(id);
            cargarDatos();
        }
    }
}
