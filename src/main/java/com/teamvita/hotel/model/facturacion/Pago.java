package com.teamvita.hotel.model.facturacion;

import com.teamvita.hotel.model.*;
import com.teamvita.hotel.model.habitacion.*;
import com.teamvita.hotel.model.reserva.*;
import com.teamvita.hotel.model.servicio.*;
import com.teamvita.hotel.model.fidelizacion.*;
import com.teamvita.hotel.model.facturacion.*;

import java.util.Date;

public class Pago {
    private int id;
    private double monto;
    private Date fechaPago;
    private String metodoPago;
    private Reserva reserva;

    public void registrarPago() {}
}
