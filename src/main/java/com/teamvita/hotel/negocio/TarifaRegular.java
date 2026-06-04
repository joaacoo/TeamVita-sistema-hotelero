package com.teamvita.hotel.negocio;
import com.teamvita.hotel.model.*;
import java.util.List;

public class TarifaRegular implements TarifaStrategy {
    @Override
    public double calcularTarifa(double precioBase) { return precioBase; }
}
