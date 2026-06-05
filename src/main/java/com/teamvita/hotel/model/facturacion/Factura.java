package com.teamvita.hotel.model.facturacion;

public class Factura {
    private double total;

    public Factura(double total) {
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public void generarFactura() {
        System.out.println("FACTURA GENERADA");
        System.out.println("Total a pagar: $" + total);
    }
}
