package com.teamvita.hotel.model.fidelizacion;

public class CategoriaClasica implements CategoriaFidelizacion {
    @Override
    public double calcularDescuento(double montoBase) {
        return 0; // Sin descuento
    }
}
