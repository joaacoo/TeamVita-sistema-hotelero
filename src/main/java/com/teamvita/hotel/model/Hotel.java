package com.teamvita.hotel.model;

import com.teamvita.hotel.model.habitacion.Habitacion;
import com.teamvita.hotel.model.reserva.Reserva;
import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private String nombre;
    private String direccion;
    private List<Habitacion> habitaciones;
    private List<Reserva> reservas;

    public Hotel(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.habitaciones = new ArrayList<>();
        this.reservas = new ArrayList<>();
    }

    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public List<Habitacion> getHabitaciones() { return habitaciones; }
    public List<Reserva> getReservas() { return reservas; }
}
