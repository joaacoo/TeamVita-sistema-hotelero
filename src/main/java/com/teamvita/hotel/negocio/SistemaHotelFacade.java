package com.teamvita.hotel.negocio;

import com.teamvita.hotel.model.Huesped;
import com.teamvita.hotel.model.habitacion.Habitacion;
import com.teamvita.hotel.model.reserva.Reserva;
import com.teamvita.hotel.model.reserva.DetalleReserva;
import com.teamvita.hotel.model.Estadia;
import com.teamvita.hotel.repo.HuespedDAO;
import com.teamvita.hotel.repo.ReservaDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SistemaHotelFacade {
    private HabitacionFactory habitacionFactory;
    private HuespedDAO huespedDAO;
    private ReservaDAO reservaDAO;

    public SistemaHotelFacade() {
        this.habitacionFactory = new HabitacionFactory();
        this.huespedDAO = new HuespedDAO();
        this.reservaDAO = new ReservaDAO();
    }

    public void registrarReservaCompleta(String dni, String nombre, String email, String tel, LocalDate checkIn, LocalDate checkOut, String tipoHab) {
        // 1. Crear y guardar Huesped
        Huesped huesped = new Huesped(0, nombre, email, tel);
        // Setear DNI aunque no esta en el constructor original, el DAO lo usa
        // Como no esta en el constructor, asumimos que el DAO insertara el DNI directamente
        huespedDAO.insertWithDni(huesped, dni);
        
        // 2. Crear reserva
        Reserva reserva = new Reserva(huesped);
        Habitacion hab = habitacionFactory.crearHabitacion(tipoHab, 101); // Numero dummy
        DetalleReserva detalle = new DetalleReserva(hab, checkIn, checkOut, 1);
        reserva.agregarDetalle(detalle);
        
        // 3. Guardar reserva
        reservaDAO.insert(reserva);
    }

    public void registrarReserva(Huesped huesped, List<Habitacion> habitaciones, LocalDate checkIn, LocalDate checkOut, int cantPersonas) {
        Reserva reserva = new Reserva(huesped);
        for(Habitacion h : habitaciones) {
            DetalleReserva detalle = new DetalleReserva(h, checkIn, checkOut, cantPersonas);
            reserva.agregarDetalle(detalle);
        }
    }

    public void realizarCheckIn(Reserva reserva) {
        if ("CONFIRMADA".equals(reserva.getEstado().getNombreEstado())) {
            for (DetalleReserva detalle : reserva.getDetalles()) {
                detalle.getHabitacion().setDisponible(false);
            }
            Estadia estadia = new Estadia(reserva, LocalDate.now());
            reserva.setEstadia(estadia);
        }
    }
}
