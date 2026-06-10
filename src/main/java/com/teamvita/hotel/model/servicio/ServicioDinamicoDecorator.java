package com.teamvita.hotel.model.servicio;

public class ServicioDinamicoDecorator extends ServicioDecorator {
    private String nombreServicioEspecifico;
    private double precioDB;

    public ServicioDinamicoDecorator(Servicio servicioBase, String nombreServicioEspecifico) {
        super(servicioBase);
        this.nombreServicioEspecifico = nombreServicioEspecifico;
        // Obtenemos el precio al momento de instanciar el decorador
        this.precioDB = com.teamvita.hotel.repo.ServicioDAO.getPrecioServicio(nombreServicioEspecifico, 0.0);
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto() + precioDB;
    }
    
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + " + nombreServicioEspecifico;
    }
}
