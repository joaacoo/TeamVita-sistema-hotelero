package com.teamvita.hotel.model.habitacion;

public class HabitacionDoble extends Habitacion {
    public HabitacionDoble(int numero) {
        super(numero, 80.0);
    }

    @Override
    public double obtenerPrecio() {
        return getPrecioBase();
    }
}
