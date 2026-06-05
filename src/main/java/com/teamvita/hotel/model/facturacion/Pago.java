package com.teamvita.hotel.model.facturacion;

import com.teamvita.hotel.model.reserva.Reserva;
import java.time.LocalDate;

public class Pago {
    private int id;
    private double monto;
    private LocalDate fechaPago;
    private String metodoPago;
    private Reserva reserva;

    public Pago(double monto, LocalDate fechaPago, String metodoPago, Reserva reserva) {
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.metodoPago = metodoPago;
        this.reserva = reserva;
    }

    public double getMonto() { return monto; }
    public LocalDate getFechaPago() { return fechaPago; }
    public String getMetodoPago() { return metodoPago; }
    
    public void registrarPago() {
        reserva.agregarPago(this);
    }
}
