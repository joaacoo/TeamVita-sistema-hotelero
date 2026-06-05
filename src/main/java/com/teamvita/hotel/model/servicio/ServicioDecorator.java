package com.teamvita.hotel.model.servicio;

public abstract class ServicioDecorator implements Servicio {
    protected Servicio servicioBase;

    public ServicioDecorator(Servicio servicioBase) {
        this.servicioBase = servicioBase;
    }
    
    @Override
    public double calcularCosto() {
        if (servicioBase != null) {
            return servicioBase.calcularCosto();
        }
        return 0;
    }
    
    @Override
    public String getDescripcion() {
        if (servicioBase != null) {
            return servicioBase.getDescripcion();
        }
        return "";
    }
}
