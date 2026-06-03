package com.teamvita.hotel.model;

public class LavanderiaDecorator extends ServicioDecorator {
    public LavanderiaDecorator(Servicio servicio) {
        super(servicio);
    }

    @Override
    public double calcularCosto() {
        return servicio.calcularCosto() + 15.0; // Precio de ejemplo
    }
}
