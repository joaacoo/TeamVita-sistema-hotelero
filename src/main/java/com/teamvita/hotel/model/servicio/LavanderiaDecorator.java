package com.teamvita.hotel.model.servicio;

public class LavanderiaDecorator extends ServicioDecorator {
    public LavanderiaDecorator(Servicio servicioBase) {
        super(servicioBase);
    }

    @Override
    public double calcularCosto() {
        double precioDB = com.teamvita.hotel.repo.ServicioDAO.getPrecioServicio("Lavanderia", 15.0);
        return super.calcularCosto() + precioDB;
    }
    
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Lavanderia";
    }
}
