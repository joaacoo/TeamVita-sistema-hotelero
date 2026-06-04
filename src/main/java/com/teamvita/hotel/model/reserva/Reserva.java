package com.teamvita.hotel.model.reserva;

import com.teamvita.hotel.model.*;
import com.teamvita.hotel.model.habitacion.*;
import com.teamvita.hotel.model.reserva.*;
import com.teamvita.hotel.model.servicio.*;
import com.teamvita.hotel.model.fidelizacion.*;
import com.teamvita.hotel.model.facturacion.*;

import java.util.Date;
import java.util.List;

public class Reserva {
    private int id;
    private Date fechaCreacion;
    private Huesped huesped;
    private EstadoReserva estado;
    private List<DetalleReserva> detalles;
    private List<Pago> pagos;
    private Estadia estadia;

    public void confirmarReserva() {}
    public void cancelarReserva() {}
}
