package com.teamvita.hotel.vista;

import com.teamvita.hotel.negocio.SistemaHotelFacade;
import com.teamvita.hotel.model.fidelizacion.*;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PanelReserva extends JPanel {
    private SistemaHotelFacade facade;
    private JTabbedPane parentTabbedPane;

    class HabitacionItem {
        int numero;
        String tipo;
        double precio;
        int capacidad;
        public HabitacionItem(int numero, String tipo, double precio, int capacidad) {
            this.numero = numero;
            this.tipo = tipo;
            this.precio = precio;
            this.capacidad = capacidad;
        }
        @Override
        public String toString() {
            return numero + " - " + tipo + " ($" + precio + " | cap: " + capacidad + ")";
        }
    }

    class PromocionItem {
        int id;
        String nombre;
        double descuento;
        public PromocionItem(int id, String nombre, double descuento) {
            this.id = id;
            this.nombre = nombre;
            this.descuento = descuento;
        }
        @Override
        public String toString() {
            return nombre + (descuento > 0 ? " (-" + descuento + "%)" : "");
        }
    }

    public PanelReserva(JTabbedPane parentTabbedPane) {
        this.parentTabbedPane = parentTabbedPane;
        this.facade = new SistemaHotelFacade();
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Registrar Nueva Reserva", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 0: DNI
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("DNI Huésped (Máx 8):"), gbc);
        gbc.gridx = 1;
        JTextField txtDni = new JTextField(15);
        aplicarFiltroDni(txtDni);
        formPanel.add(txtDni, gbc);

        // Fila 1: Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1;
        JTextField txtNombre = new JTextField(15);
        formPanel.add(txtNombre, gbc);

        // Fila 2: Email
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Correo Electrónico:"), gbc);
        gbc.gridx = 1;
        JTextField txtEmail = new JTextField(15);
        formPanel.add(txtEmail, gbc);

        // Fila 3: Telefono
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        JTextField txtTelefono = new JTextField(15);
        formPanel.add(txtTelefono, gbc);

        // Fila 4: Categoria Fidelización
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Categoría Fidelización:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cmbFidelizacion = new JComboBox<>(new String[]{"Clásica", "Gold", "Platinum"});
        cmbFidelizacion.setPreferredSize(new Dimension(165, 25));
        formPanel.add(cmbFidelizacion, gbc);

        // Fila 5: Check-In
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Check-In:"), gbc);
        gbc.gridx = 1;
        JPanel pnlCheckIn = new JPanel(new BorderLayout());
        JTextField txtCheckIn = new JTextField(10);
        txtCheckIn.setEditable(false);
        JButton btnCalIn = new JButton("...");
        btnCalIn.addActionListener(e -> {
            DatePicker dp = new DatePicker((JFrame) SwingUtilities.getWindowAncestor(this));
            dp.showDialog();
            txtCheckIn.setText(dp.setPickedDate());
        });
        pnlCheckIn.add(txtCheckIn, BorderLayout.CENTER);
        pnlCheckIn.add(btnCalIn, BorderLayout.EAST);
        formPanel.add(pnlCheckIn, gbc);

        // Fila 6: Check-Out
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Check-Out:"), gbc);
        gbc.gridx = 1;
        JPanel pnlCheckOut = new JPanel(new BorderLayout());
        JTextField txtCheckOut = new JTextField(10);
        txtCheckOut.setEditable(false);
        JButton btnCalOut = new JButton("...");
        btnCalOut.addActionListener(e -> {
            DatePicker dp = new DatePicker((JFrame) SwingUtilities.getWindowAncestor(this));
            dp.showDialog();
            txtCheckOut.setText(dp.setPickedDate());
        });
        pnlCheckOut.add(txtCheckOut, BorderLayout.CENTER);
        pnlCheckOut.add(btnCalOut, BorderLayout.EAST);
        formPanel.add(pnlCheckOut, gbc);

        // Fila 7: Habitación Disponible
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Habitación Disponible:"), gbc);
        gbc.gridx = 1;
        JComboBox<HabitacionItem> cmbHabitacion = new JComboBox<>();
        cmbHabitacion.setPreferredSize(new Dimension(200, 25));
        cargarHabitacionesDisponibles(cmbHabitacion);
        formPanel.add(cmbHabitacion, gbc);

        // Fila 8: Cantidad de personas
        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("Cantidad de Personas:"), gbc);
        gbc.gridx = 1;
        JSpinner spnPersonas = new JSpinner(new SpinnerNumberModel(1, 1, 6, 1));
        spnPersonas.setPreferredSize(new Dimension(80, 25));
        formPanel.add(spnPersonas, gbc);

        // Ya no hay lblCapacidad ni se cambia el max del spinner dinámicamente
        cmbHabitacion.addActionListener(e -> {
            // El spinner queda con máximo 6, la validación se hace al guardar.
        });

        // Fila 9: Promoción
        gbc.gridx = 0; gbc.gridy = 9;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Promoción:"), gbc);
        gbc.gridx = 1;
        JComboBox<PromocionItem> cmbPromocion = new JComboBox<>();
        cmbPromocion.setPreferredSize(new Dimension(200, 25));
        cargarPromocionesDisponibles(cmbPromocion);
        formPanel.add(cmbPromocion, gbc);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(formPanel);
        add(wrapper, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Confirmar reserva");
        btnGuardar.setPreferredSize(new Dimension(220, 40));
        btnGuardar.addActionListener(e -> {
            // Validaciones básicas
            if (txtDni.getText().isEmpty() || txtNombre.getText().isEmpty()
                    || txtCheckIn.getText().isEmpty() || txtCheckOut.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (cmbHabitacion.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "No hay habitaciones disponibles para reservar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                java.time.LocalDate dIn  = java.time.LocalDate.parse(txtCheckIn.getText());
                java.time.LocalDate dOut = java.time.LocalDate.parse(txtCheckOut.getText());
                long days = java.time.temporal.ChronoUnit.DAYS.between(dIn, dOut);
                if (days <= 0) {
                    JOptionPane.showMessageDialog(this, "Check-Out debe ser posterior al Check-In.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                HabitacionItem habItem = (HabitacionItem) cmbHabitacion.getSelectedItem();
                int cantPersonas = (int) spnPersonas.getValue();

                // Validar capacidad fija por tipo
                if (cantPersonas > capacidadPorTipo(habItem.tipo)) {
                    JOptionPane.showMessageDialog(this,
                        "La habitación " + habItem.numero + " (" + habItem.tipo + ") admite máximo "
                        + capacidadPorTipo(habItem.tipo) + " persona(s).\nNo puede reservar para " + cantPersonas + ".",
                        "Capacidad excedida", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double subTotal = days * habItem.precio;

                // Fidelización
                String catName = cmbFidelizacion.getSelectedItem().toString();
                CategoriaFidelizacion estrategia;
                if ("Gold".equals(catName)) estrategia = new CategoriaGold();
                else if ("Platinum".equals(catName)) estrategia = new CategoriaPlatinum();
                else estrategia = new CategoriaClasica();
                double descuentoFidelizacion = estrategia.calcularDescuento(subTotal);

                // Promoción
                PromocionItem promoSel = (PromocionItem) cmbPromocion.getSelectedItem();
                double descuentoPromocion = 0.0;
                if (promoSel != null) descuentoPromocion = subTotal * (promoSel.descuento / 100.0);

                double montoTotal = Math.max(0, subTotal - descuentoFidelizacion - descuentoPromocion);

                // Porcentaje de seña desde BD
                int seniaPorcentaje = 30;
                try {
                    Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
                    PreparedStatement ps = con.prepareStatement("SELECT valor FROM configuracion WHERE clave = 'senia_porcentaje'");
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) seniaPorcentaje = Integer.parseInt(rs.getString(1));
                    rs.close(); ps.close();
                } catch (Exception ex2) { /* usa default 30 */ }

                double seniaCalculada = montoTotal * seniaPorcentaje / 100.0;

                // Resumen inmodificable
                StringBuilder resumen = new StringBuilder();
                resumen.append("--- DETALLE DE RESERVA ---\n");
                resumen.append("Huésped: ").append(txtNombre.getText()).append(" (DNI: ").append(txtDni.getText()).append(")\n");
                resumen.append("Estadía: ").append(days).append(" noche(s) | ").append(dIn).append(" → ").append(dOut).append("\n");
                resumen.append("Habitación: ").append(habItem.numero).append(" - ").append(habItem.tipo).append("\n");
                resumen.append("Cantidad de personas: ").append(cantPersonas).append("\n");
                resumen.append("Subtotal: $").append(String.format("%.2f", subTotal)).append("\n");
                resumen.append("Descuento Fidelización (").append(catName).append("): -$").append(String.format("%.2f", descuentoFidelizacion)).append("\n");
                if (promoSel != null && promoSel.descuento > 0) {
                    resumen.append("Descuento Promoción (").append(promoSel.nombre).append("): -$").append(String.format("%.2f", descuentoPromocion)).append("\n");
                }
                resumen.append("--------------------------\n");
                resumen.append("MONTO TOTAL: $").append(String.format("%.2f", montoTotal)).append("\n");
                resumen.append("SEÑA (").append(seniaPorcentaje).append("%): $").append(String.format("%.2f", seniaCalculada)).append("\n");

                JTextArea txtArea = new JTextArea(resumen.toString());
                txtArea.setEditable(false);
                txtArea.setBackground(UIManager.getColor("Panel.background"));
                txtArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
                txtArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

                JScrollPane sp = new JScrollPane(txtArea);
                sp.setBorder(BorderFactory.createEmptyBorder());
                sp.setPreferredSize(new Dimension(480, 240));

                int option = JOptionPane.showConfirmDialog(this, sp, "Confirmar Reserva",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (option != JOptionPane.OK_OPTION) return;

                String[] medios = {"Efectivo", "Tarjeta de Crédito", "Tarjeta de Débito", "Transferencia"};
                String medio = (String) JOptionPane.showInputDialog(this,
                        "Medio de pago para la seña:", "Medio de Pago",
                        JOptionPane.QUESTION_MESSAGE, null, medios, medios[0]);
                if (medio == null) return;

                // Guardar en BD
                facade.registrarReservaCompleta(
                    txtDni.getText(), txtNombre.getText(), txtEmail.getText(), txtTelefono.getText(), catName,
                    dIn, dOut, habItem.tipo, habItem.numero, montoTotal, seniaCalculada, medio
                );

                JOptionPane.showMessageDialog(this, "Reserva guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Limpiar
                txtDni.setText(""); txtNombre.setText(""); txtEmail.setText(""); txtTelefono.setText("");
                txtCheckIn.setText(""); txtCheckOut.setText("");
                cmbFidelizacion.setSelectedIndex(0);
                spnPersonas.setValue(1);
                cargarHabitacionesDisponibles(cmbHabitacion);
                cargarPromocionesDisponibles(cmbPromocion);


            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        btnPanel.add(btnGuardar);
        add(btnPanel, BorderLayout.SOUTH);
    }

    /**
     * Carga habitaciones DISPONIBLES (disponible=TRUE y no tiene reserva EN CURSO).
     * Excluye habitaciones actualmente ocupadas.
     */
    /** Capacidad máxima fija por tipo de habitación. */
    private int capacidadPorTipo(String tipo) {
        if ("Suite".equals(tipo)) return 6;
        if ("Doble".equals(tipo)) return 2;
        return 1; // Simple
    }

    private void cargarHabitacionesDisponibles(JComboBox<HabitacionItem> cmb) {
        cmb.removeAllItems();
        try {
            Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
            // Excluir habitaciones que tengan una reserva EN CURSO
            String sql =
                "SELECT h.numero, h.tipo, h.precio_base, " +
                "COALESCE(h.capacidad, 0) AS capacidad " +
                "FROM habitacion h " +
                "WHERE h.disponible = TRUE " +
                "AND h.numero NOT IN (" +
                "  SELECT dr.numero_habitacion FROM detalle_reserva dr " +
                "  JOIN reserva r ON r.id = dr.id_reserva " +
                "  WHERE r.estado IN ('PENDIENTE', 'CONFIRMADA', 'EN CURSO')" +
                ") " +
                "ORDER BY h.numero";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cmb.addItem(new HabitacionItem(
                    rs.getInt("numero"),
                    rs.getString("tipo"),
                    rs.getDouble("precio_base"),
                    rs.getInt("capacidad")
                ));
            }
            rs.close(); ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cargarPromocionesDisponibles(JComboBox<PromocionItem> cmb) {
        cmb.removeAllItems();
        try {
            Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(
                "SELECT id, nombre, descuento_porcentaje FROM promocion ORDER BY descuento_porcentaje");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cmb.addItem(new PromocionItem(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("descuento_porcentaje")));
            }
            rs.close(); ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void aplicarFiltroDni(JTextField txt) {
        ((AbstractDocument) txt.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String newStr = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if (newStr.matches("\\d{0,8}")) super.replace(fb, offset, length, text, attrs);
            }
        });
    }
}
