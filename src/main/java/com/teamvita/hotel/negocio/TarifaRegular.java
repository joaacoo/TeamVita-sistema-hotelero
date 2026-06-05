package com.teamvita.hotel.negocio;

public class TarifaRegular implements TarifaStrategy {
    @Override
    public double calcularTarifa(double precioBase) {
        return precioBase;
    }
}
