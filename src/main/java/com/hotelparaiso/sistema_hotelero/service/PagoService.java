package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.model.Pago;
import com.hotelparaiso.sistema_hotelero.model.Reserva;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PagoService {

    private List<Pago> pagos = new ArrayList<>();
    private int contadorId = 0;
    private final ReservaService reservaService;

    public PagoService(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    public List<Pago> listar() {
        List<Pago> resultado = new ArrayList<>();
        for (int i = pagos.size() - 1; i >= 0; i--) {
            resultado.add(pagos.get(i));
        }
        return resultado;
    }

    public List<Reserva> listarReservasPendientes() {
        List<Reserva> pendientes = new ArrayList<>();
        for (Reserva reserva : reservaService.listar()) {
            if (reserva.getMontoPagado() < reserva.getMonto()) {
                pendientes.add(reserva);
            }
        }
        return pendientes;
    }

    public boolean registrarPago(Pago pago, String tipoPago) {
        Reserva reserva = reservaService.buscarPorID(pago.getReservaId());
        if (reserva == null || reserva.getMontoPagado() >= reserva.getMonto()) {
            return false;
        }

        double falta = reserva.getMonto() - reserva.getMontoPagado();
        double adelanto = reserva.getMonto() * 0.50 - reserva.getMontoPagado();
        pago.setMonto("adelanto".equals(tipoPago) ? Math.max(0, adelanto) : falta);
        if (pago.getMonto() <= 0) {
            return false;
        }

        Pago pagoRegistrado = buscarPorReserva(reserva.getId());
        if (pagoRegistrado == null) {
            pago.setId(0);
            pago.setCliente(reserva.getCliente());
            pago.setHabitacion(reserva.getHabitacion());
            guardarPago(pago);
        } else {
            pagoRegistrado.setMonto(pagoRegistrado.getMonto() + pago.getMonto());
            pagoRegistrado.setMetodoPago(pago.getMetodoPago());
            pagoRegistrado.setFecha(pago.getFecha());
            guardarPago(pagoRegistrado);
        }

        reserva.setMontoPagado(reserva.getMontoPagado() + pago.getMonto());
        if (reserva.getMontoPagado() >= reserva.getMonto() * 0.50) {
            reserva.setEstado("Confirmada");
        }
        return true;
    }

    public boolean eliminarPago(int id) {
        Pago pago = buscarPorID(id);
        if (pago == null) {
            return false;
        }
        Reserva reserva = reservaService.buscarPorID(pago.getReservaId());
        if (reserva != null) {
            reserva.setMontoPagado(Math.max(0, reserva.getMontoPagado() - pago.getMonto()));
            reserva.setEstado(reserva.getMontoPagado() >= reserva.getMonto() * 0.50
                    ? "Confirmada" : "Pendiente");
        }
        eliminar(id);
        return true;
    }

    public Pago buscarPorID(int id) {
        for (Pago pago : pagos) {
            if (pago.getId() == id) {
                return pago;
            }
        }
        return null;
    }

    public Pago buscarPorReserva(int reservaId) {
        for (Pago pago : pagos) {
            if (pago.getReservaId() == reservaId) {
                return pago;
            }
        }
        return null;
    }

    public double totalIngresos() {
        double total = 0;
        for (Pago pago : pagos) {
            total += pago.getMonto();
        }
        return total;
    }

    public Pago guardarPago(Pago pago) {
        if (pago.getId() == 0) {
            contadorId++;
            pago.setId(contadorId);
            pagos.add(pago);
        } else {
            for (int i = 0; i < pagos.size(); i++) {
                if (pagos.get(i).getId() == pago.getId()) {
                    pagos.set(i, pago);
                }
            }
        }
        return pago;
    }

    public void eliminar(int id) {
        for (int i = pagos.size() - 1; i >= 0; i--) {
            if (pagos.get(i).getId() == id) {
                pagos.remove(i);
            }
        }
    }
}