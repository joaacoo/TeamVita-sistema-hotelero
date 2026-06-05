package com.teamvita.hotel.model.reserva;

public interface EstadoReserva {
    void cambiarEstado(Reserva reserva, EstadoReserva nuevoEstado);
    String getNombreEstado();
}
