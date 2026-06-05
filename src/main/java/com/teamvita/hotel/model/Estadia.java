package com.teamvita.hotel.model;

import com.teamvita.hotel.model.reserva.Reserva;
import com.teamvita.hotel.model.servicio.Servicio;
import com.teamvita.hotel.model.facturacion.Factura;
import com.teamvita.hotel.model.reserva.DetalleReserva;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Estadia {
    private LocalDate fechaCheckIn;
    private LocalDate fechaCheckOut;
    private double costoTotal;
    private Reserva reserva;
    private List<Servicio> serviciosAdicionales;
    private Factura factura;

    public Estadia(Reserva reserva, LocalDate fechaCheckIn) {
        this.reserva = reserva;
        this.fechaCheckIn = fechaCheckIn;
        this.serviciosAdicionales = new ArrayList<>();
    }

    public void agregarServicio(Servicio servicio) {
        this.serviciosAdicionales.add(servicio);
    }

    public double calcularCostoTotal() {
        double subtotal = 0;
        
        // Sumar costo de habitaciones por noche
        for (DetalleReserva detalle : reserva.getDetalles()) {
            double precioPorNoche = detalle.getHabitacion().obtenerPrecio();
            int noches = detalle.calcularNoches();
            subtotal += (precioPorNoche * noches);
        }
        
        // Sumar servicios adicionales (Decorator)
        for (Servicio servicio : serviciosAdicionales) {
            subtotal += servicio.calcularCosto();
        }
        
        // Aplicar descuento de fidelizacion (Strategy)
        double descuento = reserva.getHuesped().getCategoria().calcularDescuento(subtotal);
        
        this.costoTotal = subtotal - descuento;
        return this.costoTotal;
    }

    public void registrarCheckOut(LocalDate fechaCheckOut) {
        this.fechaCheckOut = fechaCheckOut;
        this.factura = new Factura(calcularCostoTotal());
    }

    public Factura getFactura() { return factura; }
    public LocalDate getFechaCheckIn() { return fechaCheckIn; }
    public LocalDate getFechaCheckOut() { return fechaCheckOut; }
    public double getCostoTotal() { return costoTotal; }
}
