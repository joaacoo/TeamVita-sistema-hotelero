package com.teamvita.hotel.model.servicio;

public class SpaDecorator extends ServicioDecorator {
    public SpaDecorator(Servicio servicioBase) {
        super(servicioBase);
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto() + 50.0;
    }
    
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Spa";
    }
}
