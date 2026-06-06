package com.teamvita.hotel.model.servicio;

public class RestauranteDecorator extends ServicioDecorator {
    public RestauranteDecorator(Servicio servicioBase) {
        super(servicioBase);
    }

    @Override
    public double calcularCosto() {
        double precioDB = com.teamvita.hotel.repo.ServicioDAO.getPrecioServicio("Restaurante", 30.0);
        return super.calcularCosto() + precioDB;
    }
    
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Restaurante";
    }
}
