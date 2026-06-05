package com.teamvita.hotel.model.fidelizacion;

public class CategoriaPlatinum implements CategoriaFidelizacion {
    @Override
    public double calcularDescuento(double montoBase) {
        return montoBase * 0.20; // 20% de descuento
    }
}
