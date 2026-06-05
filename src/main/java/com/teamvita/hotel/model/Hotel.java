package com.teamvita.hotel.model;

import com.teamvita.hotel.model.habitacion.*;
import com.teamvita.hotel.model.reserva.*;
import com.teamvita.hotel.model.servicio.*;
import com.teamvita.hotel.model.fidelizacion.*;
import com.teamvita.hotel.model.facturacion.*;

import java.util.List;

public class Hotel {
    private String nombre;
    private String direccion;
    private List<Habitacion> habitaciones;
    private List<Reserva> reservas;
}
