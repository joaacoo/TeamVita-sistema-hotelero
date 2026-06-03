package com.teamvita.hotel.model;

import java.util.Date;
import java.util.List;

public class Estadia {
    private Date fechaCheckIn;
    private Date fechaCheckOut;
    private double costoTotal;
    private Reserva reserva;
    private List<Servicio> serviciosAdicionales;
    private Factura factura;

    public double calcularCostoTotal() {
        return 0; // TODO: Implementar
    }
}
