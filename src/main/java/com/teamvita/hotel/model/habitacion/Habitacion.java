package com.teamvita.hotel.model.habitacion;

import com.teamvita.hotel.model.*;
import com.teamvita.hotel.model.habitacion.*;
import com.teamvita.hotel.model.reserva.*;
import com.teamvita.hotel.model.servicio.*;
import com.teamvita.hotel.model.fidelizacion.*;
import com.teamvita.hotel.model.facturacion.*;

public abstract class Habitacion {
    private int numero;
    private double precioBase;
    private boolean disponible;

    public abstract double obtenerPrecio();
}
