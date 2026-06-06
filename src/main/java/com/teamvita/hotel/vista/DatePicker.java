package com.teamvita.hotel.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class DatePicker {
    private JDialog dialog;
    private String selectedDate = "";
    private int month = LocalDate.now().getMonthValue();
    private int year = LocalDate.now().getYear();
    private JLabel l = new JLabel("", JLabel.CENTER);
    private String day = "";
    private JButton[] button = new JButton[49];

    public DatePicker(JFrame parent) {
        dialog = new JDialog(parent, "Seleccionar Fecha", true);
        dialog.setLayout(new BorderLayout());
        JPanel p1 = new JPanel(new GridLayout(7, 7));
        String[] header = { "Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa" };
        for (int i = 0; i < 7; i++) {
            p1.add(new JLabel(header[i], JLabel.CENTER));
        }
        for (int i = 0; i < 42; i++) {
            final int selection = i;
            button[i] = new JButton("");
            button[i].setFont(new Font("Arial", Font.BOLD, 16));
            button[i].setFocusPainted(false);
            button[i].setBackground(Color.white);
            button[i].addActionListener(e -> {
                if (!button[selection].getText().isEmpty()) {
                    day = button[selection].getText();
                    selectedDate = String.format("%04d-%02d-%02d", year, month, Integer.parseInt(day));
                    dialog.dispose();
                }
            });
            p1.add(button[i]);
        }
        
        JPanel p2 = new JPanel(new GridLayout(1, 3));
        JButton previous = new JButton("<<");
        previous.addActionListener(e -> { month--; if (month == 0) { month = 12; year--; } displayDate(); });
        p2.add(previous);
        p2.add(l);
        JButton next = new JButton(">>");
        next.addActionListener(e -> { month++; if (month == 13) { month = 1; year++; } displayDate(); });
        p2.add(next);
        
        dialog.add(p1, BorderLayout.CENTER);
        dialog.add(p2, BorderLayout.SOUTH);
        dialog.setSize(500, 400);
        displayDate();
    }

    private void displayDate() {
        for (int i = 0; i < 42; i++) button[i].setText("");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(year, month - 1, 1);
        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        for (int i = 0; i < daysInMonth; i++) {
            button[i + dayOfWeek - 1].setText(String.valueOf(i + 1));
        }
        l.setText(month + " / " + year);
    }

    public String setPickedDate() {
        if (day.isEmpty()) return "";
        return selectedDate;
    }
    
    public void showDialog() {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
