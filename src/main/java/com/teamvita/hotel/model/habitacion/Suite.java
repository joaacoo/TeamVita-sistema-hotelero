package com.teamvita.hotel.model.habitacion;

public class Suite extends Habitacion {
    public Suite(int numero) {
        super(numero, 150.0);
    }

    @Override
    public double obtenerPrecio() {
        return getPrecioBase();
    }
}
