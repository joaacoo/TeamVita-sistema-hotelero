package com.teamvita.hotel.model;

public abstract class ServicioDecorator implements Servicio {
    protected Servicio servicio;

    public ServicioDecorator(Servicio servicio) {
        this.servicio = servicio;
    }
}
