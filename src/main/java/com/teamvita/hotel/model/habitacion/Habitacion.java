package com.teamvita.hotel.model.habitacion;

public abstract class Habitacion {
    private int numero;
    private double precioBase;
    private boolean disponible;

    public Habitacion(int numero, double precioBase) {
        this.numero = numero;
        this.precioBase = precioBase;
        this.disponible = true;
    }

    public int getNumero() { return numero; }
    public double getPrecioBase() { return precioBase; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public abstract double obtenerPrecio();
}
