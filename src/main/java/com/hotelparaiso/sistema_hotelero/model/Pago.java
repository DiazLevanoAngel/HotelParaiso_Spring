package com.hotelparaiso.sistema_hotelero.model;

import java.time.LocalDate;

public class Pago {
    private int id;
    private int reservaId;
    private String cliente;
    private String habitacion;
    private Double monto;
    private String metodoPago;
    private LocalDate fecha;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReservaId() { return reservaId; }
    public void setReservaId(int reservaId) { this.reservaId = reservaId; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getHabitacion() { return habitacion; }
    public void setHabitacion(String habitacion) { this.habitacion = habitacion; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}