package org.iesvdm.tienda.Trader;

public class Trader {

    private final String nombre;

    private final String ciudad;

    public Trader(String n, String c) {
        this.nombre = n;
        this.ciudad = c;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String toString() {
        return "Trader:" + this.nombre + " in " + this.ciudad;
    }
}

