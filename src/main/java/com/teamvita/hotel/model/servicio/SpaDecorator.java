package com.teamvita.hotel.model.servicio;

import com.teamvita.hotel.model.*;
import com.teamvita.hotel.model.habitacion.*;
import com.teamvita.hotel.model.reserva.*;
import com.teamvita.hotel.model.servicio.*;
import com.teamvita.hotel.model.fidelizacion.*;
import com.teamvita.hotel.model.facturacion.*;

public class SpaDecorator extends ServicioDecorator {
    public SpaDecorator(Servicio servicio) {
        super(servicio);
    }

    @Override
    public double calcularCosto() {
        return servicio.calcularCosto() + 50.0; // Precio de ejemplo
    }
}
