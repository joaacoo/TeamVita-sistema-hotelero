package com.teamvita.hotel.model.reserva;

import com.teamvita.hotel.model.habitacion.Habitacion;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DetalleReserva {
    private Habitacion habitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int cantidadPersonas;

    public DetalleReserva(Habitacion habitacion, LocalDate fechaInicio, LocalDate fechaFin, int cantidadPersonas) {
        this.habitacion = habitacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cantidadPersonas = cantidadPersonas;
    }

    public int calcularNoches() {
        return (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }

    public Habitacion getHabitacion() { return habitacion; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
}
