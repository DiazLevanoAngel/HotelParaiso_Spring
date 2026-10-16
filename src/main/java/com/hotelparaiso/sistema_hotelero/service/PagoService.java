package com.hotelparaiso.sistema_hotelero.service;

import com.hotelparaiso.sistema_hotelero.model.Pago;
import com.hotelparaiso.sistema_hotelero.model.Reserva;
import com.hotelparaiso.sistema_hotelero.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaService reservaService;

    public PagoService(PagoRepository pagoRepository, ReservaService reservaService) {
        this.pagoRepository = pagoRepository;
        this.reservaService = reservaService;
    }

    public List<Pago> listar() {
        return pagoRepository.findAllByOrderByIdDesc();
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

    @Transactional
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

        pago.setId(0);
        pago.setReserva(reserva);
        pago.setCliente(reserva.getCliente());
        pago.setHabitacion(reserva.getHabitacion());
        pagoRepository.save(pago);

        reservaService.actualizarEstadoPorPagos(reserva);
        return true;
    }

    @Transactional
    public boolean eliminarPago(int id) {
        Pago pago = buscarPorID(id);
        if (pago == null) {
            return false;
        }
        Reserva reserva = pago.getReserva();
        pagoRepository.delete(pago);
        if (reserva != null) {
            reservaService.actualizarEstadoPorPagos(reserva);
        }
        return true;
    }

    public Pago buscarPorID(int id) {
        return pagoRepository.findById(id).orElse(null);
    }

    public double totalIngresos() {
        double total = 0;
        for (Pago pago : pagoRepository.findAll()) {
            total += pago.getMonto();
        }
        return total;
    }

    public Pago guardarPago(Pago pago) {
        return pagoRepository.save(pago);
    }

    public void eliminar(int id) {
        pagoRepository.deleteById(id);
    }
}