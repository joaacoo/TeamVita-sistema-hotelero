package com.teamvita.hotel.model.servicio;

public class RestauranteDecorator extends ServicioDecorator {
    public RestauranteDecorator(Servicio servicioBase) {
        super(servicioBase);
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto() + 30.0;
    }
    
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Restaurante";
    }
}
