package com.teamvita.hotel.model;

public class CategoriaGold implements CategoriaFidelizacion {
    @Override
    public double calcularDescuento(double montoBase) {
        return montoBase * 0.10; // 10% de descuento
    }
}
