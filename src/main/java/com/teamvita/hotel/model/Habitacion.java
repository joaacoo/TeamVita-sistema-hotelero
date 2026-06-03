package com.teamvita.hotel.model;

public abstract class Habitacion {
    private int numero;
    private double precioBase;
    private boolean disponible;

    public abstract double obtenerPrecio();
}
