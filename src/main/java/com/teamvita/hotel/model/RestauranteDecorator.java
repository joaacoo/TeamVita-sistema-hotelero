package com.teamvita.hotel.model;

public class RestauranteDecorator extends ServicioDecorator {
    public RestauranteDecorator(Servicio servicio) {
        super(servicio);
    }

    @Override
    public double calcularCosto() {
        return servicio.calcularCosto() + 30.0; // Precio de ejemplo
    }
}
