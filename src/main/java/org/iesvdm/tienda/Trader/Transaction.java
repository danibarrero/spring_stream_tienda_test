package org.iesvdm.tienda.Trader;

public class Transaction {
    private final Trader trader;
    private final int anio;

    private final int valor;

    public Transaction(Trader trader, int anio, int valor) {
        this.trader = trader;
        this.anio = anio;
        this.valor = valor;
    }

    public Trader getTrader() {
        return this.trader;
    }

    public int getAnio() {
        return anio;
    }

    public int getValor() {
        return valor;
    }

    public String toString() {
        return "{" + this.trader + ", " + "año: " + this.anio + ", " + "valor:" + this.valor + "}";
    }
}