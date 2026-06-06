package com.teamvita.hotel.model.reserva;

import com.teamvita.hotel.model.Huesped;
import com.teamvita.hotel.model.Estadia;
import com.teamvita.hotel.model.facturacion.Pago;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
    private int id;
    private LocalDate fechaCreacion;
    private Huesped huesped;
    private EstadoReserva estado;
    private List<DetalleReserva> detalles;
    private List<Pago> pagos;
    private Estadia estadia;

    public Reserva(Huesped huesped) {
        this.huesped = huesped;
        this.fechaCreacion = LocalDate.now();
        this.estado = new ReservaPendiente();
        this.detalles = new ArrayList<>();
        this.pagos = new ArrayList<>();
    }

    public void agregarDetalle(DetalleReserva detalle) {
        this.detalles.add(detalle);
    }
    
    public void agregarPago(Pago pago) {
        this.pagos.add(pago);
    }

    public void cambiarEstado(EstadoReserva nuevoEstado) {
        this.estado = nuevoEstado;
    }
    
    public void confirmarReserva() {
        this.estado.cambiarEstado(this, new ReservaConfirmada());
    }
    
    public void cancelarReserva() {
        this.estado.cambiarEstado(this, new ReservaCancelada());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public List<DetalleReserva> getDetalles() { return detalles; }
    public Huesped getHuesped() { return huesped; }
    public EstadoReserva getEstado() { return estado; }
    public void setEstadia(Estadia estadia) { this.estadia = estadia; }
    public Estadia getEstadia() { return estadia; }
}
