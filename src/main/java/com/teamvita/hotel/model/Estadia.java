package com.teamvita.hotel.model;

import com.teamvita.hotel.model.habitacion.*;
import com.teamvita.hotel.model.reserva.*;
import com.teamvita.hotel.model.servicio.*;
import com.teamvita.hotel.model.fidelizacion.*;
import com.teamvita.hotel.model.facturacion.*;

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
