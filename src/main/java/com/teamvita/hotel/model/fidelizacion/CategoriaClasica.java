package com.teamvita.hotel.model.fidelizacion;

import com.teamvita.hotel.model.*;
import com.teamvita.hotel.model.habitacion.*;
import com.teamvita.hotel.model.reserva.*;
import com.teamvita.hotel.model.servicio.*;
import com.teamvita.hotel.model.fidelizacion.*;
import com.teamvita.hotel.model.facturacion.*;

public class CategoriaClasica implements CategoriaFidelizacion {
    @Override
    public double calcularDescuento(double montoBase) {
        return 0; // Sin descuento
    }
}
