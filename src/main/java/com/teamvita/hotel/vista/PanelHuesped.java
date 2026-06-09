package com.teamvita.hotel.vista;

import com.teamvita.hotel.repo.HuespedDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelHuesped extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private HuespedDAO hDao;

    private JButton btnEditar;

    public PanelHuesped() {
        hDao = new HuespedDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Gestión de Huéspedes", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        String[] columnNames = { "ID", "DNI", "Nombre", "Email", "Teléfono", "Categoría" };
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnEditar = new JButton("Editar Seleccionado");

        pnlBotones.add(btnEditar);
        add(pnlBotones, BorderLayout.SOUTH);

        btnEditar.addActionListener(e -> editarHuesped());
        table.getSelectionModel().addListSelectionListener(e -> btnEditar.setEnabled(table.getSelectedRow() != -1));

        cargarDatos();
    }

    public void cargarDatos() {
        model.setRowCount(0);
        List<Object[]> datos = hDao.obtenerTodos();
        for (Object[] row : datos) {
            model.addRow(row);
        }
        btnEditar.setEnabled(table.getSelectedRow() != -1);
    }

    private void editarHuesped() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        int id = (int) model.getValueAt(row, 0);
        String dni = (String) model.getValueAt(row, 1);
        String nombre = (String) model.getValueAt(row, 2);
        String email = (String) model.getValueAt(row, 3);
        String tel = (String) model.getValueAt(row, 4);
        String cat = (String) model.getValueAt(row, 5);

        JTextField txtDni = new JTextField(dni);
        JTextField txtNombre = new JTextField(nombre);
        JTextField txtEmail = new JTextField(email);
        JTextField txtTel = new JTextField(tel);
        JComboBox<String> cmbCat = new JComboBox<>(new String[] { "Clásica", "Gold", "Platinum" });
        cmbCat.setSelectedItem(cat);

        Object[] message = {
                "DNI:", txtDni,
                "Nombre:", txtNombre,
                "Email:", txtEmail,
                "Teléfono:", txtTel,
                "Categoría Fidelización:", cmbCat
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Editar Huésped", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                hDao.actualizarHuesped(id, txtDni.getText(), txtNombre.getText(), txtEmail.getText(), txtTel.getText(),
                        cmbCat.getSelectedItem().toString());
                JOptionPane.showMessageDialog(this, "Huésped actualizado correctamente.");
                cargarDatos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
            }
        }
    }
}
