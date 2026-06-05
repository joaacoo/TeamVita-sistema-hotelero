package com.teamvita.hotel.model.reserva;

public class ReservaConfirmada implements EstadoReserva {
    @Override
    public void cambiarEstado(Reserva reserva, EstadoReserva nuevoEstado) {
        if (nuevoEstado instanceof ReservaFinalizada || nuevoEstado instanceof ReservaCancelada) {
            reserva.cambiarEstado(nuevoEstado);
        } else {
            throw new IllegalStateException("Transición inválida desde Confirmada a " + nuevoEstado.getNombreEstado());
        }
    }

    @Override
    public String getNombreEstado() { return "CONFIRMADA"; }
}
