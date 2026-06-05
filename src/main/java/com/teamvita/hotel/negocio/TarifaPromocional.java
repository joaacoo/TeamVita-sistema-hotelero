package com.teamvita.hotel.negocio;

public class TarifaPromocional implements TarifaStrategy {
    @Override
    public double calcularTarifa(double precioBase) {
        return precioBase * 0.85; // 15% descuento
    }
}
