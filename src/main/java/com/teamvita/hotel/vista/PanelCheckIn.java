package com.teamvita.hotel.vista;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PanelCheckIn extends JPanel {

    private javax.swing.table.DefaultTableModel model;
    private JTable table;
    private com.teamvita.hotel.repo.ReservaDAO rDao;

    private JButton btnCheckIn;
    private JButton btnCheckOutFacturar;
    private JButton btnCancelarReserva;
    private JButton btnCargarServicio;

    public PanelCheckIn() {
        rDao = new com.teamvita.hotel.repo.ReservaDAO();
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestión de Reservas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        // Tabla
        String[] columnNames = {"ID", "Huesped", "Fecha Reserva", "Habitacion", "Check-In", "Check-Out", "Estado", "Total", "Senia Abonada"};
        model = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMaxWidth(45);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Reservas Registradas"));
        add(scrollPane, BorderLayout.CENTER);

        // Botones (sin emojis, sin boton Actualizar)
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        btnCheckIn        = new JButton("Realizar Check-In");
        btnCheckOutFacturar = new JButton("Check-Out y Facturar");
        btnCancelarReserva = new JButton("Cancelar Reserva");
        btnCargarServicio = new JButton("Añadir Consumo");

        btnCheckIn.setPreferredSize(new Dimension(160, 38));
        btnCheckOutFacturar.setPreferredSize(new Dimension(180, 38));
        btnCancelarReserva.setPreferredSize(new Dimension(160, 38));
        btnCargarServicio.setPreferredSize(new Dimension(160, 38));

        btnPanel.add(btnCancelarReserva);
        btnPanel.add(btnCheckIn);
        btnPanel.add(btnCargarServicio);
        btnPanel.add(btnCheckOutFacturar);
        add(btnPanel, BorderLayout.SOUTH);

        btnCheckIn.addActionListener(e -> realizarCheckIn());
        btnCheckOutFacturar.addActionListener(e -> realizarCheckOutYFacturar());
        btnCancelarReserva.addActionListener(e -> cancelarReserva());
        btnCargarServicio.addActionListener(e -> cargarServicioAdicional());

        table.getSelectionModel().addListSelectionListener(e -> actualizarEstadoBotones());

        cargarDatos();
        actualizarEstadoBotones();
    }

    public void cargarDatos() {
        model.setRowCount(0);

        // Query simplificada: no usa fecha_checkin/fecha_checkout de reserva (puede no existir)
        // Usa las fechas de detalle_reserva si existen
        String sql =
            "SELECT r.id, h.nombre, r.fecha_creacion, " +
            "  COALESCE(CAST(dr.numero_habitacion AS CHAR), 'N/A') AS habitacion, " +
            "  COALESCE(CAST(dr.fecha_inicio AS CHAR), '-') AS fecha_inicio, " +
            "  COALESCE(CAST(dr.fecha_fin AS CHAR), '-') AS fecha_fin, " +
            "  r.estado, r.total_estimado, " +
            "  COALESCE((SELECT SUM(p.monto) FROM pagos p WHERE p.id_reserva = r.id), 0) AS senia " +
            "FROM reserva r " +
            "JOIN huesped h ON r.id_huesped = h.id " +
            "LEFT JOIN detalle_reserva dr ON dr.id_reserva = r.id " +
            "ORDER BY r.id DESC";

        try {
            Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDate("fecha_creacion"),
                    rs.getString("habitacion"),
                    rs.getString("fecha_inicio"),
                    rs.getString("fecha_fin"),
                    rs.getString("estado"),
                    String.format("$ %.2f", rs.getDouble("total_estimado")),
                    String.format("$ %.2f", rs.getDouble("senia"))
                });
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.err.println("[PanelCheckIn] Error cargando reservas: " + e.getMessage());
            e.printStackTrace();
        }
        actualizarEstadoBotones();
    }

    private void actualizarEstadoBotones() {
        int row = table.getSelectedRow();
        if (row == -1) {
            btnCheckIn.setEnabled(false);
            btnCheckOutFacturar.setEnabled(false);
            return;
        }
        String estado = (String) model.getValueAt(row, 6);
        btnCheckIn.setEnabled("PENDIENTE".equalsIgnoreCase(estado) || "CONFIRMADA".equalsIgnoreCase(estado));
        btnCheckOutFacturar.setEnabled("EN CURSO".equalsIgnoreCase(estado));
        btnCancelarReserva.setEnabled("PENDIENTE".equalsIgnoreCase(estado) || "CONFIRMADA".equalsIgnoreCase(estado));
        btnCargarServicio.setEnabled("EN CURSO".equalsIgnoreCase(estado));
    }

    private void realizarCheckIn() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int idReserva = (int) model.getValueAt(row, 0);
        String huesped = (String) model.getValueAt(row, 1);
        String estado  = (String) model.getValueAt(row, 6);

        if (!"PENDIENTE".equalsIgnoreCase(estado) && !"CONFIRMADA".equalsIgnoreCase(estado)) {
            JOptionPane.showMessageDialog(this, "Esta reserva no puede hacer Check-In en su estado actual.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showOptionDialog(this,
            "Confirmar Check-In para " + huesped + "?\nSe marcara la habitacion como ocupada.",
            "Confirmar Check-In",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
            new String[]{"Sí", "No"}, "Sí");

        if (confirm != 0) return;

        try {
            Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
            // Intentar actualizar con fecha_checkin si la columna existe
            try {
                PreparedStatement ps = con.prepareStatement(
                    "UPDATE reserva SET estado = 'EN CURSO', fecha_checkin = CURDATE() WHERE id = ?");
                ps.setInt(1, idReserva);
                ps.executeUpdate();
                ps.close();
            } catch (Exception ex) {
                // Si fecha_checkin no existe, actualizar solo el estado
                PreparedStatement ps = con.prepareStatement(
                    "UPDATE reserva SET estado = 'EN CURSO' WHERE id = ?");
                ps.setInt(1, idReserva);
                ps.executeUpdate();
                ps.close();
            }
            JOptionPane.showMessageDialog(this,
                "Check-In realizado correctamente.\nHuesped: " + huesped,
                "Check-In Exitoso", JOptionPane.INFORMATION_MESSAGE);
            cargarDatos();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarReserva() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int idReserva = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea cancelar la reserva #" + idReserva + "?", "Cancelar Reserva", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement("UPDATE reserva SET estado = 'CANCELADA' WHERE id = ?");
                ps.setInt(1, idReserva);
                ps.executeUpdate();
                ps.close();
                JOptionPane.showMessageDialog(this, "Reserva cancelada correctamente.");
                cargarDatos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void cargarServicioAdicional() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int idReserva = (int) model.getValueAt(row, 0);
        
        try {
            Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement("SELECT nombre, precio FROM servicio");
            ResultSet rs = ps.executeQuery();
            java.util.List<String> servicios = new java.util.ArrayList<>();
            while(rs.next()){
                servicios.add(rs.getString("nombre"));
            }
            rs.close();
            ps.close();

            if (servicios.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay servicios registrados.");
                return;
            }

            JComboBox<String> cmbServicios = new JComboBox<>(servicios.toArray(new String[0]));
            JSpinner spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
            
            Object[] message = {
                "Servicio:", cmbServicios,
                "Cantidad:", spnCantidad
            };
            
            int opt = JOptionPane.showConfirmDialog(this, message, "Añadir Consumo Extra", JOptionPane.OK_CANCEL_OPTION);
            if (opt == JOptionPane.OK_OPTION) {
                com.teamvita.hotel.repo.ConsumoServicioDAO cDao = new com.teamvita.hotel.repo.ConsumoServicioDAO();
                cDao.registrarConsumo(idReserva, cmbServicios.getSelectedItem().toString(), (int)spnCantidad.getValue());
                JOptionPane.showMessageDialog(this, "Consumo registrado.");
            }
        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void realizarCheckOutYFacturar() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int idReserva = (int) model.getValueAt(row, 0);
        String huesped = (String) model.getValueAt(row, 1);
        String estado  = (String) model.getValueAt(row, 6);

        if (!"EN CURSO".equalsIgnoreCase(estado)) {
            JOptionPane.showMessageDialog(this, "Solo se puede hacer Check-Out de reservas en estado 'EN CURSO'.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();

            // Total estimado (habitación)
            double totalHabitacion = 0;
            PreparedStatement psTotal = con.prepareStatement("SELECT total_estimado FROM reserva WHERE id = ?");
            psTotal.setInt(1, idReserva);
            ResultSet rsTotal = psTotal.executeQuery();
            if (rsTotal.next()) totalHabitacion = rsTotal.getDouble(1);
            rsTotal.close(); psTotal.close();

            // Total Consumos Extras
            com.teamvita.hotel.repo.ConsumoServicioDAO cDao = new com.teamvita.hotel.repo.ConsumoServicioDAO();
            double totalConsumos = cDao.getTotalConsumos(idReserva);
            double totalEstimado = totalHabitacion + totalConsumos;

            // Senia ya abonada
            double seniaAbonada = 0;
            PreparedStatement psPagos = con.prepareStatement("SELECT SUM(monto) FROM pagos WHERE id_reserva = ?");
            psPagos.setInt(1, idReserva);
            ResultSet rsPagos = psPagos.executeQuery();
            if (rsPagos.next()) seniaAbonada = rsPagos.getDouble(1);
            rsPagos.close(); psPagos.close();

            double restoAPagar = Math.max(0, totalEstimado - seniaAbonada);

            // Panel de facturacion
            StringBuilder resumen = new StringBuilder();
            resumen.append("--- FACTURA DE ESTADIA ---\n");
            resumen.append("Huesped: ").append(huesped).append("\n");
            resumen.append("Total Habitacion:    $").append(String.format("%.2f", totalHabitacion)).append("\n");
            if (totalConsumos > 0) {
                resumen.append("Servicios Extra:     $").append(String.format("%.2f", totalConsumos)).append("\n");
            }
            resumen.append("Total de estadia:    $").append(String.format("%.2f", totalEstimado)).append("\n");
            resumen.append("Senia ya abonada:   -$").append(String.format("%.2f", seniaAbonada)).append("\n");
            resumen.append("--------------------------\n");
            resumen.append("SALDO A COBRAR:      $").append(String.format("%.2f", restoAPagar)).append("\n");

            String[] medios = {"Efectivo", "Tarjeta de Credito", "Tarjeta de Debito", "Transferencia"};

            JPanel pnlFactura = new JPanel(new BorderLayout(10, 10));
            JTextArea txtResumen = new JTextArea(resumen.toString());
            txtResumen.setEditable(false);
            txtResumen.setFont(new Font("Monospaced", Font.PLAIN, 13));
            txtResumen.setBackground(UIManager.getColor("Panel.background"));
            txtResumen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            pnlFactura.add(txtResumen, BorderLayout.CENTER);

            JPanel pnlMedio = new JPanel(new FlowLayout(FlowLayout.LEFT));
            pnlMedio.add(new JLabel("Medio de pago del saldo:"));
            JComboBox<String> cmbMedio = new JComboBox<>(medios);
            pnlMedio.add(cmbMedio);
            pnlFactura.add(pnlMedio, BorderLayout.SOUTH);
            pnlFactura.setPreferredSize(new Dimension(420, 220));

            int confirm = JOptionPane.showConfirmDialog(this, pnlFactura,
                "Check-Out y Facturacion - " + huesped,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (confirm != JOptionPane.OK_OPTION) return;

            String medioPago = cmbMedio.getSelectedItem().toString();

            // Registrar pago del saldo si hay algo a cobrar
            if (restoAPagar > 0) {
                PreparedStatement psPago = con.prepareStatement(
                    "INSERT INTO pagos (id_reserva, monto, medio_pago, fecha) VALUES (?, ?, ?, CURDATE())");
                psPago.setInt(1, idReserva);
                psPago.setDouble(2, restoAPagar);
                psPago.setString(3, medioPago);
                psPago.executeUpdate();
                psPago.close();
            }

            // Generar factura usando el DAO
            new com.teamvita.hotel.repo.FacturaDAO().guardarFactura(idReserva, totalEstimado, medioPago);

            // Actualizar estado a FINALIZADA (con o sin fecha_checkout)
            try {
                PreparedStatement psEstado = con.prepareStatement(
                    "UPDATE reserva SET estado = 'FINALIZADA', fecha_checkout = CURDATE() WHERE id = ?");
                psEstado.setInt(1, idReserva);
                psEstado.executeUpdate();
                psEstado.close();
            } catch (Exception ex) {
                PreparedStatement psEstado = con.prepareStatement(
                    "UPDATE reserva SET estado = 'FINALIZADA' WHERE id = ?");
                psEstado.setInt(1, idReserva);
                psEstado.executeUpdate();
                psEstado.close();
            }

            String ticket =
                "CHECK-OUT COMPLETADO\n\n" +
                "Huesped: " + huesped + "\n" +
                "Total cobrado: $" + String.format("%.2f", totalEstimado) + "\n" +
                "Medio de pago del saldo: " + medioPago + "\n\n" +
                "Gracias por su estadia!";
            JOptionPane.showMessageDialog(this, ticket, "Factura Emitida", JOptionPane.INFORMATION_MESSAGE);

            cargarDatos();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al facturar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
