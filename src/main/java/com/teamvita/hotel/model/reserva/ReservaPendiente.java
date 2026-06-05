package com.teamvita.hotel.model.reserva;

public class ReservaPendiente implements EstadoReserva {
    @Override
    public void cambiarEstado(Reserva reserva, EstadoReserva nuevoEstado) {
        if (nuevoEstado instanceof ReservaConfirmada || nuevoEstado instanceof ReservaCancelada) {
            reserva.cambiarEstado(nuevoEstado);
        } else {
            throw new IllegalStateException("Transición inválida desde Pendiente a " + nuevoEstado.getNombreEstado());
        }
    }
    
    @Override
    public String getNombreEstado() { return "PENDIENTE"; }
}
