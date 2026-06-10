package com.teamvita.hotel.model.reserva;

public class Acompanante {
    private int id;
    private int idReserva;
    private String nombre;
    private String dni;

    public Acompanante(int id, int idReserva, String nombre, String dni) {
        this.id = id;
        this.idReserva = idReserva;
        this.nombre = nombre;
        this.dni = dni;
    }

    public Acompanante(String nombre, String dni) {
        this.nombre = nombre;
        this.dni = dni;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
}
