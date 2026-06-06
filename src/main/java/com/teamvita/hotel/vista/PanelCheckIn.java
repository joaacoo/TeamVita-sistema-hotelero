package com.teamvita.hotel.vista;

import javax.swing.*;
import java.awt.*;

public class PanelCheckIn extends JPanel {

    public PanelCheckIn() {
        setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("Gestión de Check-In / Check-Out", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        // Cargar tabla real de la base de datos
        String[] columnNames = {"ID Reserva", "Huésped", "Fecha Creación", "Check-Out", "Estado", "Total"};
        com.teamvita.hotel.repo.ReservaDAO rDao = new com.teamvita.hotel.repo.ReservaDAO();
        java.util.List<Object[]> rows = rDao.obtenerReservasParaTabla();
        
        Object[][] data = new Object[rows.size()][];
        for(int i = 0; i < rows.size(); i++){
            data[i] = rows.get(i);
        }
        
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo la columna "Estado" (índice 4) es editable
                return column == 4;
            }
        };
        
        model.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column == 4) { // Si se editó el estado manualmente
                    String nuevoEstado = (String) model.getValueAt(row, column);
                    int idReserva = (int) model.getValueAt(row, 0);
                    try {
                        com.teamvita.hotel.repo.ReservaDAO rDaoLocal = new com.teamvita.hotel.repo.ReservaDAO();
                        rDaoLocal.actualizarEstado(idReserva, nuevoEstado);
                        JOptionPane.showMessageDialog(this, "Estado actualizado en BD a: " + nuevoEstado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error al actualizar estado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Todas las Reservas (Huéspedes)"));
        
        add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        JButton btnAccion = new JButton("Realizar Check-In / Check-Out");
        btnAccion.setPreferredSize(new Dimension(250, 40));
        btnAccion.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una reserva de la tabla haciendo click en ella.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int idReserva = (int) model.getValueAt(selectedRow, 0);
            String estadoActual = (String) model.getValueAt(selectedRow, 4);
            
            try {
                if (estadoActual == null || estadoActual.equalsIgnoreCase("PENDIENTE") || estadoActual.equalsIgnoreCase("CONFIRMADA")) {
                    String[] medios = {"Efectivo", "Tarjeta de Crédito", "Tarjeta de Débito", "Transferencia"};
                    String medio = (String) JOptionPane.showInputDialog(this, "Check-In: Seleccione el medio de pago para la seña:", "Medio de Pago", JOptionPane.QUESTION_MESSAGE, null, medios, medios[0]);
                    
                    if (medio != null) {
                        rDao.actualizarEstado(idReserva, "EN CURSO");
                        model.setValueAt("EN CURSO", selectedRow, 4);
                        JOptionPane.showMessageDialog(this, "Check-in realizado exitosamente.\nMedio de pago registrado: " + medio, "Check-In", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (estadoActual.equalsIgnoreCase("EN CURSO")) {
                    rDao.actualizarEstado(idReserva, "FINALIZADA");
                    model.setValueAt("FINALIZADA", selectedRow, 4);
                    JOptionPane.showMessageDialog(this, "Check-Out realizado exitosamente. La reserva ha finalizado.", "Check-Out", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "La reserva ya está en estado: " + estadoActual + " y no se puede realizar Check-in o Check-out.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar la reserva: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnPanel.add(btnAccion);
        add(btnPanel, BorderLayout.SOUTH);
    }
}
