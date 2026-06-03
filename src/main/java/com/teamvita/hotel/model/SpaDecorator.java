package com.teamvita.hotel.model;

public class SpaDecorator extends ServicioDecorator {
    public SpaDecorator(Servicio servicio) {
        super(servicio);
    }

    @Override
    public double calcularCosto() {
        return servicio.calcularCosto() + 50.0; // Precio de ejemplo
    }
}
