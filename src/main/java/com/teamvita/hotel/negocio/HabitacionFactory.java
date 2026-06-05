package com.teamvita.hotel.negocio;

import com.teamvita.hotel.model.habitacion.*;

public class HabitacionFactory {
    public Habitacion crearHabitacion(String tipo, int numero) {
        switch (tipo.toLowerCase()) {
            case "simple":
                return new HabitacionSimple(numero);
            case "doble":
                return new HabitacionDoble(numero);
            case "suite":
                return new Suite(numero);
            default:
                throw new IllegalArgumentException("Tipo de habitacion desconocido: " + tipo);
        }
    }
}
