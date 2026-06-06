package com.teamvita.hotel.vista;

import com.teamvita.hotel.negocio.SistemaHotelFacade;
import javax.swing.*;
import java.awt.*;

public class PanelCheckIn extends JPanel {
    private SistemaHotelFacade facade;

    public PanelCheckIn() {
        this.facade = new SistemaHotelFacade();
        setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("Realizar Check-In", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        formPanel.add(new JLabel("Número de Reserva:"));
        JTextField txtReserva = new JTextField(15);
        formPanel.add(txtReserva);
        
        JButton btnBuscar = new JButton("Buscar y Hacer Check-In");
        btnBuscar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Check-in realizado para la reserva " + txtReserva.getText(), "Check-In", JOptionPane.INFORMATION_MESSAGE);
        });
        formPanel.add(btnBuscar);

        add(formPanel, BorderLayout.NORTH);

        // Cargar tabla real de la base de datos
        String[] columnNames = {"ID Reserva", "Huésped", "Fecha Creación", "Check-Out", "Estado", "Total"};
        com.teamvita.hotel.repo.ReservaDAO rDao = new com.teamvita.hotel.repo.ReservaDAO();
        java.util.List<Object[]> rows = rDao.obtenerReservasParaTabla();
        
        Object[][] data = new Object[rows.size()][];
        for(int i = 0; i < rows.size(); i++){
            data[i] = rows.get(i);
        }
        
        JTable table = new JTable(new javax.swing.table.DefaultTableModel(data, columnNames));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Todas las Reservas (Huéspedes)"));
        
        add(scrollPane, BorderLayout.CENTER);
    }
}
