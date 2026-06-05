package com.teamvita.hotel.model;

import com.teamvita.hotel.model.habitacion.*;
import com.teamvita.hotel.model.reserva.*;
import com.teamvita.hotel.model.servicio.*;
import com.teamvita.hotel.model.fidelizacion.*;
import com.teamvita.hotel.model.facturacion.*;

public class Huesped {
    private int id;
    private String nombre;
    private String email;
    private String telefono;
    private CategoriaFidelizacion categoria;
}
