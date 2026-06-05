package com.teamvita.hotel.model.servicio;

public class LavanderiaDecorator extends ServicioDecorator {
    public LavanderiaDecorator(Servicio servicioBase) {
        super(servicioBase);
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto() + 15.0;
    }
    
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Lavanderia";
    }
}
