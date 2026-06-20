package com.teamvita.hotel.negocio;

import com.teamvita.hotel.model.Huesped;
import com.teamvita.hotel.model.habitacion.Habitacion;
import com.teamvita.hotel.model.reserva.Reserva;
import com.teamvita.hotel.model.reserva.DetalleReserva;
import com.teamvita.hotel.model.Estadia;
import com.teamvita.hotel.repo.HuespedDAO;
import com.teamvita.hotel.repo.ReservaDAO;
import com.teamvita.hotel.repo.PagoDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SistemaHotelFacade {
    private HabitacionFactory habitacionFactory;
    private HuespedDAO huespedDAO;
    private ReservaDAO reservaDAO;
    private PagoDAO pagoDAO;
    private com.teamvita.hotel.repo.AcompananteDAO acompananteDAO;
    private com.teamvita.hotel.repo.HabitacionDAO habitacionDAO;

    public SistemaHotelFacade() {
        this.habitacionFactory = new HabitacionFactory();
        this.huespedDAO = new HuespedDAO();
        this.habitacionDAO = new com.teamvita.hotel.repo.HabitacionDAO();
        this.reservaDAO = new ReservaDAO();
        this.pagoDAO = new PagoDAO();
        this.acompananteDAO = new com.teamvita.hotel.repo.AcompananteDAO();
    }

    public Huesped buscarHuespedPorDni(String dni) {
        return huespedDAO.buscarPorDNI(dni);
    }

    public void registrarReservaCompleta(String dni, String nombre, String email, String tel, String categoriaFidelizacion, LocalDate checkIn, LocalDate checkOut, String tipoHab, int numeroHabitacion, double montoTotal, double seniaAbonada, String medioPago, int cantidadPersonas) {
        // 1. Crear y guardar Huesped
        Huesped huesped = new Huesped(0, nombre, email, tel);
        huespedDAO.insertWithDni(huesped, dni, categoriaFidelizacion);
        
        // 2. Crear reserva
        Reserva reserva = new Reserva(huesped);
        Habitacion hab = habitacionFactory.crearHabitacion(tipoHab, numeroHabitacion);
        DetalleReserva detalle = new DetalleReserva(hab, checkIn, checkOut, 1);
        reserva.agregarDetalle(detalle);
        
        // 3. Guardar reserva y obtener ID
        int idReserva = reservaDAO.insert(reserva, montoTotal, cantidadPersonas);
        
        // 4. Guardar detalle de reserva (habitación + fechas) en BD
        if (idReserva != -1) {
            reservaDAO.insertDetalleReserva(idReserva, numeroHabitacion, checkIn, checkOut);
        }
        
        // 5. Registrar Pago de seña con medio de pago
        if(idReserva != -1) {
            pagoDAO.registrarPago(idReserva, seniaAbonada, medioPago);
        }
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

    public void guardarAcompanantes(int idReserva, List<com.teamvita.hotel.model.reserva.Acompanante> acompanantes) {
        for (com.teamvita.hotel.model.reserva.Acompanante a : acompanantes) {
            a.setIdReserva(idReserva);
            acompananteDAO.insertar(a);
        }
    }

    // --- Métodos de Gestión de Habitaciones (Puente con DAO) ---
    public java.util.List<Object[]> obtenerTodasHabitaciones() {
        return habitacionDAO.getAllForTable();
    }

    public void agregarHabitacion(int numero, String tipo, double precioBase, int capacidad) {
        habitacionDAO.insert(numero, tipo, precioBase, capacidad);
    }

    public void editarHabitacion(int numero, String tipo, double precioBase, int capacidad) {
        habitacionDAO.updateCompleto(numero, tipo, precioBase, capacidad);
    }

    public boolean isHabitacionOcupada(int numero) {
        return habitacionDAO.isHabitacionOcupada(numero);
    }

    public Object[] getHabitacionData(int numero) {
        return habitacionDAO.getHabitacionData(numero);
    }
}
