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

    public SistemaHotelFacade() {
        this.habitacionFactory = new HabitacionFactory();
        this.huespedDAO = new HuespedDAO();
        this.reservaDAO = new ReservaDAO();
        this.pagoDAO = new PagoDAO();
    }

    public void registrarReservaCompleta(String dni, String nombre, String email, String tel, String categoriaFidelizacion, LocalDate checkIn, LocalDate checkOut, String tipoHab, int numeroHabitacion, double montoTotal, double seniaAbonada, String medioPago) {
        // 1. Crear y guardar Huesped
        Huesped huesped = new Huesped(0, nombre, email, tel);
        huespedDAO.insertWithDni(huesped, dni, categoriaFidelizacion);
        
        // 2. Crear reserva
        Reserva reserva = new Reserva(huesped);
        Habitacion hab = habitacionFactory.crearHabitacion(tipoHab, numeroHabitacion);
        DetalleReserva detalle = new DetalleReserva(hab, checkIn, checkOut, 1);
        reserva.agregarDetalle(detalle);
        
        // 3. Guardar reserva y obtener ID
        int idReserva = reservaDAO.insert(reserva, montoTotal);
        
        // 4. Guardar detalle de reserva (habitación + fechas) en BD
        if (idReserva != -1) {
            try {
                java.sql.Connection con = com.teamvita.hotel.repo.ConexionBD.getInstancia().getConexion();
                java.sql.PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO detalle_reserva (id_reserva, numero_habitacion, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)");
                ps.setInt(1, idReserva);
                ps.setInt(2, numeroHabitacion);
                ps.setDate(3, java.sql.Date.valueOf(checkIn));
                ps.setDate(4, java.sql.Date.valueOf(checkOut));
                ps.executeUpdate();
                ps.close();
            } catch (Exception e) {
                System.err.println("[Facade] Error guardando detalle_reserva: " + e.getMessage());
            }
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
}
