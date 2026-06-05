package com.teamvita.hotel.negocio;

import com.teamvita.hotel.model.Huesped;
import com.teamvita.hotel.model.habitacion.Habitacion;
import com.teamvita.hotel.model.reserva.Reserva;
import com.teamvita.hotel.model.reserva.DetalleReserva;
import com.teamvita.hotel.model.reserva.EstadoReserva;
import com.teamvita.hotel.model.Estadia;
import java.time.LocalDate;
import java.util.List;

public class SistemaHotelFacade {
    private HabitacionFactory habitacionFactory;

    public SistemaHotelFacade() {
        this.habitacionFactory = new HabitacionFactory();
    }

    public void registrarReserva(Huesped huesped, List<Habitacion> habitaciones, LocalDate checkIn, LocalDate checkOut, int cantPersonas) {
        Reserva reserva = new Reserva(huesped);
        for(Habitacion h : habitaciones) {
            DetalleReserva detalle = new DetalleReserva(h, checkIn, checkOut, cantPersonas);
            reserva.agregarDetalle(detalle);
        }
        System.out.println("Reserva registrada con éxito.");
    }

    public void realizarCheckIn(Reserva reserva) {
        if ("CONFIRMADA".equals(reserva.getEstado().getNombreEstado())) {
            for (DetalleReserva detalle : reserva.getDetalles()) {
                detalle.getHabitacion().setDisponible(false);
            }
            Estadia estadia = new Estadia(reserva, LocalDate.now());
            reserva.setEstadia(estadia);
            System.out.println("Check-in realizado exitosamente.");
        } else {
            System.out.println("Error: La reserva no está en estado válido para check-in.");
        }
    }
}
