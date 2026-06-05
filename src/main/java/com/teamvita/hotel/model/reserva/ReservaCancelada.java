package com.teamvita.hotel.model.reserva;

public class ReservaCancelada implements EstadoReserva {
    @Override
    public void cambiarEstado(Reserva reserva, EstadoReserva nuevoEstado) {
        throw new IllegalStateException("Una reserva cancelada no puede cambiar de estado.");
    }

    @Override
    public String getNombreEstado() { return "CANCELADA"; }
}
