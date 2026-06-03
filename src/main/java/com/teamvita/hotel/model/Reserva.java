package com.teamvita.hotel.model;

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
