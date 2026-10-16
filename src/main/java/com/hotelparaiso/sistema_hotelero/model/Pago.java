package com.hotelparaiso.sistema_hotelero.model;

import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    @Transient
    private int reservaId;

    private String cliente;
    private String habitacion;
    private Double monto;

    @Column(name = "metodo_pago")
    private String metodoPago;

    private LocalDate fecha;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReservaId() {
        return reserva != null ? reserva.getId() : reservaId;
    }
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

    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }
}