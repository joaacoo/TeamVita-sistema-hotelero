package com.teamvita.hotel.model.servicio;

public class SpaDecorator extends ServicioDecorator {
    public SpaDecorator(Servicio servicioBase) {
        super(servicioBase);
    }

    @Override
    public double calcularCosto() {
        double precioDB = com.teamvita.hotel.repo.ServicioDAO.getPrecioServicio("Spa", 50.0);
        return super.calcularCosto() + precioDB;
    }
    
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Spa";
    }
}
