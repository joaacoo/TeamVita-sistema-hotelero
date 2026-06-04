package com.teamvita.hotel.model.reserva;

import com.teamvita.hotel.model.*;
import com.teamvita.hotel.model.habitacion.*;
import com.teamvita.hotel.model.reserva.*;
import com.teamvita.hotel.model.servicio.*;
import com.teamvita.hotel.model.fidelizacion.*;
import com.teamvita.hotel.model.facturacion.*;

import java.util.Date;

public class DetalleReserva {
    private Habitacion habitacion;
    private Date fechaInicio;
    private Date fechaFin;
    private int cantidadPersonas;

    public int calcularNoches() {
        return 0; // TODO: Implementar
    }
}
