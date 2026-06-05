package com.teamvita.hotel.model.reserva;

public class ReservaFinalizada implements EstadoReserva {
    @Override
    public void cambiarEstado(Reserva reserva, EstadoReserva nuevoEstado) {
        throw new IllegalStateException("Una reserva finalizada no puede cambiar de estado.");
    }

    @Override
    public String getNombreEstado() { return "FINALIZADA"; }
}
