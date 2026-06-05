package com.teamvita.hotel.model;

import com.teamvita.hotel.model.fidelizacion.CategoriaFidelizacion;
import com.teamvita.hotel.model.fidelizacion.CategoriaClasica;

public class Huesped {
    private int id;
    private String nombre;
    private String email;
    private String telefono;
    private CategoriaFidelizacion categoria;

    public Huesped(int id, String nombre, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.categoria = new CategoriaClasica(); // Por defecto
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public CategoriaFidelizacion getCategoria() { return categoria; }
    public void setCategoria(CategoriaFidelizacion categoria) { this.categoria = categoria; }
}
