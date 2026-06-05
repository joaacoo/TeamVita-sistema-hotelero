package com.teamvita.hotel.model.habitacion;

public class HabitacionSimple extends Habitacion {
    public HabitacionSimple(int numero) {
        super(numero, 50.0);
    }

    @Override
    public double obtenerPrecio() {
        return getPrecioBase();
    }
}
